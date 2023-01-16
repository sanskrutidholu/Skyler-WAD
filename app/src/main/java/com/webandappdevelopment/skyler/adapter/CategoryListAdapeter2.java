package com.webandappdevelopment.skyler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.activities.SubCategoryActivity;
import com.webandappdevelopment.skyler.modelclass.CategoryList;

import java.util.ArrayList;

public class CategoryListAdapeter2 extends RecyclerView.Adapter<CategoryListAdapeter2.ViewHolder> {

    Context context;
    ArrayList<CategoryList> serviceListArrayList;

    public CategoryListAdapeter2(Context context, ArrayList<CategoryList> serviceListArrayList){
        this.context = context;
        this.serviceListArrayList = serviceListArrayList;
    }

    @NonNull
    @Override
    public CategoryListAdapeter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categorylist2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapeter2.ViewHolder holder, int position) {
        CategoryList categoryList = serviceListArrayList.get(position);

        String categoryImage = categoryList.getCategory_url();
        String categoryName = categoryList.getCategory_name();
        holder.tv_categoryName.setText(categoryName);

        if (categoryImage.equals("")){
            Picasso.get().load(R.drawable.skylersplash).fit().into(holder.iv_categoryImg);
        } else {
            Picasso.get().load(categoryList.getCategory_url()).fit().into(holder.iv_categoryImg);
        }

        holder.categoryDetails.setOnClickListener(view -> {
            Intent i = new Intent(context, SubCategoryActivity.class);
            i.putExtra("cName",categoryName);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return serviceListArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_categoryImg;
        TextView tv_categoryName;
        RelativeLayout categoryDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_categoryImg = itemView.findViewById(R.id.cr_ivImage);
            tv_categoryName = itemView.findViewById(R.id.cr_tvTitle);
            categoryDetails = itemView.findViewById(R.id.rl_categoryDetails);

        }
    }
}
