package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.webandappdevelopment.skyler.R;

public class TermsConditionsActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        findViewById(R.id.back).setOnClickListener(view -> {
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        });
        webView = findViewById(R.id.wv);

        webView.loadUrl("https://www.app.skyler.co.in/terms_and_conditions.html");

        // this will enable the javascript.
        webView.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
    }
}