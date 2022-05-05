package com.aspark.allbooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class bookshelfFrag extends Fragment {
    ScrollView shelfScrollView;
    networkRequest networkReq;
    RecyclerView shelfRecyclerView;
    public static final int SHELF_REQ_CODE = 20;

    public bookshelfFrag() {



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookshelf,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shelfScrollView = view.findViewById(R.id.shelfScrollView);
        shelfRecyclerView = view.findViewById(R.id.shelfRecyclerView);

        networkReq = new networkRequest("",getContext(),shelfRecyclerView);
        networkReq.search(SHELF_REQ_CODE);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        shelfRecyclerView.setLayoutManager(layoutManager);


    }
}
