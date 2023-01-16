package com.webandappdevelopment.skyler.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.activities.CallUsActivity;

import java.util.Objects;

public class SupportFragment extends Fragment {

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support,container,false);



        view.findViewById(R.id.ll_callUs).setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), CallUsActivity.class);
            startActivity(i);
        });

        view.findViewById(R.id.ll_message).setOnClickListener(view1 ->{
            Intent viewIntent = new Intent("android.intent.action.VIEW",
                            Uri.parse("https://api.whatsapp.com/send?phone=918889557784"));
            startActivity(viewIntent);
        });

        return view;
    }

}