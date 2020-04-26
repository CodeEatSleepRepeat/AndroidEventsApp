package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import rs.ac.uns.ftn.eventsapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private final String EMAIL = "email";
    private final String EVENTS = "user_events";
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupFacebookLogin();

        Button btnLogin = findViewById(R.id.btn_login_login);
        TextView textForgotPassword = findViewById(R.id.text_forgot_password_login);
        TextView textBackToRegister = findViewById(R.id.text_back_to_register_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
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



    }

    private void setupFacebookLogin(){
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button_login);
        loginButton.setPermissions(Arrays.asList(EMAIL, EVENTS));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                Log.d(TAG, "onSuccess: token  = " + accessToken.getToken());
                Log.d(TAG, "onSuccess: logged = " + isLoggedIn);

                Log.d(TAG, "onClick:");

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.14:8080/user/login/" + accessToken.getToken();

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.d(TAG, "Response is: "+ response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: Server didn't receive FB token!");
                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: " + exception.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginUser(){
        // TODO: Log user in system then go to main window
    }

    private void forgotPasswordClicked(){
        // TODO: Do something when user click forgot password
    }

    private void goToRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}
