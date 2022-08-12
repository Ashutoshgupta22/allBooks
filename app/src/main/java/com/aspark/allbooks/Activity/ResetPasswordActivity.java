package com.aspark.allbooks.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aspark.allbooks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    private final String TAG = "ResetPasswordActivity";
    Button resetBtn, loginBtn;
    EditText emailResetPassword;
    TextView showConfirmation_tv;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetBtn = findViewById(R.id.resetBtn);
        loginBtn = findViewById(R.id.loginResetPasswordBtn);
        emailResetPassword = findViewById(R.id.resetPassword_EditText);
        showConfirmation_tv =findViewById(R.id.showResetPasswordSent_tv);

        showConfirmation_tv.setVisibility(View.INVISIBLE);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailResetPassword.getText().toString().trim();

               checkIfEmailRegistered();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    private void checkIfEmailRegistered() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        Log.i(TAG, "onComplete: task "+task);
                        if (task.isSuccessful()) {

                            boolean emailEmpty = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();
                            if (! emailEmpty) {
                                Log.d(TAG, "onComplete: email exists");

                                sendPasswordResetEmail();
                            }
                            else
                                Toast.makeText(ResetPasswordActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();

                        }
                        else
                            Log.d(TAG, "onComplete: fetching email methods Task failed");


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ResetPasswordActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void sendPasswordResetEmail() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Log.d(TAG, "onComplete: Password reset email sent successfully");
                        showConfirmation_tv.setVisibility(View.VISIBLE);
                        resetBtn.setClickable(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ResetPasswordActivity.this, "Could not send Password Reset email. " +
                                "PLease try again.", Toast.LENGTH_LONG).show();
                    }
                });

    }

}