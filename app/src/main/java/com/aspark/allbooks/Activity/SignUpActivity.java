package com.aspark.allbooks.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aspark.allbooks.FireStore;
import com.aspark.allbooks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    EditText firstName_editText, lastName_editText,email_editText,mobileNumber_editText,password_editText;
    Button signUpBtn;
    String firstName,lastName,email,mobileNumber,password;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName_editText = findViewById(R.id.firstName_EditText);
        lastName_editText = findViewById(R.id.lastName_EditText);
        email_editText = findViewById(R.id.email_EditText);
        mobileNumber_editText = findViewById(R.id.mobileNumber_EditText);
        password_editText = findViewById(R.id.password_EditText);
        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = firstName_editText.getText().toString().trim();
                lastName = lastName_editText.getText().toString().trim();
                email = email_editText.getText().toString().trim();
                mobileNumber = mobileNumber_editText.getText().toString().trim();
                password = password_editText.getText().toString().trim();

                if (firstName.isEmpty())
                    firstName_editText.setError("First name cannot be empty");

                if (email.isEmpty())
                    email_editText.setError("Email cannot be empty");

               if (password.isEmpty())
                   password_editText.setError("Password cannot be empty");
               else {

                   if (password.length() <6)
                       password_editText.setError("Password must have at least 6 characters");
                   else
                    {
                       if (! firstName.isEmpty() && ! email.isEmpty()){

                           checkEmailExists();
                       }
                   }
               }
            }
        });

    }

    private void checkEmailExists() {



        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        if (task.isSuccessful()) {

                            boolean resultEmpty = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();
                            if (! resultEmpty) {
                                email_editText.setError("Email already registered");
//                                Toast.makeText(SignUpActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                registerUser();
                            }
                        }
                        else
                            Log.d(TAG, "onComplete: fetching email methods Task failed");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SignUpActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void registerUser() {
        Log.d(TAG, "registerUser: Registering new User ");

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onSuccess: User registered successfully");
                            currentUser = task.getResult().getUser();

                            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                            updateUserInfo();
                        }
                        else
                            Toast.makeText(SignUpActivity.this, "Cannot register, please try again", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void updateUserInfo() {

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstName)
                .build();

        currentUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: User display name updated successfully");

                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser !=null) {
                                Log.d(TAG, "onComplete: Adding user to backend");
                                FireStore fireStore = new FireStore(getApplicationContext());
                                fireStore.addUser(currentUser.getUid(),firstName,lastName,mobileNumber,email,password,"not_null");
                            }
                        }

                        else
                            Log.e(TAG, "onComplete: display name update task unsuccessfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: Display name update FAILED");
                    }
                });

    }
}