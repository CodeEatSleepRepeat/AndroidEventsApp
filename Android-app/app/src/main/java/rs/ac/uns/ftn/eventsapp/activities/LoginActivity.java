package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import rs.ac.uns.ftn.eventsapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btn_login_login);
        TextView textForgotPassword = findViewById(R.id.text_forgot_password_login);
        TextView textBackToRegister = findViewById(R.id.text_back_to_register_login);
        ImageView imageFacebookLogin = findViewById(R.id.image_facebook_logo_login);

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

        imageFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInWithFacebook();
            }
        });

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

    private void logInWithFacebook(){
        // TODO: Log in with facebook token
    }


}
