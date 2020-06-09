package rs.ac.uns.ftn.eventsapp.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.threeten.bp.ZonedDateTime;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.UserLoginDTO;
import rs.ac.uns.ftn.eventsapp.firebase.FirebaseSignIn;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.sync.SyncGoingInterestedEventsTask;
import rs.ac.uns.ftn.eventsapp.sync.SyncMyEventsTask;
import rs.ac.uns.ftn.eventsapp.sync.SyncReceiverInitTask;
import rs.ac.uns.ftn.eventsapp.sync.SyncUserTask;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private Retrofit retrofit;
    private long lastSyncTime;
    private EditText email;
    private EditText psw;
    private final String EMAIL = "email";
    private final String EVENTS = "user_events";
    private final String TAG = this.getClass().getName();
    private SyncReceiverInitTask syncReceiverInitTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupFacebookLogin();

        Button btnLogin = findViewById(R.id.btn_login_login);
        TextView textForgotPassword = findViewById(R.id.text_forgot_password_login);
        TextView textBackToRegister = findViewById(R.id.text_continue_as_anonymous_sign_in);
        TextView textContinueAsAnonymous = findViewById(R.id.text_continue_as_anonymous_login);

        email = findViewById(R.id.edittext_email_login);
        psw = findViewById(R.id.password_edittext_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationSuccess()) {
                    loginUser();
                }
            }
        });

        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordClicked();
            }
        });

        textBackToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });

        textContinueAsAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainWindow();   //as unauthorized, of course
            }
        });

        syncReceiverInitTask = new SyncReceiverInitTask();
        //register broadcast listener
        IntentFilter filter = new IntentFilter();
        filter.addAction(SplashScreenActivity.SYNC_MY_EVENTS);
        filter.addAction(SplashScreenActivity.SYNC_GI_EVENTS);
        registerReceiver(syncReceiverInitTask, filter);
    }

    private boolean validationSuccess() {
        if (email.getText().toString().trim().equals("")) {
            //Toast.makeText(getApplicationContext(), R.string.registerUserValidationEmail, Toast.LENGTH_LONG).show();
            email.setError(getString(R.string.registerUserValidationEmail));
            return false;
        }
        if (!isValidEmail(email.getText())) {
            //Toast.makeText(getApplicationContext(), R.string.registerUserValidationEmail2, Toast.LENGTH_LONG).show();
            email.setError(getString(R.string.registerUserValidationEmail2));
            return false;
        }
        if (psw.getText().toString().trim().equals("")) {
            //Toast.makeText(getApplicationContext(), R.string.registerUserValidationPsw, Toast.LENGTH_LONG).show();
            psw.setError(getString(R.string.loginPasswordBlank));
            return false;
        }
        return true;
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.fullyInitialize();

        LoginButton loginButton = findViewById(R.id.login_button_login);
        loginButton.setPermissions(Arrays.asList(EMAIL, EVENTS));

        // Callback login for facebook
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                Log.d(TAG, "onSuccess: token  = " + accessToken.getToken());
                Log.d(TAG, "onSuccess: logged = " + isLoggedIn);

                Log.d(TAG, "onClick:");

                // Instantiate the backend request
                retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.localhost_uri))
                        .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                        .build();
                UserAppApi api = retrofit.create(UserAppApi.class);
                Call<User> call = api.login(accessToken.getToken());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 403) {
                                email.setError(getString(R.string.facebookNotAvailable));
                            } else {
                                Log.d(TAG, "onErrorResponse: Server didn't receive FB token!");
                                Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                                goToNoServer();
                            }
                        } else {
                            Log.d("TAG", response.body().getId().toString());
                            User loggedUser = response.body();
                            addUserToDB(loggedUser);

                            lastSyncTime = ZonedDateTime.now().toInstant().toEpochMilli();
                            SharedPreferences sharedPreferences = getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong(SyncUserTask.preferenceSyncUser, lastSyncTime);
                            editor.commit();


                            signInToFirebase(loggedUser);
                            //FirebaseLogin firebaseLogin = new FirebaseLogin(LoginActivity.this);
                            //firebaseLogin.loginWithEmailAndPassword(loggedUser.getEmail(), loggedUser.getPassword());    //TODO: ne sme da blokira dalji tok programa - moze biti interno poslat na register!

                            //start fetching other data in background
                            //new SyncMyEventsTask(getApplicationContext()).execute();
                            //new SyncGoingInterestedEventsTask(getApplicationContext()).execute();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                        Log.d("xxs", "onFailure: login failed: " + t.getMessage());
                        t.printStackTrace();
                        goToNoServer();
                    }
                });
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: " + exception.toString());
                Toast.makeText(getApplicationContext(), "Facebook sent exception: " + exception.getCause(), Toast.LENGTH_LONG).show();
                goToNoServer();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginUser() {
        UserLoginDTO user = new UserLoginDTO(email.getText().toString().trim(), psw.getText().toString());

        // Instantiate the backend request
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        UserAppApi api = retrofit.create(UserAppApi.class);
        Call<User> call = api.login(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        psw.setError(getString(R.string.userNotFound));
                        psw.setText("");
                    } else if (response.code() == 403) {
                        Toast.makeText(getApplicationContext(), "Please activate your account to continue!", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "onErrorResponse: Server didn't receive FB token!");
                        Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                        goToNoServer();
                    }
                } else {
                    Log.d("TAG", response.body().getId().toString());
                    User loggedUser = response.body();
                    addUserToDB(loggedUser);
                    signInToFirebase(loggedUser);

                    lastSyncTime = ZonedDateTime.now().toInstant().toEpochMilli();
                    SharedPreferences sharedPreferences = getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SyncUserTask.preferenceSyncUser, lastSyncTime);
                    editor.commit();

                    //FirebaseLogin firebaseLogin = new FirebaseLogin(LoginActivity.this);
                    //firebaseLogin.loginWithEmailAndPassword(loggedUser.getEmail(), loggedUser.getPassword());    //TODO: ne sme da blokira dalji tok programa

                    //start fetching other data in background
                    //new SyncMyEventsTask(getApplicationContext()).execute();
                    //new SyncGoingInterestedEventsTask(getApplicationContext()).execute();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("xxs", "onFailure: login failed");
                goToNoServer();
            }
        });
    }

    private void addUserToDB(User user) {
        AppDataSingleton.getInstance().setContext(this);
        AppDataSingleton.getInstance().createUser(user);
    }

    private void forgotPasswordClicked() {
        Intent intent = new Intent(this, ForgottenPasswordActivity.class);
        startActivity(intent);
    }

    private void goToRegisterActivity() {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToMainWindow() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToNoServer() {
        Intent intent = new Intent(this, NoServerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void signInToFirebase(User registeredUser) {
        //nakon uspesne registracije registrujemo ga i na firebase
        FirebaseSignIn firebaseSignIn = new FirebaseSignIn(LoginActivity.this);

        firebaseSignIn.performSignIn(registeredUser.getEmail(),
                registeredUser.getPassword(), registeredUser.getName(), registeredUser.getImageUri());
    }

    @Override
    protected void onDestroy() {
        //osloboditi resurse
        if (syncReceiverInitTask != null) {
            unregisterReceiver(syncReceiverInitTask);
        }
        super.onDestroy();
    }
}
