package com.webandappdevelopment.skyler.fragments;

import static android.view.View.GONE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.activities.AboutUsActivity;
import com.webandappdevelopment.skyler.activities.ChangePasswordActivity;
import com.webandappdevelopment.skyler.activities.EditProfileActivity;
import com.webandappdevelopment.skyler.activities.MainActivity;
import com.webandappdevelopment.skyler.activities.OrderActivity;
import com.webandappdevelopment.skyler.activities.PrivacyPolicyActivity;
import com.webandappdevelopment.skyler.activities.SavedLocationActivity;
import com.webandappdevelopment.skyler.activities.TermsConditionsActivity;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

public class AccountFragment extends Fragment {

    String s_name, s_mail, s_mobile, s_location, s_result;
    TextView username;
    LinearLayout ll_editProfile, ll_savedLocations, ll_terms, ll_refund, ll_privacy,ll_orderHistory,
            ll_reviewApp, ll_about, ll_signOut, ll_register, ll_username, ll_changepassword;
    InternetConnectionDetector icd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        icd = new InternetConnectionDetector(getContext());
        setUpUi(view);

        return view;
    }

    private void setUpUi(View view) {
        ll_editProfile = view.findViewById(R.id.ll_editprofile);
        ll_savedLocations = view.findViewById(R.id.ll_savedLocations);
        ll_terms = view.findViewById(R.id.ll_termsConditions);
        ll_refund = view.findViewById(R.id.ll_refundPolicy);
        ll_privacy = view.findViewById(R.id.ll_privacyPolicy);
        ll_reviewApp = view.findViewById(R.id.ll_reviewApp);
        ll_about = view.findViewById(R.id.ll_aboutUs);
        ll_signOut = view.findViewById(R.id.ll_signOut);
        ll_register = view.findViewById(R.id.ll_register);
        ll_username = view.findViewById(R.id.ll_username);
        ll_changepassword = view.findViewById(R.id.ll_changepassword);
        ll_orderHistory = view.findViewById(R.id.ll_orderHistory);
        username = view.findViewById(R.id.tv_username);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        s_result = sharedPreferences.getString("STATUS", "NOT REGISTERED");
        s_location = sharedPreferences.getString("CITY",null);
        s_name = sharedPreferences.getString("NAME",null);
        s_mail = sharedPreferences.getString("EMAIL",null);
        s_mobile = sharedPreferences.getString("MOBILE",null);

        username.setText(s_name);

        if (s_result.matches("Already Registered")){
            ll_register.setVisibility(GONE);
        }else{
            ll_register.setVisibility(View.VISIBLE);
            ll_editProfile.setVisibility(GONE);
            ll_username.setVisibility(GONE);
            ll_changepassword.setVisibility(GONE);
            ll_savedLocations.setVisibility(GONE);
            ll_signOut.setVisibility(GONE);
            ll_orderHistory.setVisibility(GONE);
        }

        ll_savedLocations.setOnClickListener(view1 ->{
            Intent i = new Intent(getContext(), SavedLocationActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        ll_changepassword.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        ll_register.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(),MainActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        ll_editProfile.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(),EditProfileActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        ll_reviewApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.webandappdevelopment.skyler"));
                startActivity(intent);
                getActivity().finish();
            }
        });

        ll_terms.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(),TermsConditionsActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        ll_privacy.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(),PrivacyPolicyActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        ll_about.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), AboutUsActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        ll_signOut.setOnClickListener(view1 -> {
            Dialog dialog = new Dialog(getContext(),R.style.MyDialogTheme);
            dialog.setContentView(R.layout.dialog_logout);
            Button yesbutton = dialog.findViewById(R.id.yesbutton);
            Button Nobutton = dialog.findViewById(R.id.nobutton);
            dialog.show();

            yesbutton.setOnClickListener(view22 -> {
                if (icd.isConnected()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    startActivity(new Intent(getContext(), MainActivity.class));
                    requireActivity().finish();
                    dialog.dismiss();

                }else{
                    new ShowCustomDialog(getContext());
                }

            });

            Nobutton.setOnClickListener(view2 -> dialog.dismiss());
        });

        ll_orderHistory.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), OrderActivity.class);
            startActivity(i);
            getActivity().finish();
        });


    }

}