package com.webandappdevelopment.skyler.adapter;

import android.content.Context;
import android.content.Intent;
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

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder>  {

    private List<CartList> cartItems;
    private Context context;
    private DBClass dbClass;

    public MyCartAdapter(List<CartList> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbClass = new DBClass(context);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mycart, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CartList cartItem = cartItems.get(position);
        String url = cartItem.getImg_url();

        if (url.equals("") || url == null){
            holder.itemImg.setBackgroundResource(R.drawable.ic_launcher_foreground);
        }else{
            Picasso.get().load(url).fit().into(holder.itemImg);
        }

        holder.tvItemName.setText(cartItem.getTitle());
        holder.tvItemPrice.setText("Rs. " + cartItem.getPrice());

        holder.btnRemove.setOnClickListener(v -> {
            dbClass.removeCartItem(cartItem.getId());
            removeItem(position);
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CategoryDetailActivity.class);
                i.putExtra("scName", cartItem.getTitle());
                i.putExtra("scImage",cartItem.getImg_url());
                i.putExtra("scCharge",cartItem.getPrice());
                i.putExtra("scDetail",cartItem.getDetails());
                i.putExtra("scId",cartItem.getId());
                i.putExtra("code","0");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemPrice, tvItemName;
        ImageView itemImg;
        Button btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.cart_ivImage);
            tvItemName = itemView.findViewById(R.id.cart_tvTitle);
            tvItemPrice = itemView.findViewById(R.id.cart_tvPrice);
            btnRemove = itemView.findViewById(R.id.cart_btnRemove);

        }
    }

    private void removeItem(int position) {
        cartItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartItems.size());
    }

}
