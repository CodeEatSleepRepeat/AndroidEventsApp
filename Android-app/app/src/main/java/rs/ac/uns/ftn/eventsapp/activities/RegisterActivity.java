package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnRegister = findViewById(R.id.btn_register_register);
        Button btnSelectImage = findViewById(R.id.btn_select_photo_register);
        TextView textAlreadyHaveAccount = findViewById(R.id.text_already_have_an_account_register);
        TextView textContinueAsAnnoymous = findViewById(R.id.text_continue_as_anonymous_register);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        textAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });

        textContinueAsAnnoymous.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToMainWindowAsUnauthorized();
            }
        });


    }


    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void selectImage() {
        // TODO: Select image from user disk
    }

    private void registerUser() {
        // TODO: Register user on system then upload image to it
    }

    private void goToMainWindowAsUnauthorized(){
        //TODO: Go to main window.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(SignInActivity.IS_ANONYMOUS, true);
        startActivity(intent);
    }
}
