package com.webandappdevelopment.skyler.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.webandappdevelopment.skyler.utility.DBClass;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartService_PaymentActivity extends AppCompatActivity {

    ImageView back;
    Button next;
    RadioGroup rg;
    RadioButton rb_upi, rb_cash;
    TextView tv_price, tv_charges, tv_totalPrice, tv_total;

    String paymentOption;
    int totalCharge = 0;
    String PLACE_ORDER ="https://www.app.skyler.co.in/appservice/place_order.php";
    String CONFIRM_ORDER = "https://www.app.skyler.co.in/appservice/confirm_order.php";
    SharedPreferences orderPreference;
    SharedPreferences.Editor orderEditor;

    private DBClass dbClass;
    InternetConnectionDetector icd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_service_payment);

        back = findViewById(R.id.back);
        next = findViewById(R.id.btn_next);
        rg = findViewById(R.id.rg);
        rb_upi = findViewById(R.id.rb_upi);
        rb_cash = findViewById(R.id.rb_cash);
        tv_price = findViewById(R.id.tv_price);
        tv_charges = findViewById(R.id.tv_charges);
        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        tv_total = findViewById(R.id.tv_total);

        back.setOnClickListener(view -> {
            startActivity(new Intent(this, CartService_AddressActivity.class));
            finish();
        });

        dbClass = new DBClass(this);
        icd  = new InternetConnectionDetector(this);

        getTotalPrice();

        next.setOnClickListener(view ->  {
            if(rb_cash.isChecked()){
                paymentOption = rb_cash.getText().toString();
            }else{
                paymentOption = rb_upi.getText().toString();
            }

            orderPreference = getSharedPreferences("ORDER_DETAILS", MODE_PRIVATE);
            orderEditor = orderPreference.edit();
            orderEditor.putString("PAYMENT_OPTION",paymentOption);
            orderEditor.apply();

            getInsertedId();
        });
    }

    private void getTotalPrice() {
        SQLiteDatabase db = dbClass.getReadableDatabase();
        Cursor cursor = dbClass.selectCartItem();
        try {
            while (cursor.moveToNext()){
                Log.d("inside:", "while");
                @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_NEW_PRICE));
                totalCharge = totalCharge + Integer.parseInt(price);
            }
            tv_totalPrice.setText("Rs." + String.valueOf(totalCharge));
            tv_price.setText(String.valueOf(totalCharge));
            tv_total.setText(String.valueOf(totalCharge));
            tv_charges.setText("0");
        } finally {
            if (cursor!=null && cursor.isClosed()){
                cursor.close();
            }
            db.close();
        }

    }

    private void getInsertedId() {
        orderPreference = getSharedPreferences("ORDER_DETAILS", MODE_PRIVATE);
        String appId = orderPreference.getString("APP_ID",null);
        String name = orderPreference.getString("O_NAME",null);
        String mobile = orderPreference.getString("O_CONTACT_1",null);
        String pincode = orderPreference.getString("O_PINCODE",null);
        String state = orderPreference.getString("O_STATE",null);
        String city = orderPreference.getString("O_CITY",null);
        String house = orderPreference.getString("O_HOUSE",null);
        String area = orderPreference.getString("O_AREA",null);
        String paymentMode = orderPreference.getString("PAYMENT_OPTION",null);
        String date = orderPreference.getString("DATE",null);
        String time = orderPreference.getString("TIME",null);
        String address = pincode + ", " + state+ ", " + city + ", " + house + ", " + area;

        if (icd.isConnected()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, PLACE_ORDER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("","orderResponse"+response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.names().get(0).equals("success")) {
                            String stInsertedId = jsonObject.getString("success");
                            confirmOrder(stInsertedId,date,time);
                        } else if (jsonObject.names().get(0).equals("fail") || jsonObject.names().get(0).equals("error")) {
                            new CallToast(CartService_PaymentActivity.this,"Something went wrong. Please try again.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        new CallToast(CartService_PaymentActivity.this,"Server not responding. Please try again.");
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("appId",appId);
                    map.put("name",name);
                    map.put("mobile",mobile);
                    map.put("address",address);
                    map.put("paymentMode",paymentMode);

                    Log.e("","appIdOnPlaceOrder"+appId);

                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);

        }else{
            new ShowCustomDialog(this);
        }

    }

    private void confirmOrder(String stInsertedId, String date, String time) {
        SQLiteDatabase db = dbClass.getReadableDatabase();
        Cursor cursor = dbClass.selectCartItem();
        try {
            while (cursor.moveToNext()){
                @SuppressLint("Range") final String service = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_NAME));
                @SuppressLint("Range") final String price = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_NEW_PRICE));
                @SuppressLint("Range") final String details = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_COUNT));
                totalCharge = totalCharge + Integer.parseInt(price);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, CONFIRM_ORDER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("","confirmOrder"+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                new CallToast(CartService_PaymentActivity.this,"Your order has been placed.");
                                dbClass.emptyCartItem();

                                SharedPreferences.Editor editor = orderPreference.edit();
                                editor.clear();
                                editor.apply();

                            } else if (jsonObject.names().get(0).equals("fail") || jsonObject.names().get(0).equals("error")) {
                                new CallToast(CartService_PaymentActivity.this,"Something went wrong. Please try again.");
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
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("insertedId",stInsertedId);
                        map.put("service",service);
                        map.put("details",details);
                        map.put("preferredDate",date);
                        map.put("preferredTime",time);
                        map.put("price",price);

                        Log.e("","parameters"+map);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.getCache().clear();
                requestQueue.add(stringRequest);
            }
            startActivity(new Intent(CartService_PaymentActivity.this, OrderActivity.class));
            finish();
        } finally {
            if (cursor!=null && cursor.isClosed()){
                cursor.close();
            }
            db.close();
        }
    }
}