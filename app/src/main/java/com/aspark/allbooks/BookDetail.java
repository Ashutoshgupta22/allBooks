package com.aspark.allbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookDetail extends AppCompatActivity {

    ImageView bookCover ;
    TextView descriptionView , authorView,titleView;
    TextView no_of_pagesView,languageView,ratingView;
    TextView publisherView ,publishedDateView, categoriesView;
    DataModel bookData;
    RecyclerView fromAuthorRecyclerView;


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
        if (categories.size() !=0)
            categoriesView.setText(categories.get(0));       //TODO display all categories.
        else
            categoriesView.setText("Unknown");

       LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        fromAuthorRecyclerView.setLayoutManager(layoutManager);

        NetworkRequest networkRequest = new NetworkRequest("",this,fromAuthorRecyclerView);
        networkRequest.fromAuthor(authorName );


    }
}