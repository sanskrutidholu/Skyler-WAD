package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.webandappdevelopment.skyler.fragments.AccountFragment;
import com.webandappdevelopment.skyler.fragments.HomeFragment;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.fragments.SupportFragment;

public class HomeActivity extends AppCompatActivity {
        BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        replacefragment(new HomeFragment());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home:
                    replacefragment(new HomeFragment());
                    break;
                case R.id.account:
                    replacefragment(new AccountFragment());
                    break;
                case R.id.support:
                    replacefragment(new SupportFragment());
                    break;
            }
            return true;
        });
    }

    private void replacefragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
}