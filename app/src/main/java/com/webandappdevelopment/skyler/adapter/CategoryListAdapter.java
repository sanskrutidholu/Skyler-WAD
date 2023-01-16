package com.webandappdevelopment.skyler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.activities.CategoryActivity;
import com.webandappdevelopment.skyler.activities.SubCategoryActivity;
import com.webandappdevelopment.skyler.modelclass.CategoryList;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    Context context;
    ArrayList<CategoryList> serviceListArrayList;

    public CategoryListAdapter(Context context, ArrayList<CategoryList> serviceListArrayList){
        this.context = context;
        this.serviceListArrayList = serviceListArrayList;
    }

    @NonNull
    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categorylist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.ViewHolder holder, int position) {
        CategoryList categoryList = serviceListArrayList.get(position);
        String categoryImage = categoryList.getCategory_url();
        String categoryName = categoryList.getCategory_name();
        holder.tv_categoryName.setText(categoryName);

        if (categoryImage.equals("")){
            Picasso.get().load(R.drawable.skylersplash).fit().into(holder.iv_categoryImg);
        } else {
            Picasso.get().load(categoryList.getCategory_url()).fit().into(holder.iv_categoryImg);
        }

        holder.categoryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SubCategoryActivity.class);
                i.putExtra("cName",categoryName);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceListArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_categoryImg;
        TextView tv_categoryName;
        LinearLayout categoryDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryDetails = itemView.findViewById(R.id.ll_categoryDetails);
            iv_categoryImg = itemView.findViewById(R.id.iv_categoryImg);
            tv_categoryName = itemView.findViewById(R.id.tv_categoryName);

        }
    }
}
