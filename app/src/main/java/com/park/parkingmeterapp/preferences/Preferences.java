package com.park.parkingmeterapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREF_NAME = "Park_Pref";
    private static final String AUTH_TOKEN = "auth_token";
    private static final String IS_UPDATE_ACCOUNT = "is_update_account";
    private SharedPreferences pref;
    private SharedPreferences.Editor e;

    public Preferences(Context ctx){
        pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        e = pref.edit();
    }

    public void setAuthToken(String authToken){
        e.putString(AUTH_TOKEN,authToken);
        e.apply();
    }

    public String getAuthToken(){
        return pref.getString(AUTH_TOKEN,"");
    }

    public void setIsUpdate(boolean isUpdate){
        e.putBoolean(IS_UPDATE_ACCOUNT,isUpdate);
        e.apply();
    }

    public boolean getIsUpdate(){
        return pref.getBoolean(IS_UPDATE_ACCOUNT,false);
    }

    public void clear(){
        e.clear();
        e.apply();
    }
}
