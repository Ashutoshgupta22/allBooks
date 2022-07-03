package com.aspark.allbooks;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Fragment.SearchFrag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FireStore {
    String TAG = "FireStore";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> recentlyViewedList = new ArrayList<>();
    Context context;

    public FireStore(Context context) {
        this.context = context;
    }

    public void addUser(String userId,String email) {

        Map<String, Object>  users = new HashMap<>();
        users.put("user",email);
        users.put("refresh_token","abcdefghijklmnopqrstuvwxyz");

        db.collection("users").document(userId)
                .set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d(TAG, "onSuccess: user added");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, "onFailure: user NOT added");
                    }
                });


    }

    public void addRecentlyViewed(String userId,String volumeId) {

        Map<String,Object> recently_viewed = new HashMap<>();
//        ArrayList<String> list = new ArrayList<>();
//        list.add(volumeId);
        recently_viewed.put("volumeId_"+volumeId,volumeId);

        db.collection("recently_viewed").document(userId)
                .set(recently_viewed, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: RecentlyViewed updated");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: couldn't update recentlyViewed " + e.getMessage());

                    }
                });

    }

    public ArrayList<String> getRecentlyViewed(String userId, RecyclerView recentlyViewed_RV) {

        DocumentReference documentReference = db.collection("recently_viewed").document(userId);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {

                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()){

                                Log.d(TAG, "recently_viewed exists "+snapshot.getData());
                                Map<String,Object> map = snapshot.getData();

                                if (map != null) {
                                    for (Map.Entry<String,Object> entry : map.entrySet()) {

                                        String key = entry.getKey();

                                        Log.d(TAG, "Map entry " + key);
                                        Log.d(TAG, "entry value " + entry.getValue());
                                        recentlyViewedList.add(entry.getValue().toString());

                                    }
                                }

                                Log.i(TAG, "recentlyViewedList "+ recentlyViewedList);
                                SearchFrag searchFrag = new SearchFrag();
                                searchFrag.setRecentlyViewed_RV(context,recentlyViewedList,recentlyViewed_RV);

                            } else
                                Log.i(TAG, "RecentlyView document does not exists");

                        }else
                            Log.i(TAG, "recentlyViewed task Failed "+task.getException());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, "onFailure : recentlyViewed "+e.getMessage() );
                    }
                });

        return recentlyViewedList;
    }

    public void createCollections(String userId) {

        Map<String,Object> first = new HashMap<>();
        first.put("first","");

        db.collection("recently_viewed").document(userId)
                .set(first)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d(TAG, " recently_viewed created");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, " recently_viewed NOT  created");

                    }
                });
    }

    public void getRefreshToken(String userId) {

    }
}