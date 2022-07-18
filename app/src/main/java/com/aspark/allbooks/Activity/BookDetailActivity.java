package com.aspark.allbooks.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspark.allbooks.DataModel;
import com.aspark.allbooks.FireStore;
import com.aspark.allbooks.Network.NetworkRequest;
import com.aspark.allbooks.R;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookDetailActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    String TAG = "BookDetailActivity";
    ImageView bookCover ;
    TextView descriptionView , authorView,titleView;
    TextView no_of_pagesView,languageView,ratingView;
    TextView publisherView ,publishedDateView, categoriesView;
    DataModel bookData;
    RecyclerView fromAuthorRecyclerView ,youMayLikeRecyclerView;
    String volumeId;

    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookCover = findViewById(R.id.bookCover);
        descriptionView = findViewById(R.id.descriptionText);
        authorView = findViewById(R.id.authorText);
        titleView = findViewById(R.id.bookTitle);
        no_of_pagesView = findViewById(R.id.no_of_pagesView_bookDetail);
        languageView = findViewById(R.id.languageView_bookDetail);
        ratingView = findViewById(R.id.ratingView_bookDetail);
        publisherView = findViewById(R.id.publisherView);
        publishedDateView  = findViewById(R.id.publishedDateView);
        categoriesView = findViewById(R.id.categoriesView);
        fromAuthorRecyclerView = findViewById(R.id.fromAuthorRecyclerView);
        youMayLikeRecyclerView = findViewById(R.id.youMayLikeRecyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        bookData = (DataModel) getIntent().getSerializableExtra("bookData");

        String coverUrl = bookData.getCoverUrl();
        String authorName = bookData.getAuthor();
        String title = bookData.getTitle();
        String subTitle = bookData.getSubTitle();
        String description = bookData.getDescription();
        String no_of_pages = bookData.getNoOfPages();
        String language = bookData.getLanguage();
        String rating = bookData.getRating();
        String ratingsCount = bookData.getRatingsCount();
        String publisher = bookData.getPublisher();
        String publishedDate = bookData.getPublishedDate();
        List<String> categories = bookData.getCategories();
         volumeId = bookData.getVolumeId();

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(coverUrl)
                .fitCenter()
                .into(bookCover);

        authorView.setText(authorName);
        titleView.setText(title);
        descriptionView.setText(description);
        no_of_pagesView.setText(no_of_pages);
        languageView.setText(language);
        ratingView.setText(rating);
        publisherView.setText(publisher);
        publishedDateView.setText(publishedDate);
        if (categories.size() !=0) {
            //TODO display all categories.
            categoriesView.setText(categories.get(0));


            LinearLayoutManager layoutManager2 =
                    new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            youMayLikeRecyclerView.setLayoutManager(layoutManager2);

            NetworkRequest networkRequest2 = new NetworkRequest(this,youMayLikeRecyclerView);
            networkRequest2.youMayLike(categories);

        }else {
            categoriesView.setText("Unknown");
        }

       LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        fromAuthorRecyclerView.setLayoutManager(layoutManager);

        NetworkRequest networkRequest = new NetworkRequest(this,fromAuthorRecyclerView);
        networkRequest.fromAuthor(authorName );

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopUp(view);
            }
        });
    }

    private void showPopUp(View view) {

        PopupMenu popupMenu = new PopupMenu(BookDetailActivity.this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.getMenuInflater().inflate(R.menu.fab_menu,popupMenu.getMenu());
        popupMenu.show();

    }


    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "onPause: ");

        SharedPreferences preferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        FireStore fireStore = new FireStore(getApplicationContext());
        fireStore.addRecentlyViewed(preferences.getString("userId",null),volumeId);

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem)  {

        int  id = menuItem.getItemId();
        FireStore fireStore = new FireStore(getApplicationContext());

        if (id == R.id.to_read) {

            fireStore.addToBookshelf(volumeId,"To Read");
            Toast.makeText(this, "Added to To Read", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.favourites) {

            fireStore.addToBookshelf(volumeId,"Favorites");
            Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.bookshelf_1) {

            fireStore.addToBookshelf(volumeId,"Bookshelf 1");
            Toast.makeText(this, "Added to Bookshelf 1", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.bookshelf_2) {

            fireStore.addToBookshelf(volumeId,"Bookshelf 2");
            Toast.makeText(this, "Added to Bookshelf 2", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;


    }
}