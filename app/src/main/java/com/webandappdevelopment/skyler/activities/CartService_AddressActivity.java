package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.adapter.AddressAdapter;
import com.webandappdevelopment.skyler.adapter.TagAdapter;
import com.webandappdevelopment.skyler.modelclass.AddressList;
import com.webandappdevelopment.skyler.utility.CallToast;
import com.webandappdevelopment.skyler.utility.DBClass;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import java.util.ArrayList;
import java.util.List;

public class CartService_AddressActivity extends AppCompatActivity implements TagAdapter.ItemClickListner{

    ImageView back;
    Button next;
    EditText et_name, et_number1, et_number2;
    TextInputLayout ll_number2;
    TextView tv_addNumber, et_pincode, et_state, et_city, et_house, et_area;
    RecyclerView rv_tag;

    String name, number1, number2, pincode, state, city, house, area;
    SharedPreferences orderPreference;
    SharedPreferences.Editor orderEditor;

    private TagAdapter.ItemClickListner clickListner;
    DBClass dbClass;
    List<AddressList> addressLists = new ArrayList<>();
    InternetConnectionDetector icd;
    AddressList addressList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_service_address);

        findViewById(R.id.hide).setOnClickListener(this::closeKeyboard);

        back = findViewById(R.id.back);
        next = findViewById(R.id.btn_next);
        rv_tag = findViewById(R.id.rv_tag);
        tv_addNumber = findViewById(R.id.tv_addNumber);
        et_name = findViewById(R.id.nameEt);
        et_number1 = findViewById(R.id.contactEt);
        et_number2 = findViewById(R.id.contactEt2);
        et_pincode = findViewById(R.id.pincodeEt);
        et_state = findViewById(R.id.stateEt);
        et_city = findViewById(R.id.cityEt);
        et_house = findViewById(R.id.houseNoEt);
        et_area = findViewById(R.id.areaEt);
        ll_number2 = findViewById(R.id.et_contact2);

        clickListner = this;
        dbClass = new DBClass(this);
        icd = new InternetConnectionDetector(this);

        tv_addNumber.setOnClickListener(view -> {
            ll_number2.setVisibility(View.VISIBLE);
        });

        back.setOnClickListener(view -> {
            startActivity(new Intent(this, CartService_DateTimeActivity.class));
            finish();
        });

        if (icd.isConnected()){
            rv_tag.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rv_tag.setLayoutManager(linearLayoutManager);
            loadTag();
        }else{
            new ShowCustomDialog(this);
        }



        next.setOnClickListener(view ->  {
            name = et_name.getText().toString();
            number1 = et_number1.getText().toString();
            number2 = et_number2.getText().toString();
            pincode = et_pincode.getText().toString();
            state = et_state.getText().toString();
            city = et_city.getText().toString();
            house = et_house.getText().toString();
            area = et_area.getText().toString();

            if (name.isEmpty()){
                new CallToast(this,"Enter name..");
            }else if(number1.isEmpty()){
                new CallToast(this,"Enter contact..");
            }else if(pincode.isEmpty()){
                new CallToast(this,"Enter pincode..");
            }else if(state.isEmpty()){
                new CallToast(this,"Enter state..");
            }else if(city.isEmpty()){
                new CallToast(this,"Enter city..");
            }else if(house.isEmpty()){
                new CallToast(this,"Enter house..");
            }else if(area.isEmpty()){
                new CallToast(this,"Enter area..");
            }
            else{
                orderPreference = getSharedPreferences("ORDER_DETAILS", MODE_PRIVATE);
                orderEditor = orderPreference.edit();
                orderEditor.putString("O_NAME",name);
                orderEditor.putString("O_CONTACT_1",number1);
                orderEditor.putString("O_CONTACT_2",number2);
                orderEditor.putString("O_PINCODE",pincode);
                orderEditor.putString("O_STATE",state);
                orderEditor.putString("O_CITY",city);
                orderEditor.putString("O_HOUSE",house);
                orderEditor.putString("O_AREA",area);
                orderEditor.apply();

                Intent i = new Intent(this,CartService_PaymentActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    private void loadTag(){
        SQLiteDatabase db = dbClass.getReadableDatabase();
        Cursor cursor = dbClass.viewAllAddress();
        try{
            while (cursor.moveToNext()){
                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbClass.ADDRESS_ID)));
                @SuppressLint("Range") String pincode = cursor.getString(cursor.getColumnIndex(dbClass.ADDRESS_PINCODE));
                @SuppressLint("Range") String state = cursor.getString(cursor.getColumnIndex(dbClass.ADDRESS_STATE));
                @SuppressLint("Range") String city = cursor.getString(cursor.getColumnIndex(dbClass.ADDRESS_CITY));
                @SuppressLint("Range") String house = cursor.getString(cursor.getColumnIndex(dbClass.ADDRESS_HOUSE));
                @SuppressLint("Range") String area = cursor.getString(cursor.getColumnIndex(dbClass.ADDRESS_AREA));
                @SuppressLint("Range") String tag = cursor.getString(cursor.getColumnIndex(dbClass.ADDRESS_TAG));

                addressList = new AddressList(id,pincode,state,city,house,area,tag);
                addressLists.add(addressList);
            }

            TagAdapter addressAdapter = new TagAdapter(addressLists,this,clickListner);
            rv_tag.setAdapter(addressAdapter);

        }finally {
            if (cursor!=null && cursor.isClosed()){
                cursor.close();
            }
            db.close();
        }
    }

    private void closeKeyboard(View view) {
        view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemClick(AddressList addressList) {
        pincode = addressList.getPincode();
        state = addressList.getState();
        city = addressList.getCity();
        house = addressList.getHouse();
        area = addressList.getArea();

        et_pincode.setText(pincode);
        et_state.setText(state);
        et_city.setText(city);
        et_house.setText(house);
        et_area.setText(area);
    }
}