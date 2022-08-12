package com.aspark.allbooks.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Adapter.ShelfAdapter;
import com.aspark.allbooks.Network.HomeNetReq;
import com.aspark.allbooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class HomeFrag extends Fragment {
    private final String TAG ="HomeFrag";
    String userName;
    TextView userNameTextView;
    HomeNetReq homeNetReq;
    RecyclerView romance_RV, recommended_RV ,horror_RV , biography_RV, fiction_RV,mystery_RV,business_RV;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public HomeFrag() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userNameTextView= view.findViewById(R.id.userNameTextView);
        romance_RV = view.findViewById(R.id.romanceRecyclerView);
//        recommended_RV = view.findViewById(R.id.recommendedRecyclerView);
        horror_RV = view.findViewById(R.id.horrorRecyclerView);
        fiction_RV = view.findViewById(R.id.fictionRecyclerView);
        biography_RV = view.findViewById(R.id.biographyRecyclerView);
        business_RV = view.findViewById(R.id.businessRecyclerView);
        mystery_RV = view.findViewById(R.id.mysteryRecyclerView);

      if (currentUser!=null) {
          userName = Objects.requireNonNull(currentUser.getDisplayName()).split(" ")[0];
          userNameTextView.setText(userName);
          Log.i(TAG, "onViewCreated: display name= "+userName);
      }

        LinearLayoutManager layoutManager ;

//        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
//        recommended_RV.setLayoutManager(layoutManager);
//        recommended_RV.setAdapter(new ShelfAdapter(view.getContext(),null));


        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        romance_RV.setLayoutManager(layoutManager);
        romance_RV.setAdapter(new ShelfAdapter(getContext(),null));

        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        fiction_RV.setLayoutManager(layoutManager);
        fiction_RV.setAdapter(new ShelfAdapter(getContext(),null));

        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        mystery_RV.setLayoutManager(layoutManager);
        mystery_RV.setAdapter(new ShelfAdapter(getContext(),null));

        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        horror_RV.setLayoutManager(layoutManager);
        horror_RV.setAdapter(new ShelfAdapter(getContext(),null));

        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        business_RV.setLayoutManager(layoutManager);
        business_RV.setAdapter(new ShelfAdapter(getContext(),null));

        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        biography_RV.setLayoutManager(layoutManager);
        biography_RV.setAdapter(new ShelfAdapter(getContext(),null));

        homeNetReq = new HomeNetReq(view.getContext());

//        homeNetReq.getRecommendedBooks(recommended_RV);
        homeNetReq.searchGenre("romance",romance_RV);
        homeNetReq.searchGenre("fiction", fiction_RV);
        homeNetReq.searchGenre("mystery",mystery_RV);
        homeNetReq.searchGenre("horror thriller",horror_RV);
        homeNetReq.searchGenre("business economics",business_RV);
        homeNetReq.searchGenre("biography autobiography",biography_RV);



    }
}
