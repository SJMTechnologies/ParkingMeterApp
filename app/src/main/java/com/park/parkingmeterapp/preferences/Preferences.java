package com.park.parkingmeterapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREF_NAME = "Park_Pref";
    private static final String AUTH_TOKEN = "auth_token";
    private static final String IS_UPDATE_ACCOUNT = "is_update_account";
    private static final String NOTIFICATION_TOKEN = "notification_token";
    private SharedPreferences pref;
    private SharedPreferences.Editor e;

    private static final String USER_CUSTID = "customer_id";
    private static final String USER_FNAME = "fname";
    private static final String USER_lNAME = "lname";
    private static final String USER_ADDRESS1 = "address1";
    private static final String USER_ADDRESS2 = "address2";
    private static final String USER_CITY = "city";
    private static final String USER_STATE = "state";
    private static final String USER_ZIP = "zip";
    private static final String USER_COUNTRY = "country";
    private static final String USER_EMAIL = "email";
    private static final String USER_TELEPHONE = "telephone";
    private static final String USER_FAX = "fax";
    private static final String USER_COMPANY = "company";
    private static final String USER_NEWSLETER = "newsleter";
    private static final String USER_BADGE = "badge";

    public Preferences(Context ctx) {
        pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        e = pref.edit();
    }

    public String getUSER_FNAME() {
        return pref.getString(USER_FNAME, "");
    }

    public String getUSER_lNAME() {
        return pref.getString(USER_lNAME, "");
    }

    public String getUSER_ADDRESS1() {
        return pref.getString(USER_ADDRESS1, "");
    }

    public String getUSER_ADDRESS2() {
        return pref.getString(USER_ADDRESS2, "");
    }

    public String getUSER_CITY() {
        return pref.getString(USER_CITY, "");
    }

    public String getUSER_STATE() {
        return pref.getString(USER_STATE, "");
    }

    public String getUSER_ZIP() {
        return pref.getString(USER_ZIP, "");
    }

    public String getUSER_COUNTRY() {
        return pref.getString(USER_COUNTRY, "");
    }

    public String getUSER_TELEPHONE() {
        return pref.getString(USER_TELEPHONE, "");
    }

    public String getUSER_EMAIL() {
        return pref.getString(USER_EMAIL, "");
    }

    public String getUSER_FAX() {
        return pref.getString(USER_FAX, "");
    }

    public String getUSER_COMPANY() {
        return pref.getString(USER_COMPANY, "");
    }

    public String getUSER_BADGE() {
        return pref.getString(USER_BADGE, "");
    }

    public String getUSER_NEWSLETER() {
        return pref.getString(USER_NEWSLETER, "");
    }

    public String getUSER_CUSTID() {
        return pref.getString(USER_CUSTID, "");
    }

    public void setUSER_NEWSLETER(String authToken) {
        e.putString(USER_NEWSLETER, authToken);
        e.apply();
    }

    public void setUSER_CUSTID(String authToken) {
        e.putString(USER_CUSTID, authToken);
        e.apply();
    }

    public void setUSER_BADGE(String authToken) {
        e.putString(USER_BADGE, authToken);
        e.apply();
    }

    public void setUSER_COMPANY(String authToken) {
        e.putString(USER_COMPANY, authToken);
        e.apply();
    }

    public void setUSER_FAX(String authToken) {
        e.putString(USER_FAX, authToken);
        e.apply();
    }

    public void setUSER_TELEPHONE(String authToken) {
        e.putString(USER_TELEPHONE, authToken);
        e.apply();
    }

    public void setAuthToken(String authToken) {
        e.putString(AUTH_TOKEN, authToken);
        e.apply();
    }

    public void setUSER_FNAME(String fname) {
        e.putString(USER_FNAME, fname);
        e.apply();
    }

    public void setUSER_lNAME(String lname) {
        e.putString(USER_lNAME, lname);
        e.apply();
    }

    public void setUSER_ADDRESS1(String address) {
        e.putString(USER_ADDRESS1, address);
        e.apply();
    }

    public void setUSER_ADDRESS2(String address) {
        e.putString(USER_ADDRESS2, address);
        e.apply();
    }

    public void setUSER_CITY(String city) {
        e.putString(USER_CITY, city);
        e.apply();
    }

    public void setUSER_STATE(String state) {
        e.putString(USER_STATE, state);
        e.apply();
    }

    public void setUSER_ZIP(String zip) {
        e.putString(USER_ZIP, zip);
        e.apply();
    }

    public void setUSER_COUNTRY(String country) {
        e.putString(USER_COUNTRY, country);
        e.apply();
    }

    public void setUSER_EMAIL(String email) {
        e.putString(USER_EMAIL, email);
        e.apply();
    }


    public String getAuthToken() {
        return pref.getString(AUTH_TOKEN, "");
    }

    public void setIsUpdate(boolean isUpdate) {
        e.putBoolean(IS_UPDATE_ACCOUNT, isUpdate);
        e.apply();
    }

    public void setNotificationToken(String token) {
        e.putString(NOTIFICATION_TOKEN, token);
        e.apply();
    }

    public String getNotificationToken() {
        String token = pref.getString(NOTIFICATION_TOKEN, "");
        return token;
    }

    public boolean getIsUpdate() {
        return pref.getBoolean(IS_UPDATE_ACCOUNT, false);
    }

    public void clear() {
        e.clear();
        e.apply();
    }

    public void clearPref() {
        e.putString(USER_CUSTID, "");
        e.putString(USER_BADGE, "");
        e.putString(USER_COMPANY, "");
        e.putString(USER_FAX, "");
        e.putString(USER_TELEPHONE, "");
        e.putString(USER_FNAME, "");
        e.putString(USER_lNAME, "");
        e.putString(USER_ADDRESS1, "");
        e.putString(USER_ADDRESS2, "");
        e.putString(USER_CITY, "");
        e.putString(USER_STATE, "");
        e.putString(USER_ZIP, "");
        e.putString(USER_COUNTRY, "");
        e.putString(USER_EMAIL, "");
        e.putString(USER_NEWSLETER, "");
        e.apply();


    }



}
