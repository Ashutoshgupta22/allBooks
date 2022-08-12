package com.aspark.allbooks.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.aspark.allbooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerifyActivity extends AppCompatActivity {

    private final String TAG = "EmailVerifyActivity";

    TextView email_tv;
    Button verifyBtn;
    String  email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        email_tv = findViewById(R.id.email_verify_TV);
        verifyBtn = findViewById(R.id.verifyBtn);

        email = getIntent().getStringExtra("email");
        email_tv.setText(email);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(EmailVerifyActivity.this,LoginActivity.class));

            }
        });
    }


}