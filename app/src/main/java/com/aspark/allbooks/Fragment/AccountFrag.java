package com.aspark.allbooks.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Adapter.AccountAdapter;
import com.aspark.allbooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFrag extends Fragment {
    Button signOutBtn;
    RecyclerView account_RV;
    ImageView profilePic;

    public AccountFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signOutBtn= view.findViewById(R.id.signOutBtn);
        account_RV = view.findViewById(R.id.account_RV);
        profilePic = view.findViewById(R.id.profilePicAccount);

        profilePic.setBackgroundResource(R.drawable.profile_background);
        profilePic.setImageResource(R.drawable.logo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        account_RV.setLayoutManager(layoutManager);
        account_RV.setAdapter(new AccountAdapter());



        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               FirebaseAuth.getInstance().signOut();
                Log.i(TAG, "User Signed Out");
            }
        });
    }
}
