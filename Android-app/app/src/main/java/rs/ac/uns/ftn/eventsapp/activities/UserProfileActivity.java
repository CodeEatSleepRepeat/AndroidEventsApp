package rs.ac.uns.ftn.eventsapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;
import rs.ac.uns.ftn.eventsapp.R;

public class UserProfileActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST=123;
    private String imgUri = "";
    private CircleImageView userProfile;
    private Button selectImageBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbarUserProfile);
        toolbar.setTitle("Your profile page");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userProfile = findViewById(R.id.circle_image_view_profile);
        selectImageBtn = findViewById(R.id.btn_select_photo_profile);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageExists()) {
                    openContextMenu(v);
                } else {
                    changeImage();
                }
            }
        });
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageExists()) {
                    openContextMenu(v);
                } else {
                    changeImage();
                }
            }
        });
        registerForContextMenu(userProfile);

    }

    /*
     * Rukuje se rezultatom gore poslatog intenta za odabir profilne slike
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri imageData = data.getData();
            imgUri = imageData.toString();
            userProfile.setImageURI(imageData);
            selectImageBtn.setVisibility(View.INVISIBLE);
        }
    }

    private boolean imageExists() {
        return !imgUri.equals("");
    }

    private void changeImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), GALLERY_REQUEST);
    }

    private void removeImage(){
        imgUri = "";
        userProfile.setImageURI(null);
        selectImageBtn.setVisibility(View.VISIBLE);
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
                changeImage();
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

    private void unlinkFacebook() {
        //TODO: dodati dijalog za potvrdu brisanja sa obaveznim unosom lozinke!!! https://developer.android.com/guide/topics/ui/dialogs
        Toast.makeText(this, "Unlink facebook", Toast.LENGTH_SHORT).show();
    }

    private void removeAccountClick() {
        //TODO: dodati dijalog za potvrdu brisanja sa obaveznim unosom lozinke!!! https://developer.android.com/guide/topics/ui/dialogs
        Toast.makeText(this, "Remove account", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remove_profile, menu);
        return true;
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
