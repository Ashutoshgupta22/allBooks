package com.aspark.allbooks;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebAuthActivity extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;
    WebViewClient client;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_auth);

        Log.i(TAG, "onCreate: webAuthActivity called");

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webViewAuth);

        url = getIntent().getStringExtra("url");

        Log.i(TAG, "onCreate: url "+url);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

//        client = new WebViewClient();
//        webView.setWebViewClient(client);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

       // webView.loadUrl(url);
    }
}