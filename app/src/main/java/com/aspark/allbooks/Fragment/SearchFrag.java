package com.aspark.allbooks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Activity.SearchActivity;
import com.aspark.allbooks.Adapter.RecentlyViewedAdapter;
import com.aspark.allbooks.FireStore;
import com.aspark.allbooks.Network.NetworkRequest;
import com.aspark.allbooks.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchFrag extends Fragment implements View.OnClickListener {

    final String TAG = "SearchFrag";
    RecyclerView recentlyViewed_RV;
    ScrollView recentlyViewedScrollView;
    CardView searchCardView;
    SearchView searchView;
    TextView queryHintTextView,noDataFound;
    Context context;

    public SearchFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.searchView);
        recentlyViewed_RV = view.findViewById(R.id.recentlyViewed_RV);
        recentlyViewedScrollView = view.findViewById(R.id.recentlyViewedScrollView);
        searchCardView = view.findViewById(R.id.searchCardView);
        queryHintTextView = view.findViewById(R.id.queryHintTextView);
        noDataFound = view.findViewById(R.id.noDataFoundSearch_tv);

         context = view.getContext();

        searchView.clearFocus();
        recentlyViewedScrollView.setVisibility(View.VISIBLE);

        Log.i(TAG, "onViewCreated: showRecentlyViewData");
        showData();

        queryHintTextView.setOnClickListener(this);
        searchView.setOnClickListener(this);
        searchCardView.setOnClickListener(this);

    }

    private void showData() {

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recentlyViewed_RV.setLayoutManager(layoutManager);
        recentlyViewed_RV.stopScroll();
        recentlyViewed_RV.setAdapter(new RecentlyViewedAdapter());

        Log.i(TAG, "showData: Calling fireStore");
        FireStore fireStore = new FireStore(context);
        fireStore.getRecentlyViewed(recentlyViewed_RV,noDataFound);

    }

    @Override
    public void onClick(View view) {

        Log.d("TAG", "onClick: searchCard clicked ");
        startActivity(new Intent(view.getContext(), SearchActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: refresh recentlyViewed");
        showData();

    }
}
