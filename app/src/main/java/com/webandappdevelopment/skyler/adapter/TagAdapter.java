package com.webandappdevelopment.skyler.adapter;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
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
import com.webandappdevelopment.skyler.modelclass.AddressList;
import com.webandappdevelopment.skyler.modelclass.CartList;
import com.webandappdevelopment.skyler.utility.DBClass;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>  {

    private List<AddressList> addressLists;
    private Context context;
    private TagAdapter.ItemClickListner clickListener;

    public TagAdapter(List<AddressList> addressLists, Context context,TagAdapter.ItemClickListner clickListner) {
        this.addressLists = addressLists;
        this.context = context;
        this.clickListener=clickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AddressList cartItem = addressLists.get(position);
        String tag = cartItem.getTag();

        holder.tvTag.setText(tag);

        holder.tvTag.setOnClickListener(view->{
            clickListener.onItemClick(cartItem);
        });
    }

    @Override
    public int getItemCount() {
        return addressLists.size();
    }

    public interface ItemClickListner {
        public void onItemClick(AddressList addressList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);

        }
    }

}
