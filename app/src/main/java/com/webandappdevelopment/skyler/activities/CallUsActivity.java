package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.webandappdevelopment.skyler.R;

public class CallUsActivity extends AppCompatActivity {

    private TextView tvContact;
    private Button btnCall1, btnCall2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_us);

        findViewById(R.id.back).setOnClickListener(view -> {
            finish();
        });

        String CONTACT_CONTENT = "For any query or offline service booking contact us at";
        tvContact = findViewById(R.id.contact_tvContact);
        tvContact.setText(CONTACT_CONTENT);
        btnCall1 = findViewById(R.id.contact_btnCall1);
        btnCall2 = findViewById(R.id.contact_btnCall2);

        btnCall1.setOnClickListener(view ->{
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:+917805919117"));
            startActivity(i);
        });

        btnCall2.setOnClickListener(view ->{
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:+918889557784"));
            startActivity(i);
        });
    }
}