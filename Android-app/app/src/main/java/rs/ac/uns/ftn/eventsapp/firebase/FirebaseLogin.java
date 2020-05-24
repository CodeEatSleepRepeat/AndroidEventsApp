package rs.ac.uns.ftn.eventsapp.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rs.ac.uns.ftn.eventsapp.MainActivity;

public class FirebaseLogin {

    public static String FIREBASE_LOGIN_TAG = "rs.ac.uns.ftn.eventsapp.firebase.FirebaseLogin";

    private FirebaseAuth firebaseAuthInstance = FirebaseAuth.getInstance();
    private Context activityContext;


    public FirebaseLogin(Context context){
        activityContext = context;
    }

    public void loginWithEmailAndPassword(String email,String password){
        firebaseAuthInstance.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) activityContext, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToMainWindowAsAuthorized();
                        } else {
                            InformAboutLoginFailure(task);
                        }
                    }
                });
    }

    private void InformAboutLoginFailure(@NonNull Task<AuthResult> task) {
        Log.w(FIREBASE_LOGIN_TAG, "signInWithEmail:failure", task.getException());
        Toast.makeText(activityContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    private void goToMainWindowAsAuthorized() {
        Intent intent = new Intent(activityContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityContext.startActivity(intent);
    }
}