package com.aspark.allbooks.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Adapter.bookshelfNameAdapter;
import com.aspark.allbooks.FireStore;
import com.aspark.allbooks.Network.NetworkRequest;
import com.aspark.allbooks.R;

import java.util.ArrayList;
import java.util.List;

public class BookshelfFrag extends Fragment {
    ScrollView shelfScrollView;
    RecyclerView shelfRecyclerView, bookshelfName_RV;
    List<String> bookshelfNameList = new ArrayList<>();
    FireStore fireStore;

    public BookshelfFrag() {
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
        bookshelfName_RV = view.findViewById(R.id.bookshelfName_RV);

        fireStore = new FireStore(view.getContext());

        bookshelfNameList.add("Favorites");
        bookshelfNameList.add("To Read");
        bookshelfNameList.add("Bookshelf 1");
        bookshelfNameList.add("Bookshelf 2");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        bookshelfName_RV.setLayoutManager(linearLayoutManager);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        shelfRecyclerView.setLayoutManager(layoutManager);
        fireStore.getBookshelf("Favorites",shelfRecyclerView);


        bookshelfName_RV.setAdapter(new bookshelfNameAdapter(bookshelfNameList,shelfRecyclerView));

    }
}
