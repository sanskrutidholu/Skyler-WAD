package com.webandappdevelopment.skyler.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    InternetConnectionDetector icd;
    String CHECK_USER_URL = "https://www.app.skyler.co.in/appservice/check_user.php";
    String appId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        icd = new InternetConnectionDetector(this);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               checkUser();
           }
       },4000);
    }

    private void checkUser() {
        if (icd.isConnected()){
            SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
            String result = sharedPreferences.getString("STATUS", "NOT REGISTERED");

            if (result.matches("Already Registered")) {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
            else{
                directLogin();
            }
        } else{
            new ShowCustomDialog(this);
        }

    }

    private void directLogin() {
        if (icd.isConnected()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_USER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("","directResponse"+response);
                        JSONArray jsonObject = new JSONArray(response);
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject(i);
                            String appId = jsonObject1.getString("app_id");
                            SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("APP_ID", appId);
                            editor.putString("CITY", "Bilaspur");
                            editor.putString("STATE", "Chhattisgarh");
                            editor.apply();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();

                            Log.e("","skip"+appId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters = new HashMap<>();
                    parameters.put("appId", "0");
                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);

        } else{
            new ShowCustomDialog(this);
        }

    }
}