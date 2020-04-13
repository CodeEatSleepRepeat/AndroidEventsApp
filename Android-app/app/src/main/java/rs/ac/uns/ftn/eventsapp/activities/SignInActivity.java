package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import rs.ac.uns.ftn.eventsapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button btnLogin = findViewById(R.id.btn_login_sign_in);
        Button btnRegister =findViewById(R.id.btn_register_sign_in);
        TextView textContinueAsAnnoymous = findViewById(R.id.text_back_to_register_login);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });

        textContinueAsAnnoymous.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToMainWindowAsUnauthorized();
            }
        });

    }

    private void goToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToMainWindowAsUnauthorized(){
        //TODO: Go to main window.
    }

}
