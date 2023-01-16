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
import android.widget.EditText;
import android.widget.ImageView;
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

public class EditProfileActivity extends AppCompatActivity {

    EditText et_name, et_mail, et_mobile;
    Spinner sp_location;
    TextView tv_proceed;
      ImageView btn_back;
    String s_name, s_mail = "", s_mobile, s_location, appId;

    InternetConnectionDetector icd;
    ArrayList<String> StringCity = new ArrayList<>();
    ArrayList<String> IDCity = new ArrayList<>();
    String GET_CITY = "https://www.app.skyler.co.in/appservice/fetch_city.php";
    String EDIT_PROFILE ="https://www.app.skyler.co.in/appservice/edit_profile.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViewById(R.id.hide).setOnClickListener(this::closeKeyboard);

        et_name = findViewById(R.id.tv_username);
        et_mail = findViewById(R.id.tv_email);
        et_mobile = findViewById(R.id.tv_mobile);
        sp_location = findViewById(R.id.sp_location);
        tv_proceed = findViewById(R.id.tv_proceed);
        btn_back = findViewById(R.id.back);
        icd = new InternetConnectionDetector(this);

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        s_location = sharedPreferences.getString("CITY",null);
        s_name = sharedPreferences.getString("NAME",null);
        s_mail = sharedPreferences.getString("EMAIL",null);
        s_mobile = sharedPreferences.getString("MOBILE",null);
        appId = sharedPreferences.getString("APP_ID",null);

        et_name.setText(s_name);
        et_mail.setText(s_mail);
        et_mobile.setText(s_mobile);

        btn_back.setOnClickListener(view -> {
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        });

        getCity();

        tv_proceed.setOnClickListener(view -> {
            String mobilePattern = "[6-9][0-9]{9}";
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            s_name = et_name.getText().toString();
            s_mail = et_mail.getText().toString();
            s_mobile = et_mobile.getText().toString();

            if (s_name.isEmpty() || s_mail.isEmpty() || s_mobile.isEmpty()){
                new CallToast(this,"Please enter all value..");
            }else if (!(Patterns.PHONE.matcher(s_mobile).matches()) || !(s_mobile.matches(mobilePattern))){
                new CallToast(this,"Invalid Mobile Number");
            } else if (!(Patterns.EMAIL_ADDRESS.matcher(s_mail).matches()) || !(s_mail.matches(emailPattern))){
                new CallToast(this,"Invalid Email Address");
            } else{
                editProfile(appId,s_name,s_mail,s_mobile,s_location);
            }
        });
    }

    private void editProfile(String appId, String s_name, String s_mail, String s_mobile, String s_location) {
        if (icd.isConnected()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_PROFILE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("","profileRespone"+response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.names().get(0).equals("success")){

                            SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("NAME",s_name);
                            editor.putString("MOBILE",s_mobile);
                            editor.putString("EMAIL",s_mail);
                            editor.putString("CITY",s_location);
                            editor.putString("APP_ID", appId);
                            editor.apply();

                            new CallToast(EditProfileActivity.this,"Profile updated successfully");
                            startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
                            finish();

                        } else if (jsonObject.names().get(0).equals("fail") || jsonObject.names().get(0).equals("error")) {
                            new CallToast(EditProfileActivity.this,"Something went wrong. Please try again");                        }
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
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("appId",appId);
                    parameters.put("name",s_name);
                    parameters.put("email",s_mail);
                    parameters.put("mobile",s_mobile);
                    parameters.put("city",s_location);

                    Log.e("","parameters"+parameters);
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

    public void getCity() {
        if (icd.isConnected()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CITY, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("","city"+response);
                    try {
                        IDCity.clear();
                        StringCity.clear();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i< jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("city_id");
                            String cityName = jsonObject.getString("city_name");

                            IDCity.add(id);
                            StringCity.add(cityName);
                        }

                        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(EditProfileActivity.this,R.layout.support_simple_spinner_dropdown_item,StringCity);
                        sp_location.setAdapter(spnAdapter);
                        sp_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                s_location = StringCity.get(i);
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

    private void closeKeyboard(View view) {
         view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}