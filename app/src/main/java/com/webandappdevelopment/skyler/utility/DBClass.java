package com.webandappdevelopment.skyler.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.webandappdevelopment.skyler.modelclass.AddressList;

public class DBClass extends SQLiteOpenHelper {

    private static int DB_VERSION = 3;
    private static String DB_NAME = "CartListDB";
    private static String TABLE_NAME = "CartTable";
    public static String KEY_ID = "id";
    public static String ITEM_NAME = "itemName";
    public static String ITEM_IMAGE = "itemImage";
    public static String ITEM_DETAILS = "itemDetails";
    public static String ITEM_COUNT = "itemCount";
    public static String ITEM_PRICE = "itemPrice";
    public static String ITEM_NEW_PRICE = "itemNewPrice";
    public static String CART_STATUS = "cartStatus";

    private static String ADDRESS_TABLE_NAME = "AddressTable";
    public static String ADDRESS_ID = "addressId";
    public static String ADDRESS_PINCODE = "addresspincode";
    public static String ADDRESS_STATE = "addressstate";
    public static String ADDRESS_CITY = "addresscity";
    public static String ADDRESS_HOUSE = "addresshouse";
    public static String ADDRESS_AREA = "addressarea";
    public static String ADDRESS_TAG = "addresstag";


    public static String CREATE_TABLE_ADDRESS = "CREATE TABLE " + ADDRESS_TABLE_NAME + " ("
            + ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ADDRESS_PINCODE + " TEXT, "
            + ADDRESS_STATE + " TEXT, "
            + ADDRESS_CITY + " TEXT, "
            + ADDRESS_HOUSE + " TEXT, "
            + ADDRESS_AREA + " TEXT, "
            + ADDRESS_TAG + " TEXT);";

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + KEY_ID + " TEXT, "
            + ITEM_NAME + " TEXT, "
            + ITEM_PRICE + " TEXT, "
            + ITEM_NEW_PRICE + " TEXT, "
            + ITEM_IMAGE + " TEXT, "
            + ITEM_DETAILS + " TEXT, "
            + ITEM_COUNT + " TEXT, "
            + CART_STATUS + " TEXT);";

    public DBClass(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_ADDRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ADDRESS_TABLE_NAME);
        }
        onCreate(db);
    }

    public void insertEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        for (int x=1; x<11 ; x++){
            cv.put(KEY_ID,x);
            cv.put(CART_STATUS, 0);
            db.insert(TABLE_NAME,null, cv);
        }
    }

    public void insertIntoDatabase(int id, String item_name, String item_price,  String newPrice, String item_image, String item_details, String cart_status, String item_count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID,id);
        cv.put(ITEM_NAME, item_name);
        cv.put(ITEM_PRICE, item_price);
        cv.put(ITEM_NEW_PRICE, newPrice);
        cv.put(ITEM_IMAGE, item_image);
        cv.put(ITEM_DETAILS, item_details);
        cv.put(CART_STATUS, cart_status);
        cv.put(ITEM_COUNT, item_count);
        db.insert(TABLE_NAME,null, cv);
    }

    public Cursor readAllData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "=" + id + ";";
        return db.rawQuery(sql, null, null);
    }

    public void updateCartItem(int id, String total){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE "+ TABLE_NAME + " SET " + ITEM_COUNT + "=" + total + " WHERE "+ KEY_ID+ "=" + id + ";";
        db.execSQL(sql);
    }

    public void updateItemPrice(int id, String price){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE "+ TABLE_NAME + " SET " + ITEM_NEW_PRICE + "=" + price + " WHERE "+ KEY_ID+ "=" + id + ";";
        db.execSQL(sql);
    }

    public void removeCartItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE "+ KEY_ID+"="+id+";";
        db.execSQL(sql);
    }

    public void emptyCartItem(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public Cursor selectCartItem(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + CART_STATUS + " ='1'";
        return db.rawQuery(sql, null, null);
    }

    public boolean isEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cur != null){
            cur.moveToFirst();
            if (cur.getInt(0) == 0) {
                return true;
            }
        }
        return false;
    }

    // for address data

    public void insertAddress(String pincode, String state, String city, String house, String area, String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ADDRESS_PINCODE,pincode);
        cv.put(ADDRESS_STATE,state);
        cv.put(ADDRESS_CITY,city);
        cv.put(ADDRESS_HOUSE,house);
        cv.put(ADDRESS_AREA,area);
        cv.put(ADDRESS_TAG,tag);
        long result = db.insert(ADDRESS_TABLE_NAME,null,cv);
        if (result == -1){
            Log.e("","database"+"fail");
        }else{
            Log.e("","database"+"added");
        }
    }

    public Cursor viewAllAddress(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + ADDRESS_TABLE_NAME;
        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(sql,null);
        }
        return cursor;
    }

    public void removeAddress(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + ADDRESS_TABLE_NAME + " WHERE "+ ADDRESS_ID+"="+id+";";
        db.execSQL(sql);
    }
}