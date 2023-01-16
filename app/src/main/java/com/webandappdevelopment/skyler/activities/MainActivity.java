package com.webandappdevelopment.skyler.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.utility.CallToast;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView skiptv, forgetPassword;
    EditText email, password, enter_name, enter_email, enter_password;
    Spinner enter_location;
    Button loginbtn, regisbtn, signin, signup;
    ImageView loginback, registerback;
    String in_mail, in_password, up_mail, up_name, up_password;
    FrameLayout login_fl, register_fl;
    RelativeLayout splashbackground;
    InternetConnectionDetector icd;
    String appId;

    String selectedCity;
    ArrayList<String> StringCity = new ArrayList<>();
    ArrayList<String> IDCity = new ArrayList<>();

    String LOGIN_URL = "https://www.app.skyler.co.in/appservice/login.php";
    String REGISTRATION_URL = "https://www.app.skyler.co.in/appservice/registration.php";
    String GET_CITY = "https://www.app.skyler.co.in/appservice/fetch_city.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.hide).setOnClickListener(this::closeKeyboard);

        icd = new InternetConnectionDetector(this);

        skiptv = findViewById(R.id.skipbtn);
        loginbtn = findViewById(R.id.btn_signin);
        regisbtn = findViewById(R.id.btn_signup);
        splashbackground = findViewById(R.id.hide);

        // for login layout
        login_fl = findViewById(R.id.signin_fl);
        signin = findViewById(R.id.btn_login);
        loginback = findViewById(R.id.iv_back);
        email = findViewById(R.id.l_mail);
        password = findViewById(R.id.l_password);
        forgetPassword = findViewById(R.id.forgetPassword);

        // for registration layout
        register_fl = findViewById(R.id.signup_fl);
        signup = findViewById(R.id.btn_registration);
        registerback = findViewById(R.id.iv_back1);
        enter_name = findViewById(R.id.r_name);
        enter_email = findViewById(R.id.r_mail);
        enter_password = findViewById(R.id.r_password);
        enter_location = findViewById(R.id.sp_location);

        skiptv.setOnClickListener(view -> {
            Intent i =new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        });

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
        appId = sharedPreferences.getString("APP_ID",null);

        loginbtn.setOnClickListener(view -> {
            login_fl.setVisibility(View.VISIBLE);
            register_fl.setVisibility(View.GONE);
            splashbackground.setBackgroundResource(R.color.grey);

            forgetPassword.setOnClickListener(view1 ->{
                Intent i = new Intent(this,ForgetPasswordActivity.class);
                startActivity(i);
                finish();
            });

            loginback.setOnClickListener(view1 -> {
                login_fl.setVisibility(View.GONE);
                splashbackground.setBackgroundResource(R.color.white);
            });

            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mobilePattern = "[6-9][0-9]{9}";
                    in_mail = email.getText().toString();
                    in_password = password.getText().toString();
                    if (in_mail.isEmpty() || in_password.isEmpty()){
                        new CallToast(MainActivity.this,"All values required");
                    }else if (!(Patterns.PHONE.matcher(in_mail).matches()) || !(in_mail.matches(mobilePattern))){
                        new CallToast(MainActivity.this,"Invalid Mobile Number");

                    } else{
                        onLogin(in_mail,in_password);
                    }
                }
            });
        });

        regisbtn.setOnClickListener(view -> {
            register_fl.setVisibility(View.VISIBLE);
            login_fl.setVisibility(View.GONE);
            splashbackground.setBackgroundResource(R.color.grey);

            registerback.setOnClickListener(view1 -> {
                register_fl.setVisibility(View.GONE);
                splashbackground.setBackgroundResource(R.color.white);
            });

            getCity();

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mobilePattern = "[6-9][0-9]{9}";
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    up_mail = enter_email.getText().toString();
                    up_name = enter_name.getText().toString();
                    up_password = enter_password.getText().toString();
                    if (up_name.isEmpty() || up_mail.isEmpty() || up_password.isEmpty() || selectedCity.isEmpty()){
                        new CallToast(MainActivity.this,"All values required");
                    }else if (!(Patterns.PHONE.matcher(up_mail).matches()) || !(up_mail.matches(mobilePattern))){
                        new CallToast(MainActivity.this,"Invalid Mobile Number");
                    } else{
                        onRegister(up_name,up_mail,"",up_password,selectedCity,appId);
                    }
                }
            });
        });

    }

    public void getCity() {
        if (icd.isConnected()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CITY, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("","city"+response);
                    try {
                        IDCity.clear();
                        StringCity.clear();
                        IDCity.add("");
                        StringCity.add("Select City");
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i< jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("city_id");
                            String cityName = jsonObject.getString("city_name");

                            IDCity.add(id);
                            StringCity.add(cityName);
                        }

                        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,StringCity);
                        enter_location.setAdapter(spnAdapter);
                        enter_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedCity = StringCity.get(i);
                                Log.e("","selectedCity"+selectedCity);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }
    }

    public void onLogin(String s1, String s2){
        if (icd.isConnected()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("","loginResponse"+response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.names().get(0).equals("user_id")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("NAME", jsonObject.getString("user_name"));
                            editor.putString("MOBILE", jsonObject.getString("user_mobile"));
                            editor.putString("EMAIL", jsonObject.getString("user_email"));
                            editor.putString("CITY", jsonObject.getString("user_city"));
                            editor.putString("STATUS", "Already Registered");
                            editor.putString("APP_ID", jsonObject.getString("app_id"));
                            editor.apply();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        } else if (jsonObject.names().get(0).equals("fail")) {
                            new CallToast(MainActivity.this,"Password you have entered is incorrect");

                        } else if (jsonObject.names().get(0).equals("error")) {
                            new CallToast(MainActivity.this,"OOPs!! No user found with given details.");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters = new HashMap<>();
                    parameters.put("mobile", s1);
                    parameters.put("pswd", s2);
                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }else{
            new ShowCustomDialog(this);
        }

    }

    public void onRegister(String name, String mobile, String email, String password, String city, String appId){
        if (icd.isConnected()){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTRATION_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("","registerResponse"+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.names().get(0).equals("error")) {
                            Toast.makeText(getApplicationContext(), "Please enter all details.", Toast.LENGTH_LONG).show();
                        } else if (jsonObject.names().get(0).equals("fail")) {
                            Toast.makeText(getApplicationContext(), "Server not responding. Please try again.", Toast.LENGTH_LONG).show();
                        } else if (jsonObject.names().get(0).equals("success")) {
                            Toast.makeText(getApplicationContext(), "You are successfully registered with us", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("STATUS", "Already Registered");
                            editor.putString("NAME",name);
                            editor.putString("MOBILE",mobile);
                            editor.putString("EMAIL",email);
                            editor.putString("PASSWORD",password);
                            editor.putString("CITY",city);
                            editor.putString("APP_ID", appId);
                            editor.apply();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
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
                    parameters.put("name", name);
                    parameters.put("mobile", mobile);
                    parameters.put("pswd", password);
                    parameters.put("city", city);
                    parameters.put("appId", appId);
                    Log.d("Parameters:", parameters.toString());
                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }else{
            new ShowCustomDialog(this);
        }

    }

    public void callToast(String msg){
        Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_LONG).show();
    }

    private void closeKeyboard(View view) {
        view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}