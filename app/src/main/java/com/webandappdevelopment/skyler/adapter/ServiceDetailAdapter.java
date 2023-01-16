package com.webandappdevelopment.skyler.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.activities.CategoryDetailActivity;
import com.webandappdevelopment.skyler.modelclass.CartList;
import com.webandappdevelopment.skyler.utility.DBClass;

import java.util.List;

public class ServiceDetailAdapter extends RecyclerView.Adapter<ServiceDetailAdapter.ViewHolder>  {

    private List<CartList> cartItems;
    private Context context;
    private DBClass dbClass;

    public ServiceDetailAdapter(List<CartList> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbClass = new DBClass(context);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_servicedetail, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CartList cartItem = cartItems.get(position);
        String id = String.valueOf(cartItem.getId());
        String url = cartItem.getImg_url();
        String title = cartItem.getTitle();
        String price = cartItem.getPrice();

        if (url.equals("") || url == null){
            holder.itemImg.setBackgroundResource(R.drawable.ic_launcher_foreground);
        }else{
            Picasso.get().load(url).fit().into(holder.itemImg);
        }

        holder.tvItemName.setText(title);
        holder.tvItemPrice.setText("Rs. " + price);

        int charges = Integer.parseInt(price);

        holder.one.setOnClickListener(view-> {
            holder.one.setBackgroundResource(R.drawable.onserach_bg);
            holder.two.setBackgroundResource(R.drawable.search_bg_white);
            holder.three.setBackgroundResource(R.drawable.search_bg_white);
            dbClass.updateCartItem(cartItem.getId(),"1");
            int charge1 = charges * 1;
            dbClass.updateItemPrice(cartItem.getId(), String.valueOf(charge1));
        });

        holder.two.setOnClickListener(view-> {
            holder.two.setBackgroundResource(R.drawable.onserach_bg);
            holder.one.setBackgroundResource(R.drawable.search_bg_white);
            holder.three.setBackgroundResource(R.drawable.search_bg_white);
            dbClass.updateCartItem(cartItem.getId(),"2");
            int charge2 = charges * 2;
            dbClass.updateItemPrice(cartItem.getId(), String.valueOf(charge2));

        });

        holder.three.setOnClickListener(view-> {
            holder.three.setBackgroundResource(R.drawable.onserach_bg);
            holder.two.setBackgroundResource(R.drawable.search_bg_white);
            holder.one.setBackgroundResource(R.drawable.search_bg_white);
            dbClass.updateCartItem(cartItem.getId(),"3");
            int charge3 = charges * 3;
            dbClass.updateItemPrice(cartItem.getId(), String.valueOf(charge3));

        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemPrice, tvItemName;
        ImageView itemImg;
        Button one,two,three;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.cart_ivImage);
            tvItemName = itemView.findViewById(R.id.cart_tvTitle);
            tvItemPrice = itemView.findViewById(R.id.cart_tvPrice);
            one = itemView.findViewById(R.id.tv_one);
            two = itemView.findViewById(R.id.tv_two);
            three = itemView.findViewById(R.id.tv_three);
        }
    }
}

