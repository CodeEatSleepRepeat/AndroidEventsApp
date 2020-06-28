package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class ForgottenPasswordActivity extends Activity {

    private Retrofit retrofit;
    private EditText email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        email = findViewById(R.id.forgotten_psw_reset_email);

        final Button reset = findViewById(R.id.forgotten_psw_reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(email.getText())){
                    resetPassword(email.getText());
                } else {
                    email.setError(getString(R.string.registerUserValidationEmail2));
                }
            }
        });

        final TextView register = findViewById(R.id.forgotten_psw_text_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });
    }

    private void resetPassword(CharSequence text) {
        // Instantiate the backend request
        retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        UserAppApi api = retrofit.create(UserAppApi.class);
        Call<Void> call = api.forgotPassword(text.toString());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        email.setError(getString(R.string.emailNotFound));
                    } else {
                        Log.d("TAG", "onErrorResponse: Server crashed!" + response.code() + " " + response.message());
                        Toast.makeText(getApplicationContext(), getString(R.string.serverToBusy), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //sve ok, server je poslao mail sa novom lozinkom
                    Toast.makeText(getApplicationContext(), getString(R.string.forgottenPswSent), Toast.LENGTH_SHORT).show();
                    goToLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("xxs", "onFailure: registration failed");
            }
        });
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
