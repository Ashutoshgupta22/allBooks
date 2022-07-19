package com.aspark.allbooks;

import static com.aspark.allbooks.Activity.LoginActivity.USER_ID;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Adapter.ShelfAdapter;
import com.aspark.allbooks.Fragment.SearchFrag;
import com.aspark.allbooks.Network.NetworkRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FireStore {
    String TAG = "FireStore";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Context context;

    public FireStore(Context context) {
        this.context = context;
    }

    public void addUser(String userId,String email) {

        Map<String, Object>  users = new HashMap<>();
        users.put("user",email);
        users.put("refresh_token","abcdefghijklmnopqrstuvwxyz");

        db.collection("users").document(userId)
                .set(users,SetOptions.merge())
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
        recently_viewed.put("volumeId",volumeId);
        recently_viewed.put("timestamp",FieldValue.serverTimestamp());

        db.collection("recently_viewed").document(USER_ID).collection("recent").document(volumeId)
                .set(recently_viewed)
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

    public void getRecentlyViewed( RecyclerView recentlyViewed_RV) {

        ArrayList<String> recentlyViewedList = new ArrayList<>();

        Query query = db.collection("recently_viewed").document(USER_ID).collection("recent")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(12);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots.size() != 0) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        String volumeId = Objects.requireNonNull(snapshot.getString("volumeId")).toString();
                        recentlyViewedList.add(volumeId);

                        Log.d(TAG, "onSuccess: volumeId "+volumeId);

                    }

                    NetworkRequest networkReq = new NetworkRequest(context, recentlyViewed_RV);
                    networkReq.showRecentlyViewed(recentlyViewed_RV, recentlyViewedList);

                }
                else {
                    Log.i(TAG, "onSuccess: NO books found in recently_viewed");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e(TAG, "onFailure: could NOT get recently_viewed "+e.getMessage() );

            }
        });


//        DocumentReference documentReference = db.collection("recently_viewed").document(userId);
//        documentReference.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                        if (task.isSuccessful()) {
//
//                            DocumentSnapshot snapshot = task.getResult();
//                            if (snapshot.exists()){
//
//                                Log.d(TAG, "recently_viewed exists "+snapshot.getData());
//                                Map<String,Object> map = snapshot.getData();
//
//                                if (map != null) {
//                                    for (Map.Entry<String,Object> entry : map.entrySet()) {
//
//                                        String key = entry.getKey();
//
//                                        Log.d(TAG, "Map entry " + key);
//                                        Log.d(TAG, "entry value " + entry.getValue());
//                                        recentlyViewedList.add(entry.getValue().toString());
//
//                                    }
//                                }
//
//                                Log.i(TAG, "recentlyViewedList "+ recentlyViewedList);
//                                SearchFrag searchFrag = new SearchFrag();
//                                searchFrag.setRecentlyViewed_RV(context,recentlyViewedList,recentlyViewed_RV);
//
//                            } else
//                                Log.i(TAG, "RecentlyView document does not exists");
//
//                        }else
//                            Log.i(TAG, "recentlyViewed task Failed "+task.getException());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Log.e(TAG, "onFailure : recentlyViewed "+e.getMessage() );
//                    }
//                });

    }
    
    public void addToBookshelf(String volumeId, String bookshelf) {

        Map<String,Object> map = new HashMap<>();
        map.put("volume_"+volumeId,volumeId);

        db.collection("Bookshelves").document(USER_ID).collection("Bookshelf").document(bookshelf)
                .set(map,SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d(TAG, "onSuccess: Book added to "+bookshelf);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: Book NOT added "+e.getMessage());
                    }
                });
    }

    public void getBookshelf(String bookshelf, RecyclerView bookshelf_RV ) {

        List<String> bookshelfList = new ArrayList<>();
        bookshelf_RV.setAdapter(new ShelfAdapter(context,null));

        db.collection("Bookshelves").document(USER_ID).collection("Bookshelf").document(bookshelf)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            
                            if (snapshot.exists()){
                                Log.d(TAG, "onComplete: GOT the books in "+bookshelf);
                                
                                Map<String,Object> map = snapshot.getData();
                                if (map !=null) {

                                    for (Map.Entry<String,Object> entry : map.entrySet()) {
                                        
                                        bookshelfList.add(entry.getValue().toString());
                                    }
                                    NetworkRequest networkRequest = new NetworkRequest(context);
                                    networkRequest.getBookshelf(bookshelf_RV, bookshelfList);

                                }
                            }
                            else
                                Log.i(TAG, "onComplete: document does NOT exist");
                        }
                        else
                            Log.i(TAG, "onComplete: task is unsuccessful");
                        
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.i(TAG, "onFailure: Could NOT get books from bookshelf "+ bookshelf+" Error "+e.getMessage());
                    }
                });
        
    }
}