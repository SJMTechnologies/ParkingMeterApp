package com.park.parkingmeterapp;

import android.app.Application;

import com.park.parkingmeterapp.preferences.Preferences;

public class ParkApp extends Application {

    public static Preferences preferences;
    public ParkApp() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = new Preferences(getApplicationContext());
    }
}
