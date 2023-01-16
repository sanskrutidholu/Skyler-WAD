package com.webandappdevelopment.skyler.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.adapter.SubCategoryAdapter;
import com.webandappdevelopment.skyler.modelclass.SubCategoryList;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchListActivity extends AppCompatActivity {

    String FETCH_SEARCH_URL = "https://www.app.skyler.co.in/appservice/search_list.php";
    private RecyclerView recyclerView;
    private SubCategoryAdapter adapter;
    private ArrayList<SubCategoryList> listItems = new ArrayList<>();
    String searchText;
    InternetConnectionDetector icd;
    EditText et_Search;
    ImageView search;
    TextView tvNoResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        findViewById(R.id.hide).setOnClickListener(this::closeKeyboard);

        findViewById(R.id.back).setOnClickListener(view -> {
            finish();
        });

        Intent i = getIntent();
        searchText = i.getStringExtra("searchText");

        icd = new InternetConnectionDetector(SearchListActivity.this);
        recyclerView = findViewById(R.id.search_rvSearchList);
        search = findViewById(R.id.iv_search);
        et_Search = findViewById(R.id.home_etSearch);

        et_Search.setText(searchText);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search.setOnClickListener(view -> {
            fetchServices(et_Search.getText().toString());
        });

        fetchServices(searchText);
        tvNoResult = findViewById(R.id.search_tvNoResult);
    }

    private void fetchServices(String searchText) {
        if (icd.isConnected()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, FETCH_SEARCH_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvNoResult.setVisibility(View.GONE);

                    try {
                        JSONArray array = new JSONArray(response);
                        listItems.clear();
                        for (int i=0; i< array.length(); i++){
                            JSONObject object = array.getJSONObject(i);

                            SubCategoryList subCategoryList = new SubCategoryList();
                            int id = object.getInt("subcategory_id");
                            String title = object.getString("subcategory_name");
                            String price = object.getString("service_charge");
                            String detail = object.getString("subcategory_detail");
                            String image = object.getString("subcategory_url");

                            subCategoryList.setId(id);
                            subCategoryList.setSubcategory_name(title);
                            subCategoryList.setService_charge(price);
                            subCategoryList.setSubcategory_detail(detail);
                            subCategoryList.setSubcategory_url(image);

                            listItems.add(subCategoryList);
                        }
                        adapter = new SubCategoryAdapter(SearchListActivity.this,listItems);
                        recyclerView.setAdapter(adapter);


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
                    parameters.put("searchText", searchText);
                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        } else {
            new ShowCustomDialog(this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchServices(String.valueOf(et_Search.getText()));
    }

    private void closeKeyboard(View view) {
        view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}