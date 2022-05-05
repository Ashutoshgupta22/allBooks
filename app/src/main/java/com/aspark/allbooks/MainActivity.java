package com.aspark.allbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.IllegalFormatCodePointException;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        frameLayout = findViewById(R.id.frameLayout);

        userName = getIntent().getStringExtra("UserName");


        NavigationBarView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {

            Fragment selectedFragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home){
                    selectedFragment = new homeFrag(userName);

                }
                else if (item.getItemId() == R.id.search){
                    selectedFragment = new searchFrag();

                }

               else if (item.getItemId() == R.id.bookshelf){
                    selectedFragment = new bookshelfFrag();

                }
                else {
                    selectedFragment = new accountFrag();

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectedFragment).commit();

                return true;
            }
        };
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new homeFrag(userName)).commit();
    }
}