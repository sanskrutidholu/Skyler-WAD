package com.webandappdevelopment.skyler.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webandappdevelopment.skyler.activities.CategoryActivity;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.modelclass.CategoryList;
import com.webandappdevelopment.skyler.modelclass.ServiceList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    Context context;
    ArrayList<ServiceList> serviceListArrayList;

    String CATEGORY_API = "https://www.app.skyler.co.in/appservice/fetch_category.php";

    public ServicesAdapter(Context context, ArrayList<ServiceList> serviceListArrayList){
        this.context = context;
        this.serviceListArrayList = serviceListArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_servicelist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServiceList categoryList = serviceListArrayList.get(position);
        String serviceName = categoryList.getService_name();
        holder.categoryName.setText(serviceName);

        holder.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        loadCategory(holder.recyclerView,serviceName);

        holder.viewAll.setOnClickListener(view -> {
            Intent i = new Intent(context,CategoryActivity.class);
            i.putExtra("serviceName",serviceName);
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return serviceListArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        RecyclerView recyclerView;
        TextView viewAll, categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_categoryList);
            viewAll = itemView.findViewById(R.id.tv_viewAll);
            categoryName = itemView.findViewById(R.id.tv_category);
        }

    }

    public void loadCategory(RecyclerView rv, String serviceName){
        RequestQueue categoryqueue = Volley.newRequestQueue(context);
        categoryqueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CATEGORY_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onRespone",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<CategoryList> categoryListArrayList = new ArrayList<>();

                    for (int i = 0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CategoryList categoryList = new CategoryList();

                        String serviceName = jsonObject.getString("category_name");
                        String serviceImg = jsonObject.getString("category_url");
                        categoryList.setCategory_name(serviceName);
                        categoryList.setCategory_url(serviceImg);

                        categoryListArrayList.add(categoryList);
                    }

                    CategoryListAdapter categoryListAdapter = new CategoryListAdapter(context,categoryListArrayList);
                    rv.setAdapter(categoryListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("service",serviceName);
                return param;
            }
        };
        categoryqueue.add(stringRequest);
    }


}

