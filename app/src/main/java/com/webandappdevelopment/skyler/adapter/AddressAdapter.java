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
import com.webandappdevelopment.skyler.modelclass.AddressList;
import com.webandappdevelopment.skyler.modelclass.CartList;
import com.webandappdevelopment.skyler.utility.DBClass;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>  {

    private List<AddressList> addressLists;
    private Context context;
    private DBClass dbClass;

    public AddressAdapter(List<AddressList> addressLists, Context context) {
        this.addressLists = addressLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbClass = new DBClass(context);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AddressList cartItem = addressLists.get(position);
        String tag = cartItem.getTag();
        int id = cartItem.getId();
        String pincode = cartItem.getPincode();
        String state = cartItem.getState();
        String city = cartItem.getCity();
        String house = cartItem.getHouse();
        String area = cartItem.getArea();

        String full_address = pincode + ", " + state + ", " + city + ", " + house + ", " + area;

        holder.tvTag.setText(tag);
        holder.tvAddress.setText(full_address);

        holder.iv_delete.setOnClickListener(v -> {
            dbClass.removeAddress(cartItem.getId());
            removeItem(position);
        });

    }

    @Override
    public int getItemCount() {
        return addressLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvAddress;
        ImageView iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);
            tvAddress = itemView.findViewById(R.id.tv_address);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    private void removeItem(int position) {
        addressLists.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, addressLists.size());
    }

}
