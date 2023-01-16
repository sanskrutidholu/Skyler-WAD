package com.webandappdevelopment.skyler.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.webandappdevelopment.skyler.activities.MyCartActivity;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.activities.SearchListActivity;
import com.webandappdevelopment.skyler.adapter.ServicesAdapter;
import com.webandappdevelopment.skyler.adapter.SliderAdapter;
import com.webandappdevelopment.skyler.modelclass.ServiceList;
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

public class HomeFragment extends Fragment {

    EditText et_search;
    private TextView location_tv,name_tv;
    private String location,name,email;
    private ImageView my_cart, search;
    private ViewPager vpSlider;
    private LinearLayout llSliderDots;
    private TextView[] tvDots;
    private SliderAdapter sliderAdapter;
    InternetConnectionDetector icd;
    String FETCH_SLIDER_URL = "https://www.app.skyler.co.in/appservice/fetch_slider.php";
    String FETCH_SERVICES_URL = "https://www.app.skyler.co.in/appservice/fetch_services.php";
    private List<SliderList> listItems;
    private int idArray[];
    private String imageArray[];
    int count;

    private ArrayList<ServiceList> categoryListArrayList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.hide).setOnClickListener(this::closeKeyboard);

        icd = new InternetConnectionDetector(getContext());
        vpSlider = view.findViewById(R.id.home_vpSlider);
        llSliderDots = view.findViewById(R.id.home_llSliderDots);
        recyclerView = view.findViewById(R.id.rv_categoryList);
        my_cart = view.findViewById(R.id.my_cart);
        location_tv = view.findViewById(R.id.tv_location);
        name_tv = view.findViewById(R.id.tv_username);
        et_search = view.findViewById(R.id.home_etSearch);
        search = view.findViewById(R.id.iv_search);

        search.setOnClickListener(view1->{
            Intent i = new Intent(getContext(), SearchListActivity.class);
            i.putExtra("searchText", et_search.getText().toString());
            startActivity(i);
            et_search.setText("");
        });


        my_cart.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), MyCartActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("STATUS", "NOT REGISTERED");
        location = sharedPreferences.getString("CITY",null);
        name = sharedPreferences.getString("NAME",null);
        email = sharedPreferences.getString("EMAIL",null);

        if (icd.isConnected()){
            location_tv.setText(location);
            name_tv.setText(name);

            loadSlider();
            addDots(0,count);
            vpSlider.addOnPageChangeListener(viewListner);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            loadServices();
        } else {
            new ShowCustomDialog(getContext());
        }

        return view;
    }

    private void loadServices() {
        StringRequest categoryrequest = new StringRequest(Request.Method.POST
                , FETCH_SERVICES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("","serviceResponse" + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    categoryListArrayList.clear();
                    for (int i=0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        ServiceList categoryList = new ServiceList();
                        String serviceName = jsonObject.getString("service_name");
                        String serviceImg = jsonObject.getString("service_img");
                        String serviceId = jsonObject.getString("service_id");

                        categoryList.setService_img(serviceImg);
                        categoryList.setService_id(serviceId);
                        categoryList.setService_name(serviceName);

                        categoryListArrayList.add(categoryList);
                    }
                    ServicesAdapter servicesAdapter = new ServicesAdapter(getContext(),categoryListArrayList);
                    recyclerView.setAdapter(servicesAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("city","bilaspur");
                param.put("state","chhattisgarh");
                return param;
            }
        };
        RequestQueue categoryqueue = Volley.newRequestQueue(requireContext());
        categoryqueue.getCache().clear();
        categoryqueue.add(categoryrequest);
    }

    public void addDots(int position, int count) {
        tvDots = new TextView[count];
        llSliderDots.removeAllViews();

        for(int i=0; i < tvDots.length; i++) {
            tvDots[i] = new TextView(getContext());
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
        RequestQueue requestQueueSlider = Volley.newRequestQueue(requireContext());
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

                    sliderAdapter = new SliderAdapter(getContext(),listItems);
                    vpSlider.setAdapter(sliderAdapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("status","slider error:", error);
                Toast.makeText(getContext(), error.toString() , Toast.LENGTH_SHORT).show();

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
    }

    private void closeKeyboard(View view) {
        view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}