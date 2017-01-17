package com.park.parkingmeterapp.purchase;

/**
 * Created by Jitesh Dalsaniya on 30-Nov-16.
 */

public interface PurchaseView {

    void setTimeAndPrice(String time, String price);
    void done(String time,String amount);
    void cancel();
}
