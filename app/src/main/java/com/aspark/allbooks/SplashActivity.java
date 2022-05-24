package com.aspark.allbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();



        SharedPreferences preferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        String refreshToken = preferences.getString("refresh_token",null);



        // this method delays the calling of mainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;
                Log.i("splash", "run: Splash Activity current User "+currentUser);
                Log.i("splash", "run: Splash Activity refresh token "+refreshToken);

                if (currentUser != null && refreshToken !=null) {

                    Log.i("splash", "run: WELCOME BACK :)");
                    intent = new Intent(SplashActivity.this, MainActivity.class);


                } else {

                    Log.i("splash", "run: seems like its your first time ;)");

                    intent = new Intent(SplashActivity.this, LoginActivity.class);

                }

                startActivity(intent);
                finish();

            }
        },1500);


    }


}