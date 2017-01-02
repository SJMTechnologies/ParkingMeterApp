package com.park.parkingmeterapp.parkmvp;

/**
 * Created by Jitesh Dalsaniya on 21-Dec-16.
 */

public interface ParkView {

    void startActivity(double latitude, double longitude, String area, String post);
    void showToast(String message);
}
