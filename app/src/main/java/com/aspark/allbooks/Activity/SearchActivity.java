package com.aspark.allbooks.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aspark.allbooks.Network.NetworkRequest;
import com.aspark.allbooks.R;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView searchActivity_RV;
    String input;
    NetworkRequest networkReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchViewActivity);
        searchActivity_RV = findViewById(R.id.searchActivity_RV);


        // this function is called when query is submitted or changed.

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                input = query.trim().replace(" ","+");
                searchView.clearFocus();
                Log.i("", "onQueryTextSubmit: input is "+input);

                networkReq  = new NetworkRequest(getApplicationContext(),searchActivity_RV);
                networkReq.search(input);

                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                searchActivity_RV.setLayoutManager(layoutManager);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}