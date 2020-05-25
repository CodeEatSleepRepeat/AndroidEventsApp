package rs.ac.uns.ftn.eventsapp.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.LoginActivity;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;

public class FirebaseSignIn {

    public static String FIREBASE_REGISTER_TAG = "rs.ac.uns.ftn.eventsapp.firebase.FirebaseRegister";

    private FirebaseAuth firebaseAuthInstance = FirebaseAuth.getInstance();
    private Context activityContext;
    private String username;
    private String email;
    private String password;
    private String userImageURI;


    public FirebaseSignIn(Context context){
        activityContext = context;
    }

    public void performSignIn(final String email, String password,
                              final String username, final String userImageURI){
        this.username = username;
        this.email = email;
        this.password = password;
        this.userImageURI = userImageURI;

        Boolean isEmptyDataContainedInParameters =
                email.isEmpty() || password.isEmpty() || username.isEmpty();

        if(isEmptyDataContainedInParameters){
            Toast.makeText(activityContext, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuthInstance.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener((Activity) activityContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(FIREBASE_REGISTER_TAG, "User created on firebase");
//                            if(userImageURI != null)
//                                uploadImageToFirebase(userImageURI);
//                            else
                                saveOrUpdateUserInFirebase(userImageURI, username, email);
                        } else {
                            InformAboutRegisterFailure(task);
                        }
                    }
                });
    }

    private void uploadImageToFirebase(Uri userImageUri) {
        Log.d(FIREBASE_REGISTER_TAG, "createUserWithEmail:success");
        if(userImageUri == null) return;

        String imageFileName = generateRandomImageName();
        final StorageReference imageReference = FirebaseStorage.getInstance().getReference(
                "/images/" + imageFileName);
        imageReference.putFile(userImageUri)
                .addOnSuccessListener((Activity) activityContext, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                saveOrUpdateUserInFirebase(uri.toString(), username, email);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(FIREBASE_REGISTER_TAG,"Upload failed: " + e.getLocalizedMessage());
                    }
                });
    }


    @NotNull
    private String generateRandomImageName() {
        return UUID.randomUUID().toString();
    }

    private void saveOrUpdateUserInFirebase(String profileImageUrl, String username, String email) {
        Log.d(FIREBASE_REGISTER_TAG, "Entered saveOrUpdateUserInFirebase");
        String uid = firebaseAuthInstance.getUid();
        DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance().getReference(
                "/users/"+ uid);

        FirebaseUserDTO firebaseUser = new FirebaseUserDTO(uid, username, profileImageUrl, email);

        userDatabaseReference.setValue(firebaseUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                            //goToMainWindowAsAuthorized();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("sale","Save/update user failed: " + e.getLocalizedMessage());
                    }
                });
    }

    private void InformAboutRegisterFailure(@NonNull Task<AuthResult> task) {
        // if user is already registered then perform login
        if(task.getException().toString().contains("The email address is already in use by another account")){
            loginWithEmailAndPassword(email,password);
            return;
        }
        Log.w(FIREBASE_REGISTER_TAG, "createUserWithEmail:failure",
                task.getException());
        Toast.makeText(activityContext, "Register into message system failed.",
                Toast.LENGTH_SHORT).show();
    }

    public void loginWithEmailAndPassword(String email,String password){
        firebaseAuthInstance.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) activityContext, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("salepare", "LOGIN SUCCESSFUL! TO!");
                        } else {
                            InformAboutLoginFailure(task);
                        }
                    }
                });
    }

    private void InformAboutLoginFailure(@NonNull Task<AuthResult> task) {
        Log.w(FIREBASE_REGISTER_TAG, "signInWithEmail:failure", task.getException());
        Toast.makeText(activityContext, "Message system authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

//    private void loginAfterRegisterActivity() {
//        Toast.makeText(activityContext, activityContext.getText(R.string.confirmRegistration),
//                Toast.LENGTH_LONG).show();
//
//        Intent intent = new Intent(activityContext, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        activityContext.startActivity(intent);
//    }
//
//    private void goToMainWindowAsAuthorized() {
//        Intent intent = new Intent(activityContext, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        activityContext.startActivity(intent);
//    }
}