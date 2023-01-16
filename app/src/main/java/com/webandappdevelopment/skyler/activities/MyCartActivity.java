package com.webandappdevelopment.skyler.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
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
import com.webandappdevelopment.skyler.adapter.MyCartAdapter;
import com.webandappdevelopment.skyler.modelclass.CartList;
import com.webandappdevelopment.skyler.utility.CallToast;
import com.webandappdevelopment.skyler.utility.DBClass;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCartActivity extends AppCompatActivity {

    EditText email, password, enter_name, enter_email, enter_password, enter_location;
    Button btn_serviceDetails;
    Button loginbtn, regisbtn, signin, signup;
    String in_mail, in_password, up_mail, up_name, up_password, up_location;
    RecyclerView rv_cart;
    DBClass dbClass;
    List<CartList> cartItems = new ArrayList<>();
    MyCartAdapter cartAdapter;
    int totalCharge = 0;
    ImageView ivEmpty;
    TableLayout tlTableCart;
    LinearLayout llTotal;
    InternetConnectionDetector icd;
    FrameLayout register_fl;
    ImageView registerback;

    String appId, result;

    CartList cartItem ;
    Spinner sp_location;
    String selectedCity;
    ArrayList<String> StringCity = new ArrayList<>();
    ArrayList<String> IDCity = new ArrayList<>();
    String REGISTRATION_URL = "https://www.app.skyler.co.in/appservice/registration.php";
    String GET_CITY = "https://www.app.skyler.co.in/appservice/fetch_city.php";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        findViewById(R.id.back).setOnClickListener(view -> {
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        });

        // for registration layout
        register_fl = findViewById(R.id.signup_fl);
        signup = findViewById(R.id.btn_registration);
        registerback = findViewById(R.id.iv_back1);
        enter_name = findViewById(R.id.r_name);
        enter_email = findViewById(R.id.r_mail);
        enter_password = findViewById(R.id.r_password);
        sp_location = findViewById(R.id.sp_location);
        btn_serviceDetails = findViewById(R.id.btn_placeorder);
        rv_cart = findViewById(R.id.recycler_cart);
        ivEmpty = findViewById(R.id.order_ivEmpty);
        tlTableCart = findViewById(R.id.order_tlCartTable);
        llTotal = findViewById(R.id.layout_total);

        dbClass = new DBClass(MyCartActivity.this);
        icd = new InternetConnectionDetector(this);

        SharedPreferences userPreference = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        result = userPreference.getString("STATUS", "NOT REGISTERED");
        appId = userPreference.getString("APP_ID", null);

        SharedPreferences sharedPreferences = getSharedPreferences("ORDER_DETAILS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("APP_ID", appId);
        editor.apply();

        btn_serviceDetails.setOnClickListener(view -> {

            if (result.matches("Already Registered")) {
                if (dbClass.isEmpty()){
                    new CallToast(MyCartActivity.this,"No Item Added");

                }else{
                    Intent i = new Intent(this, CartService_DetailsActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            else {
                Dialog dialog = new Dialog(this,R.style.MyDialogTheme);
                dialog.setContentView(R.layout.dialog_login);
                Button yesbutton = dialog.findViewById(R.id.yesbutton);
                dialog.show();

                yesbutton.setOnClickListener(view22 -> {
                    register_fl.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    getCity();

                    registerback.setOnClickListener(view1 -> {
                        register_fl.setVisibility(View.GONE);
                    });

                    signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String mobilePattern = "[6-9][0-9]{9}";
                            up_mail = enter_email.getText().toString();
                            up_name = enter_name.getText().toString();
                            up_password = enter_password.getText().toString();
                            if (up_name.isEmpty() || up_mail.isEmpty() || up_password.isEmpty() || selectedCity.isEmpty()){
                                new CallToast(MyCartActivity.this,"All values required");
                            }else if (!(Patterns.PHONE.matcher(up_mail).matches()) || !(up_mail.matches(mobilePattern))){
                                new CallToast(MyCartActivity.this,"Invalid Mobile Number");
                            }else{
                                onRegister(up_name,up_mail,"",up_password,selectedCity,appId);
                            }
                        }
                    });
                });
            }
        });

        if(icd.isConnected()) {
            rv_cart.setHasFixedSize(true);
            rv_cart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
            loadCart();
        }else{
            new ShowCustomDialog(this);
        }
    }

    private void loadCart() {
        if (dbClass.isEmpty()){
            new CallToast(MyCartActivity.this,"No Items Added");
        } else {
            if (cartItems != null){ cartItems.clear(); }
            SQLiteDatabase db = dbClass.getReadableDatabase();
            Cursor cursor = dbClass.selectCartItem();
            try {
                while (cursor.moveToNext()){
                    Log.d("inside:", "while");
                    @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbClass.KEY_ID)));
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_NAME));
                    @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_PRICE));
                    @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_IMAGE));
                    @SuppressLint("Range") String detail = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_DETAILS));
                    totalCharge = totalCharge + Integer.parseInt(price);
                    cartItem = new CartList(id,title,price,image,detail);
                    cartItems.add(cartItem);
                }

            } finally {
                if (cursor!=null && cursor.isClosed()){
                    cursor.close();
                }
                db.close();
            }

            cartAdapter = new MyCartAdapter(cartItems, MyCartActivity.this);
            rv_cart.setAdapter(cartAdapter);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadCart();
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

                        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(MyCartActivity.this,R.layout.support_simple_spinner_dropdown_item,StringCity);
                        sp_location.setAdapter(spnAdapter);
                        sp_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void onRegister(String name,String mobile, String email, String password, String city, String appId){
        if (icd.isConnected()){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTRATION_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("","myResponse"+response);
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

                            register_fl.setVisibility(View.GONE);

                            if (dbClass.isEmpty()){
                                Toast.makeText(MyCartActivity.this,"No Item Added",Toast.LENGTH_SHORT).show();
                            }else{
                                startActivity(new Intent(MyCartActivity.this, CartService_DetailsActivity.class));
                                finish();
                            }

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
                    parameters.put("email", "email");
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

}