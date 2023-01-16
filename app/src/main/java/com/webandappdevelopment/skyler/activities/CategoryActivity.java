package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.webandappdevelopment.skyler.adapter.CategoryListAdapeter2;
import com.webandappdevelopment.skyler.adapter.CategoryListAdapter;
import com.webandappdevelopment.skyler.adapter.SliderAdapter;
import com.webandappdevelopment.skyler.modelclass.CategoryList;
import com.webandappdevelopment.skyler.modelclass.SliderList;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    String  serviceName;
    TextView tv_serviceName;
    RecyclerView rv_categoryList;

    private ViewPager vpSlider;
    private LinearLayout llSliderDots;
    private TextView[] tvDots;
    private SliderAdapter sliderAdapter;
    private List<SliderList> listItems;
    private int idArray[];
    private String imageArray[];
    int count;
    ArrayList<CategoryList> categoryListArrayList;

    InternetConnectionDetector icd;
    String CATEGORY_API = "https://www.app.skyler.co.in/appservice/fetch_category.php";
    String FETCH_SLIDER_URL = "https://www.app.skyler.co.in/appservice/fetch_slider.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViewById(R.id.back).setOnClickListener(view -> {
            finish();
        });

        rv_categoryList = findViewById(R.id.rv_categoryList);
        tv_serviceName = findViewById(R.id.tv_serviceName);
        vpSlider = findViewById(R.id.home_vpSlider);
        llSliderDots = findViewById(R.id.home_llSliderDots);

        icd = new InternetConnectionDetector(this);
        serviceName = getIntent().getStringExtra("serviceName");
        tv_serviceName.setText(serviceName);

        if (icd.isConnected()){
            loadSlider();
            addDots(0,count);
            vpSlider.addOnPageChangeListener(viewListner);

            rv_categoryList.setHasFixedSize(true);
            rv_categoryList.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
            loadCategory();
        } else {
            new ShowCustomDialog(CategoryActivity.this);
        }

    }

    public void addDots(int position, int count) {
        tvDots = new TextView[count];
        llSliderDots.removeAllViews();

        for(int i=0; i < tvDots.length; i++) {
            tvDots[i] = new TextView(this);
            tvDots[i].setText(Html.fromHtml("&#8226;"));
            tvDots[i].setTextSize(20);
            tvDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            llSliderDots.addView(tvDots[i]);
        }

        if (tvDots.length > 0) {
            tvDots[position].setTextColor(Color.WHITE);
        }
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDots(position, count);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void loadSlider(){
        if (icd.isConnected()){
            RequestQueue requestQueueSlider = Volley.newRequestQueue(this);
            requestQueueSlider.getCache().clear();

            StringRequest stringRequestSlider = new StringRequest(Request.Method.POST, FETCH_SLIDER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("onresponse :", response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        count = jsonArray.length();
                        idArray = new int[jsonArray.length()];
                        imageArray = new String[jsonArray.length()];

                        for (int i=0; i< jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            idArray[i] = jsonObject.getInt("slider_id");
                            imageArray[i] = jsonObject.getString("slider_url");
                        }

                        listItems = new ArrayList<>();

                        for (int x=0; x<jsonArray.length(); x++){
                            SliderList item = new SliderList(idArray[x],imageArray[x]);
                            listItems.add(item);
                        }

                        sliderAdapter = new SliderAdapter(CategoryActivity.this,listItems);
                        vpSlider.setAdapter(sliderAdapter);

                    }catch (JSONException e){
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
                    Map<String,String> parameters = new HashMap<>();
                    parameters.put("city","Bilaspur");
                    parameters.put("state","Chhattisgarh");
                    Log.d("para", parameters.toString());
                    return parameters;
                }
            };
            requestQueueSlider.add(stringRequestSlider);
        } else {
            new ShowCustomDialog(CategoryActivity.this);        }
    }

    private void loadCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CATEGORY_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onRespone",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    categoryListArrayList = new ArrayList<>();

                    for (int i = 0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CategoryList categoryList = new CategoryList();

                        String serviceName = jsonObject.getString("category_name");
                        String serviceImg = jsonObject.getString("category_url");
                        categoryList.setCategory_name(serviceName);
                        categoryList.setCategory_url(serviceImg);

                        categoryListArrayList.add(categoryList);
                    }

                    CategoryListAdapeter2 categoryListAdapter = new CategoryListAdapeter2(CategoryActivity.this,categoryListArrayList);
                    rv_categoryList.setAdapter(categoryListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("service",serviceName);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CategoryActivity.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadSlider();
        loadCategory();
    }
}