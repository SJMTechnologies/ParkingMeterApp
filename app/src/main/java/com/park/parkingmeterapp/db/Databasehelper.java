package com.park.parkingmeterapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.park.parkingmeterapp.model.Register;

public class Databasehelper extends SQLiteOpenHelper {
    public static final String TAG = Databasehelper.class.getSimpleName();
    static String DB_NAME = "parkDatabase";

    public Databasehelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + Register.TABLE_NAME + " ( " + Register.KEY_ID + " INTEGER PRIMARY KEY,"
                + Register.KEY_FIRST_NAME + " TEXT," + Register.KEY_LAST_NAME + " TEXT, "
                + Register.KEY_EMAIL + " TEXT," + Register.KEY_TELEPHONE + " TEXT, "
                + Register.KEY_ADDRESS_ONE + " TEXT," + Register.KEY_ADDRESS_TWO + " TEXT, "
                + Register.KEY_CITY + " TEXT," + Register.KEY_POSTAL_CODE + " TEXT, "
                + Register.KEY_COUNTRY + " TEXT," + Register.KEY_REGION_OR_STATE + " TEXT,"
                + Register.KEY_PASSWORD + " TEXT, " + Register.KEY_SUBSCRIBE + " TEXT" + " ) ";
        Log.e(TAG, "onCreate: query " + query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public void insert(Register reg) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Register.KEY_FIRST_NAME, reg.getFirstName());
        cv.put(Register.KEY_LAST_NAME, reg.getLastName());
        cv.put(Register.KEY_EMAIL, reg.getEmail());
        cv.put(Register.KEY_TELEPHONE, reg.getTelephone());
        cv.put(Register.KEY_ADDRESS_ONE, reg.getAddressOne());
        cv.put(Register.KEY_ADDRESS_TWO, reg.getAddressTwo());
        cv.put(Register.KEY_CITY, reg.getCity());
        cv.put(Register.KEY_POSTAL_CODE, reg.getPostalCode());
        cv.put(Register.KEY_COUNTRY, reg.getCountry());
        cv.put(Register.KEY_REGION_OR_STATE, reg.getRegionOrState());
        cv.put(Register.KEY_PASSWORD, reg.getPassword());
        cv.put(Register.KEY_SUBSCRIBE, String.valueOf(reg.isSubscribe()));

        long id = db.insert(Register.TABLE_NAME, null, cv);
        Log.e(TAG, "insert: id " + id);
        db.close();
    }

    public boolean checkUser(String strEmail) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from park_register where email = '" + strEmail + "'";
        Log.e(TAG, "checkUser: query : " + query);
        Cursor c = db.rawQuery(query, null);
        Log.e(TAG, "checkUser: Count " + c.getCount());
        db.close();
        if (c.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkLoginUser(String strEmail, String strPassword) {
        SQLiteDatabase db = getWritableDatabase();
        String username = "", password = "";
        String query = "select * from park_register where email = '" + strEmail + "' and password = '" + strPassword + "'";
        Log.e(TAG, "checkLoginUser: query : " + query);
        Cursor c = db.rawQuery(query, null);
        Log.e(TAG, "checkLoginUser: Count " + c.getCount());
        c.moveToFirst();
        if(c.getCount() > 0 && c.moveToFirst()){
            username = c.getString(c.getColumnIndex(Register.KEY_EMAIL));
            password = c.getString(c.getColumnIndex(Register.KEY_PASSWORD));
        }
        db.close();
        if (username.equals(strEmail) && strPassword.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public String getUsername(String email) {
        SQLiteDatabase db = getWritableDatabase();
        String username = "";
        String query = "select " + Register.KEY_FIRST_NAME + "," + Register.KEY_LAST_NAME + " from " + Register.TABLE_NAME
                + " where " + Register.KEY_EMAIL + " = '" + email + "'";
        Log.e(TAG, "getUsername: query " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.getCount() > 0 && c.moveToFirst()) {
            String fname = c.getString(c.getColumnIndex(Register.KEY_FIRST_NAME));
            String lname = c.getString(c.getColumnIndex(Register.KEY_LAST_NAME));
            username = fname + " " + lname;
        }

        db.close();
        return username;
    }
}