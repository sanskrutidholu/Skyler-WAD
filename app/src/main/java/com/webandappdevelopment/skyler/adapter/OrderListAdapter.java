package com.webandappdevelopment.skyler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.activities.OrderHistoryDetailActivity;
import com.webandappdevelopment.skyler.modelclass.OrderItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>  {
    private List<OrderItem> orderItems;
    private Context context;

    public OrderListAdapter(Context context, List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);

        holder.tvTitle.setText(item.getService_name());
        holder.tvPrice.setText("Rs."+item.getService_price()+"/-");
        holder.tvPerson.setText(item.getService_details());
        holder.ivImage.setBackgroundResource(R.mipmap.ic_launcher);

        String orderStatus = item.getOrder_status();
        if (orderStatus.equals("pending")){
            holder.tv_status.setText("Pending");
        }else{
            holder.tv_status.setText("Received");
        }


        // string to date
        String rdate=item.getPreferred_date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date receivedDate= null;
        try {
            receivedDate = sdf.parse(rdate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf2=new SimpleDateFormat("dd-MM-yyyy");
        holder.tvSchedule.setText(sdf2.format(receivedDate));




        holder.tv_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, OrderHistoryDetailActivity.class);
                i.putExtra("service_name",item.getService_name());
                i.putExtra("service_details",item.getService_details());
                i.putExtra("service_price",item.getService_price());
                i.putExtra("preferred_date",item.getPreferred_date());
                i.putExtra("preferred_time",item.getPreferred_time());
                i.putExtra("submit_date",item.getSubmit_date());
                i.putExtra("full_name",item.getFull_name());
                i.putExtra("phone_no",item.getPhone_no());
                i.putExtra("address",item.getAddress());
                i.putExtra("email",item.getEmail());
                i.putExtra("payment_mode",item.getPayment_mode());
                i.putExtra("order_status",item.getOrder_status());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvPrice, tvPerson, tvSchedule, tv_status, tv_viewAll;
        private ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.order_tvTitle);
            tvPrice = itemView.findViewById(R.id.order_tvPrice);
            tvPerson = itemView.findViewById(R.id.order_tvTotalServices);
            tvSchedule = itemView.findViewById(R.id.order_tvSchedule);
            ivImage = itemView.findViewById(R.id.order_ivImage);
            tv_status = itemView.findViewById(R.id.tv_orderStatus);
            tv_viewAll = itemView.findViewById(R.id.order_tvViewDetails);
        }
    }
}
