package rs.ac.uns.ftn.eventsapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;

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

    private Bundle oldUserState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //        intent.putExtra("userId", loggedUser.getUserId());
        //        intent.putExtra("userName", loggedUser.getUserName());
        //        intent.putExtra("email", loggedUser.getEmail());
        //        intent.putExtra("password", loggedUser.getPassword());
        //        intent.putExtra("profileImageUrl", loggedUser.getProfileImageUrl());
        oldUserState = getIntent().getExtras();
        if (savedInstanceState != null) imgUri = savedInstanceState.getString("imgUri");

        Toolbar toolbar = findViewById(R.id.toolbarUserProfile);
        toolbar.setTitle("Your profile page");
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
                        finish();
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
            try {
                Picasso.get().setLoggingEnabled(true);
                Picasso.get().load(Uri.parse(imgUri)).into(userProfile);
                selectImageBtn.setVisibility(View.INVISIBLE);
            } catch (Exception e){
                e.printStackTrace();
            }
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
        userProfile.setImageURI(null);
        selectImageBtn.setVisibility(View.VISIBLE);
    }

    private void addImage(Uri imageData) {
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
                            //TODO: unlink fb account
                            Toast.makeText(UserProfileActivity.this, "Facebook account is unlinked from Events account!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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
                            //TODO: remove account
                            TestMockup.getInstance().users.remove(0);

                            Toast.makeText(UserProfileActivity.this, "Account is deleted! Goodbye.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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
        //TODO: sacuvaj nove parametre
        TestMockup.getInstance().users.get(0).setUserName(name_profile.getText().toString().trim());
        TestMockup.getInstance().users.get(0).setPassword(password_new1_profile.getText().toString().trim());
        TestMockup.getInstance().users.get(0).setProfileImageUrl(imgUri);

        Intent intent = new Intent();
        //intent.putExtra("userName", name_profile.getText().toString().trim());
        //intent.putExtra("imgUri", imgUri);
        setResult(RESULT_OK, intent);

        Toast.makeText(this, "Account changes are successfully applied!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Validiraj ispravnost unetih parametara i ispisi gresku ako nisu validni
     *
     * @return
     */
    private boolean validateParams() {
        if (name_profile.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Name must contain at least one letter or number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //check password
        if (!password_current_profile.getText().toString().equals("") ||
                !password_new1_profile.getText().toString().equals("") ||
                !password_new2_profile.getText().toString().equals("")) {

            //old password not same
            if (!password_current_profile.getText().toString().equals(oldUserState.getString("password"))) {
                Toast.makeText(this, "Old password is not correct!", Toast.LENGTH_SHORT).show();
                return false;
            }
            //new password not valid
            if (password_new1_profile.getText().toString().trim().equals("") || password_new1_profile.getText().toString().trim().length() < 4) {
                Toast.makeText(this, "New passwords must be at least 4 characters long!", Toast.LENGTH_SHORT).show();
                return false;
            }
            //2 new passwords not match
            if (!password_new1_profile.getText().toString().equals(password_new2_profile.getText().toString())) {
                Toast.makeText(this, "New passwords are not matching!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remove_profile, menu);
        return true;
    }

}
