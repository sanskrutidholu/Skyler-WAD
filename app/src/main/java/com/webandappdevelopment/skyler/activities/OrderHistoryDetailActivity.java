package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.webandappdevelopment.skyler.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderHistoryDetailActivity extends AppCompatActivity {

    TextView name, mobile, email, address,
            service_name, service_detail, total_service,
            order_date, order_status, payment, received;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        findViewById(R.id.back).setOnClickListener(view -> {
            Intent i = new Intent(this,OrderActivity.class);
            startActivity(i);
            finish();
        });

        name = findViewById(R.id.tv_name);
        mobile = findViewById(R.id.tv_mobile);
        email = findViewById(R.id.tv_email);
        address = findViewById(R.id.tv_address);
        service_name = findViewById(R.id.tv_sname);
        service_detail = findViewById(R.id.tv_sprice);
        total_service = findViewById(R.id.tv_tservices);
        order_date = findViewById(R.id.tv_odate);
        payment = findViewById(R.id.tv_payment);
        order_status = findViewById(R.id.tv_ostatus);
        received = findViewById(R.id.tv_pmode);

        setData(name,"full_name");
        setData(mobile,"phone_no");
        setData(email,"email");
        setData(address,"address");
        setData(service_name,"service_name");
        setData(service_detail,"service_price");
        setData(total_service,"service_details");
        setData(payment,"payment_mode");
        setData(order_status,"order_status");


        //  string to date
        String rdate=getIntent().getStringExtra("preferred_date");
        String odate=getIntent().getStringExtra("submit_date");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date receivedDate= null;
        Date orderDate= null;
        try {
            receivedDate = sdf.parse(rdate);
            orderDate = sdf.parse(odate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf2=new SimpleDateFormat("dd MMMM yyyy");
        received.setText(sdf2.format(receivedDate));
        order_date.setText(sdf2.format(orderDate));


        // date to string
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(date);
        System.out.println("Date Format with MM/dd/yyyy : "+strDate);

    }

    private void setData(TextView et, String text){
        String message = getIntent().getStringExtra(text);
        if (message.isEmpty()){
            et.setText("....");
        }else{
            et.setText(message);
        }
    }
}