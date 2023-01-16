package com.webandappdevelopment.skyler.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.adapter.OrderListAdapter;
import com.webandappdevelopment.skyler.modelclass.OrderItem;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity  {
    RecyclerView rv_orderList;

    ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();
    InternetConnectionDetector icd;
    String appId;
    String FETCH_ORDER_URL = "https://www.app.skyler.co.in/appservice/fetch_order.php";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        findViewById(R.id.back).setOnClickListener(view -> {
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        });

        rv_orderList = findViewById(R.id.rv_orderHistory);
        icd = new InternetConnectionDetector(this);

        if (icd.isConnected()){
            SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
            appId = sharedPreferences.getString("APP_ID", "O");

            rv_orderList.setHasFixedSize(true);
            rv_orderList.setLayoutManager(new LinearLayoutManager(this));
            loadOrderHistory(appId);
        }else{
            new ShowCustomDialog(this);
        }

    }

    private void loadOrderHistory(String appId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FETCH_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("","orderResponse"+response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    orderItemArrayList.clear();
                    for (int i=0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        OrderItem orderItem = new OrderItem();

                        orderItem.setId(jsonObject.getString("id"));
                        orderItem.setService_name(jsonObject.getString("service_name"));
                        orderItem.setService_details(jsonObject.getString("service_details"));
                        orderItem.setService_price(jsonObject.getString("service_price"));
                        orderItem.setPreferred_date(jsonObject.getString("preferred_date"));
                        orderItem.setPreferred_time(jsonObject.getString("preferred_time"));
                        orderItem.setSchedule_date(jsonObject.getString("schedule_date"));
                        orderItem.setSubmit_date(jsonObject.getString("submit_date"));
                        orderItem.setFull_name(jsonObject.getString("full_name"));
                        orderItem.setPhone_no(jsonObject.getString("phone_no"));
                        orderItem.setAddress(jsonObject.getString("address"));
                        orderItem.setEmail(jsonObject.getString("email"));
                        orderItem.setOrder_status(jsonObject.getString("order_status"));
                        orderItem.setPayment_mode(jsonObject.getString("payment_mode"));
                        orderItem.setAlloted_person(jsonObject.getString("alloted_person"));

                        orderItemArrayList.add(orderItem);

                    }

                    OrderListAdapter orderListAdapter = new OrderListAdapter(OrderActivity.this,orderItemArrayList);
                    rv_orderList.setAdapter(orderListAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
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
                Map<String, String> map = new HashMap<>();
                map.put("appId",appId);

                Log.e("","orderParameters"+map);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadOrderHistory(appId);
    }
}