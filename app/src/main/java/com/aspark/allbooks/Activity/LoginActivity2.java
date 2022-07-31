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

import com.aspark.allbooks.FireStore;
import com.aspark.allbooks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity2 extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    Button loginBtn2;
    EditText loginPasswordEditText;
    TextView forgotPassword;
    String password, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        loginBtn2 = findViewById(R.id.loginBtn2);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        forgotPassword = findViewById(R.id.forgotPassword);

        email = getIntent().getStringExtra("email");

        loginBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = loginPasswordEditText.getText().toString().trim();

                if ( password.isEmpty())
                    loginPasswordEditText.setError("Password cannot be empty");

                else {

                    if (password.length() <6)
                        loginPasswordEditText.setError("Password must have at least 6 characters");
                        else
                            verifyPassword();
                    }


            }
        });

    }

    private void verifyPassword() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d(TAG, "onSuccess: Password verified");
                            startActivity(new Intent(LoginActivity2.this,MainActivity.class));
                        }
                        else
                            loginPasswordEditText.setError("Wrong password");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Something went wrong");
                    }
                });




//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//       Query query = db.collection("users")
//               .whereEqualTo("email",email)
//               .whereEqualTo("password",password);
//
//       query.get()
//               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                   @Override
//                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                       if (task.isSuccessful()) {
//
//                           QuerySnapshot snapshot = task.getResult();
//                           if (! snapshot.isEmpty()) {
//
//                               Log.d(TAG, "onComplete: USER VERIFIED , email and password  ");
//
//
//
//                           }
//                           else
//                               Log.e(TAG, "onComplete: Something went wrong snapshot is empty" );
//                       }
//                       else
//                           Log.e(TAG, "onComplete: Task unsuccessful" );
//                   }
//               }).addOnFailureListener(new OnFailureListener() {
//                   @Override
//                   public void onFailure(@NonNull Exception e) {
//
//                       Log.e(TAG, "onFailure: something went wrong " +e.getMessage() );
//                   }
//               });


    }
}