package rs.ac.uns.ftn.eventsapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.UserLoginDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UserProfileChangeDTO;
import rs.ac.uns.ftn.eventsapp.firebase.FirebaseSignIn;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class UserProfileActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 123;
    private String imgUri;
    private CircleImageView userProfile;
    private Button selectImageBtn;
    private TextInputEditText name_profile;
    private TextInputEditText email_profile;
    private TextInputEditText password_current_profile;
    private TextInputEditText password_new1_profile;
    private TextInputEditText password_new2_profile;
    private Retrofit retrofit;
    private Bitmap bitmap;
    private MediaType mediaType;
    private Bundle oldUserState;

    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //        intent.putExtra("userId", loggedUser.getId());
        //        intent.putExtra("userName", loggedUser.getName());
        //        intent.putExtra("email", loggedUser.getEmail());
        //        intent.putExtra("password", loggedUser.getPassword());
        //        intent.putExtra("profileImageUrl", loggedUser.getProfileImageUrl());
        oldUserState = getIntent().getExtras();
        if (savedInstanceState != null) imgUri = savedInstanceState.getString("imgUri");

        Toolbar toolbar = findViewById(R.id.toolbarUserProfile);
        toolbar.setTitle(R.string.your_profile_page);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userProfile = findViewById(R.id.circle_image_view_profile);
        selectImageBtn = findViewById(R.id.btn_select_photo_profile);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUri == null || !imgUri.equals("")) {
                    openContextMenu(v);
                } else {
                    startChangeImageIntent();
                }
            }
        });
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imgUri.equals("")) {
                    openContextMenu(v);
                } else {
                    startChangeImageIntent();
                }
            }
        });
        registerForContextMenu(userProfile);

        Button updateProfileBtn = findViewById(R.id.updateProfileBtn);
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasChange()) {
                    if (validateParams()) {
                        saveUserParams();
                    } else {
                        return;
                    }
                } else {
                    notifyNoChanges();
                    finish();
                }
            }
        });

        name_profile = findViewById(R.id.name_profile);
        email_profile = findViewById(R.id.email_profile);
        password_current_profile = findViewById(R.id.password_current_profile);
        password_new1_profile = findViewById(R.id.password_new1_profile);
        password_new2_profile = findViewById(R.id.password_new2_profile);

        setParameters(oldUserState);
    }

    private void notifyNoChanges() {
        Toast.makeText(this, "No changes were made to the account", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("imgUri", imgUri);
    }

    /**
     * Sets text boxes and image to current profile state
     *
     * @param oldUserState
     */
    private void setParameters(@NonNull Bundle oldUserState) {
        name_profile.setText(oldUserState.getString("userName"));
        email_profile.setText(oldUserState.getString("email"));
        String userImage = oldUserState.getString("profileImageUrl");
        if (imgUri == null) {
            imgUri = userImage;
        }

        if (imgUri != null && !imgUri.equals("")) {
            /*try {
                if (imgUri.startsWith("http")){
                    Picasso.get().load(Uri.parse(imgUri)).placeholder(R.drawable.ic_user_icon).into(userProfile);
                } else {
                    Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + imgUri)).placeholder(R.drawable.ic_user_icon).into(userProfile);
                }
                selectImageBtn.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                userProfile.setImageResource(R.drawable.ic_user_icon);
            }*/
            //check if it's local image
            try {
                Uri uri = Uri.parse(imgUri);
                InputStream is = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(is);
                mediaType = MediaType.parse(getContentResolver().getType(uri));
                userProfile.setImageURI(uri);
            } catch (Exception e) {
                //not local image
                Picasso.get().setLoggingEnabled(true);
                if (imgUri.startsWith("http")){
                    Picasso.get().load(Uri.parse(imgUri)).placeholder(R.drawable.ic_user_icon).into(userProfile);
                } else {
                    Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + imgUri)).placeholder(R.drawable.ic_user_icon).into(userProfile);
                }
            } finally {
                selectImageBtn.setVisibility(View.INVISIBLE);
            }
        } else {
            userProfile.setImageResource(R.drawable.ic_user_icon);
        }
    }

    /**
     * Rukuje se rezultatom gore poslatog intenta za odabir profilne slike
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            if (imageData != null) {
                addImage(imageData);
            }
        }
    }

    private void startChangeImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), GALLERY_REQUEST);
    }

    private void removeImage() {
        imgUri = "";
        bitmap = null;
        mediaType = null;
        userProfile.setImageURI(null);
        selectImageBtn.setVisibility(View.VISIBLE);
    }

    private void addImage(Uri imageData) {
        try {
            InputStream is = getContentResolver().openInputStream(imageData);
            bitmap = BitmapFactory.decodeStream(is);
            mediaType = MediaType.parse(getContentResolver().getType(imageData));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imgUri = imageData.toString();
        userProfile.setImageURI(imageData);
        selectImageBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu_profile_image, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_image_option_delete:
                removeImage();
                return true;
            case R.id.profile_image_option_change:
                startChangeImageIntent();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.collapse_menu_item:
                return true;
            case R.id.action_remove_account_menu_item:
                removeAccountClick();
                return true;
            case R.id.action_unlink_fb_menu_item:
                unlinkFacebook();
                return true;
            default:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dijalog za potvrdu prekidanje veze Events naloga i Facebook-a sa obaveznim unosom lozinke
     */
    private void unlinkFacebook() {
        final AlertDialog.Builder notifyDialogBuilder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_unlink_facebook, null);
        notifyDialogBuilder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.unlink_account, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText psw = view.findViewById(R.id.password_unlink_account);
                        if (psw.getText().toString().equals(oldUserState.getString("password"))) {
                            // unlink account
                            sendUnlinkFBRequest(email_profile.getText().toString(), psw.getText().toString());
                        } else {
                            //wrong password
                            Toast.makeText(UserProfileActivity.this, "Password is not correct! Action canceled.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //close dialog
                    }
                });

        notifyDialogBuilder.create().show();
    }


    /**
     * Unlink FB account from Events account
     * @param email
     * @param password
     */
    private void sendUnlinkFBRequest(String email, String password) {
        retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        UserAppApi api = retrofit.create(UserAppApi.class);
        Call<User> call = api.unlink(new UserLoginDTO(email, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: FB unlinking failed");
                } else {
                    FirebaseAuth userFirebaseInstance = FirebaseAuth.getInstance();
                    userFirebaseInstance.signOut();
                    AppDataSingleton.getInstance().deleteAllPhysical();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: FB unlinking failed");
            }
        });
    }


    /**
     * Dijalog za potvrdu brisanja Events naloga sa obaveznim unosom lozinke
     */
    private void removeAccountClick() {
        final AlertDialog.Builder notifyDialogBuilder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_remove_account, null);
        notifyDialogBuilder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.delete_account, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText psw = view.findViewById(R.id.password_remove_account);
                        if (psw.getText().toString().equals(oldUserState.getString("password"))) {
                            // delete account
                            removeAccount(email_profile.getText().toString(), psw.getText().toString());
                        } else {
                            //wrong password
                            Toast.makeText(UserProfileActivity.this, "Password is not correct! Action canceled.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //close dialog
                    }
                });

        notifyDialogBuilder.create().show();
    }

    /**
     * Remove account from Events app
     * @param email
     * @param password
     */
    private void removeAccount(String email, String password) {
        retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        UserAppApi api = retrofit.create(UserAppApi.class);
        Call<Void> call = api.delete(new UserLoginDTO(email, password));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: FB unlinking failed");
                } else {
                    FirebaseAuth userFirebaseInstance = FirebaseAuth.getInstance();
                    userFirebaseInstance.signOut();
                    AppDataSingleton.getInstance().deleteAllPhysical();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: FB unlinking failed");
            }
        });
    }

    @Override
    public void onBackPressed() {
        //provera da li su promenjeni atributi i da li su oni sacuvani i obavestavalje korisnika
        if (hasChange()) {
            notifyUserSaveChanges();
        } else {
            //notifyNoChanges();
            super.onBackPressed();
            finish();
        }
    }

    /**
     * Obavesti korisnika da postoje nesacuvane izmene i pitaj ga da li da ih sacuva
     */
    private void notifyUserSaveChanges() {
        AlertDialog.Builder notifyDialogBuilder = new AlertDialog.Builder(this);
        notifyDialogBuilder.setMessage("You have made changes to your account. Do you want to save them?");
        notifyDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user wants to save account
                if (validateParams()) {
                    saveUserParams();
                    finish();
                    UserProfileActivity.super.onBackPressed();
                }
            }
        });
        notifyDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user wants to dismiss
                finish();
                UserProfileActivity.super.onBackPressed();
            }
        });
        notifyDialogBuilder.create().show();
    }

    /**
     * Provera da li je korisnik nesto promenio
     *
     * @return true ako ima promene, false ako nema
     */
    private Boolean hasChange() {
        if (!name_profile.getText().toString().equals(oldUserState.getString("userName"))) {
            return true;
        }
        if (!imgUri.equals(oldUserState.getString("profileImageUrl"))) {
            return true;
        }
        if (!password_current_profile.getText().toString().equals("") ||
                !password_new1_profile.getText().toString().equals("") ||
                !password_new2_profile.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private void saveUserParams() {
        UserProfileChangeDTO user = new UserProfileChangeDTO(email_profile.getText().toString(), name_profile.getText().toString().trim(), imgUri, password_current_profile.getText().toString(), password_new1_profile.getText().toString(), password_new2_profile.getText().toString());

        retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        UserAppApi api = retrofit.create(UserAppApi.class);
        Call<User> call = api.update(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                        return;
                    } else if (response.code() == 400) {
                        Toast.makeText(getApplicationContext(), "Passwords are not valid! Please reenter passwords", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Log.d(TAG, "onErrorResponse: Server didn't receive FB token!");
                        Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    User loggedUser = response.body();
                    FirebaseSignIn firebaseSignIn = new FirebaseSignIn(getApplicationContext());
                    firebaseSignIn.saveOrUpdateUserInFirebase(loggedUser.getImageUri(),
                            loggedUser.getName(),loggedUser.getEmail());
                    if (bitmap != null) {
                        uploadImage(loggedUser.getId());
                    } else {
                        //update user in db
                        AppDataSingleton.getInstance().updateUser(response.body());

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("xxs", "onFailure: updating profile failed");
            }
        });

        Toast.makeText(this, "Account changes are successfully applied!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Validiraj ispravnost unetih parametara i ispisi gresku ako nisu validni
     *
     * @return
     */
    private boolean validateParams() {
        if (!isValidName(name_profile.getText())) {
            name_profile.setError(getString(R.string.registerUserValidationName2));
            return false;
        }

        //check password
        if (!password_current_profile.getText().toString().equals("") ||
                !password_new1_profile.getText().toString().equals("") ||
                !password_new2_profile.getText().toString().equals("")) {

            //old password not same
            if (!password_current_profile.getText().toString().equals(oldUserState.getString("password"))) {
                password_current_profile.setError("Old password is not correct!");
                return false;
            }
            //new password not valid
            if (!isValidPsw(password_new1_profile.getText())) {
                password_new1_profile.setError(getString(R.string.registerUserValidationPsw4));
                return false;
            }
            //repeat password empty
            if (password_new2_profile.getText().toString().trim().equals("")) {
                password_new2_profile.setError(getString(R.string.registerUserValidationPsw2));
                return false;
            }
            //2 new passwords not match
            if (!password_new1_profile.getText().toString().equals(password_new2_profile.getText().toString())) {
                password_new2_profile.setError(getString(R.string.registerUserValidationPsw3));
                return false;
            }
        }
        return true;
    }

    private boolean isValidPsw(CharSequence target) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])((?=.*[@#$%^&+=!])|(?=.*[0-9]))(?=\\S+$).{7,}$";
        return (!TextUtils.isEmpty(target) && target.toString().matches(regex));
    }

    private boolean isValidName(CharSequence target) {
        String regex = "^\\p{L}+[\\p{L} .'-]{2,64}$";
        return (!TextUtils.isEmpty(target) && target.toString().matches(regex));
    }

    private void uploadImage(Long userId) {
        retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        UserAppApi e = retrofit.create(UserAppApi.class);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
        builder.addFormDataPart("image", "image", RequestBody.create(mediaType, bos.toByteArray()));
        RequestBody requestBody = builder.build();
        Call<User> s = e.uploadImage(requestBody, userId);
        s.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                    Log.d("xxs", "onResponse: image uploaded success");
                } else {
                    //update user and return to last screen
                    AppDataSingleton.getInstance().updateUser(response.body());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("xxs", "onResponse: image upload failed");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remove_profile, menu);
        if (oldUserState.getString("fbId").equals("")) {
            MenuItem item = menu.findItem(R.id.action_unlink_fb_menu_item);
            item.setVisible(false);
        }
        return true;
    }

}
