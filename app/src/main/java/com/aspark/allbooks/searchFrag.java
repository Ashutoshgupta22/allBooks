package com.aspark.allbooks;

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

import java.util.ArrayList;
import java.util.List;

public class searchFrag extends Fragment {

    RecyclerView recyclerView;
    ScrollView recentlyViewedScrollView;
    CardView searchCardView;
    SearchView searchView;
    String input;
    networkRequest networkReq;
    List<BooksData> booksDataList = new ArrayList<>();
    public static final int SEARCH_REQ_CODE = 10;
//     String[] titleArray = new String[100];
//     String[] authorArray = new String[100];
//     String[] coverUrlArray = new String[100];


    public searchFrag() {
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
        recyclerView = view.findViewById(R.id.recyclerView);
        recentlyViewedScrollView = view.findViewById(R.id.recentlyViewedScrollView);
        searchCardView = view.findViewById(R.id.searchCardView);

        recyclerView.setVisibility(View.GONE);

        searchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG", "onClick: searchCard clicked ");
                recentlyViewedScrollView.setVisibility(View.GONE);
            }
        });



        // this function is called when query is submitted or changed.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {

               input = s.trim().replace(" ","+");
               searchView.clearFocus();
               Log.i("", "onQueryTextSubmit: input is "+input);

               networkReq  = new networkRequest(input,getContext(),recyclerView);
               booksDataList =  networkReq.search(SEARCH_REQ_CODE);
//               Log.i("TAG", "onQueryTextSubmit: is bookList null "+booksDataList.isEmpty());
//               searchAdapter = new SearchAdapter(getContext(),booksDataList);

               GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
               recyclerView.setLayoutManager(layoutManager);
               recyclerView.setVisibility(View.VISIBLE);


               return false;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               return false;
           }
       });

    }

//    public void setTitleArray(String[] titleArray) {
//        this.titleArray = titleArray;
//
//        Log.i("TAG", "setTitleArray: "+this.titleArray[0]);
//    }
//
//    public void setAuthorArray(String[] authorArray) {
//        this.authorArray = authorArray;
//    }
//
//    public void setCoverUrlArray(String[] coverUrlArray) {
//        this.coverUrlArray = coverUrlArray;
//    }
//
//    public void setView() {
//
//
//    }
}
