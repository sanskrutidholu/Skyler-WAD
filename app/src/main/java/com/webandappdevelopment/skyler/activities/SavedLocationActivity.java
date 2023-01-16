package com.webandappdevelopment.skyler.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.adapter.AddressAdapter;
import com.webandappdevelopment.skyler.modelclass.AddressList;
import com.webandappdevelopment.skyler.utility.DBClass;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import java.util.ArrayList;
import java.util.List;

public class SavedLocationActivity extends AppCompatActivity {

    LinearLayout ll_locations;
    ImageView back2;
    RecyclerView rv_locations;
    Button btn_address, btn_add;
    FrameLayout fl_addressForm;
    EditText et_pincode, et_state, et_city, et_house, et_area, et_tag;

    DBClass dbClass;
    List<AddressList> addressLists = new ArrayList<>();
    InternetConnectionDetector icd;
    AddressList addressList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location);

        findViewById(R.id.hide).setOnClickListener(this::closeKeyboard);

        ll_locations = findViewById(R.id.ll_locations);
        rv_locations = findViewById(R.id.rv_locations);
        btn_address = findViewById(R.id.btn_address);
        btn_add = findViewById(R.id.btn_add);
        fl_addressForm = findViewById(R.id.fl_addressForm);
        back2 = findViewById(R.id.back2);
        et_pincode = findViewById(R.id.pincodeEt);
        et_state = findViewById(R.id.stateEt);
        et_city = findViewById(R.id.cityEt);
        et_house = findViewById(R.id.houseNoEt);
        et_area = findViewById(R.id.areaEt);
        et_tag = findViewById(R.id.tageEt);

        dbClass = new DBClass(this);
        icd = new InternetConnectionDetector(this);

        findViewById(R.id.back).setOnClickListener(view -> {
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        });

        back2.setOnClickListener(view -> {
            loadAddress();
            fl_addressForm.setVisibility(GONE);
            ll_locations.setVisibility(VISIBLE);
        });


        btn_address.setOnClickListener(view ->{
            fl_addressForm.setVisibility(VISIBLE);
            ll_locations.setVisibility(GONE);
        });

        btn_add.setOnClickListener(view -> {
            String pinPattern = "[6-9][0-9]{5}";
            String pincode = et_pincode.getText().toString();
            String state = et_state.getText().toString();
            String city = et_city.getText().toString();
            String house = et_house.getText().toString();
            String area = et_area.getText().toString();
            String tag = et_tag.getText().toString();

            if (pinPattern.isEmpty()){
                Toast.makeText(this, "Enter pincode", Toast.LENGTH_SHORT).show();
            }else if(!(pincode.matches(pincode))){
                Toast.makeText(this, "Enter valid pincode", Toast.LENGTH_SHORT).show();
            } else if (state.isEmpty()) {
                Toast.makeText(this, "Enter state", Toast.LENGTH_SHORT).show();
            }else if (city.isEmpty()) {
                Toast.makeText(this, "Enter city", Toast.LENGTH_SHORT).show();
            }else if (house.isEmpty()) {
                Toast.makeText(this, "Enter house", Toast.LENGTH_SHORT).show();
            }else if (area.isEmpty()) {
                Toast.makeText(this, "Enter area", Toast.LENGTH_SHORT).show();
            }else if (tag.isEmpty()) {
                Toast.makeText(this, "Enter tag", Toast.LENGTH_SHORT).show();
            }else{
                dbClass.insertAddress(pincode,state,city,house,area,tag);
                fl_addressForm.setVisibility(GONE);
                ll_locations.setVisibility(VISIBLE);
            }
        });

        if (icd.isConnected()){
            rv_locations.setHasFixedSize(true);
            rv_locations.setLayoutManager(new LinearLayoutManager(this));
            loadAddress();
        }else{
            new ShowCustomDialog(this);
        }
    }

    private void loadAddress(){
        addressLists.clear();
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

            AddressAdapter addressAdapter = new AddressAdapter(addressLists,this);
            rv_locations.setAdapter(addressAdapter);

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
}