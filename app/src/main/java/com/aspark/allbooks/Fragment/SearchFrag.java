package com.aspark.allbooks.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Activity.SearchActivity;
import com.aspark.allbooks.DataModel;
import com.aspark.allbooks.Network.NetworkRequest;
import com.aspark.allbooks.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFrag extends Fragment {

    RecyclerView recentlyViewed_RV;
    ScrollView recentlyViewedScrollView;
    CardView searchCardView;
    SearchView searchView;

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

        searchView.clearFocus();

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG", "onClick: searchCard clicked ");
                startActivity(new Intent(view.getContext(), SearchActivity.class));
            }
        });
    }

}
