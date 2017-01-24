package com.park.parkingmeterapp.parkmvp;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Jitesh Dalsaniya on 21-Dec-16.
 */

public interface ParkView {

    void startActivity(double latitude, double longitude, String area, String post,String sDate, String sTime);
    void showToast(String message);
    void onMarkerClick(Marker m);
}
