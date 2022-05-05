package com.aspark.allbooks;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toolbar;

public class WebAuthActivity extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_auth);

        Log.i(TAG, "onCreate: webAuthActivity called");

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webViewAuth);

        url = getIntent().getStringExtra("url");

        webView.loadUrl(url);
    }
}