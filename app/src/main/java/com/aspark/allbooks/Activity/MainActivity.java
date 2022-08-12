package com.aspark.allbooks.Activity;

import static com.aspark.allbooks.FireStore.USER_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.aspark.allbooks.FireStore;
import com.aspark.allbooks.Fragment.AccountFrag;
import com.aspark.allbooks.Fragment.BookshelfFrag;
import com.aspark.allbooks.Fragment.HomeFrag;
import com.aspark.allbooks.Fragment.SearchFrag;
import com.aspark.allbooks.Network.NetworkRequest;
import com.aspark.allbooks.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        frameLayout = findViewById(R.id.frameLayout);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser!=null) {
            USER_ID = currentUser.getUid();
            Log.d(TAG, "onCreate: user id_token= "+currentUser.getIdToken(true));

        }
        Log.d(TAG, "onCreate: USER_ID= "+USER_ID);


        NavigationBarView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {

            Fragment selectedFragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home){
                    selectedFragment = new HomeFrag();

                }
                else if (item.getItemId() == R.id.search){
                    selectedFragment = new SearchFrag();

                }

               else if (item.getItemId() == R.id.bookshelf){
                    selectedFragment = new BookshelfFrag();

                }
                else {
                    selectedFragment = new AccountFrag();

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectedFragment).commit();

                return true;
            }
        };
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new HomeFrag()).commit();

        handleRedirectUri();

    }

    private void handleRedirectUri() {


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Log.i(TAG, "handleRedirectUri: appLinkAction " + appLinkAction);
        Uri appLinkData = appLinkIntent.getData();

        if (appLinkData != null) {

            String authCode = appLinkData.getQueryParameter("code");
            Log.i(TAG, "handleRedirectUri: authCode " + authCode);

            NetworkRequest netReq = new NetworkRequest(getApplicationContext());
            netReq.getAccessToken(authCode);


            if (authCode == null) {
                String errorCode = appLinkData.getQueryParameter("error");
                Log.i(TAG, "handleRedirectUri: ERROR  " + errorCode);

            }
        }


    }

}