package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.adapter.MyCartAdapter;
import com.webandappdevelopment.skyler.adapter.ServiceDetailAdapter;
import com.webandappdevelopment.skyler.modelclass.CartList;
import com.webandappdevelopment.skyler.utility.CallToast;
import com.webandappdevelopment.skyler.utility.DBClass;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CartService_DetailsActivity extends AppCompatActivity {

    ImageView back;
    Button next;
    TextView tv_totalPrice;
    RecyclerView rv_serviceDetails;
    ServiceDetailAdapter cartAdapter;

    List<CartList> cartItems = new ArrayList<>();
    CartList cartItem ;
    InternetConnectionDetector icd;
    DBClass dbClass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_service_details);

        back = findViewById(R.id.back);
        next = findViewById(R.id.btn_next);
        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        rv_serviceDetails = findViewById(R.id.rv_serviceDetails);

        dbClass = new DBClass(this);
        icd = new InternetConnectionDetector(this);

        if(icd.isConnected()) {
            rv_serviceDetails.setHasFixedSize(true);
            rv_serviceDetails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
            loadCart();
        }else{
            new ShowCustomDialog(this);
        }

        back.setOnClickListener(view -> {
           startActivity(new Intent(this, MyCartActivity.class));
           finish();
        });

        next.setOnClickListener(view ->  {
            startActivity(new Intent(this, CartService_DateTimeActivity.class));
            finish();
        });

    }

    private void loadCart() {
        if (dbClass.isEmpty()){
            new CallToast(this,"No Item Added..");
        } else {
            if (cartItems != null){ cartItems.clear(); }
            SQLiteDatabase db = dbClass.getReadableDatabase();
            Cursor cursor = dbClass.selectCartItem();
            try {
                while (cursor.moveToNext()){
                    Log.d("inside:", "while");
                    @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbClass.KEY_ID)));
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_NAME));
                    @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_PRICE));
                    @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_IMAGE));
                    @SuppressLint("Range") String detail = cursor.getString(cursor.getColumnIndex(dbClass.ITEM_DETAILS));
                    cartItem = new CartList(id,title,price,image,detail);
                    Log.d("cartItem:", cartItem.getTitle());
                    cartItems.add(cartItem);
                }

            } finally {
                if (cursor!=null && cursor.isClosed()){
                    cursor.close();
                }
                db.close();
            }

            cartAdapter = new ServiceDetailAdapter(cartItems, CartService_DetailsActivity.this);
            rv_serviceDetails.setAdapter(cartAdapter);
        }
    }

}