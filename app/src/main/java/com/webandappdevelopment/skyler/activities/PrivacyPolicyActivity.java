package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.webandappdevelopment.skyler.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        findViewById(R.id.back).setOnClickListener(view -> {
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        });
        webView = findViewById(R.id.wv);

        webView.loadUrl("https://www.app.skyler.co.in/privacy_policy.html");
        webView.getSettings().setJavaScriptEnabled(true);
    }
}