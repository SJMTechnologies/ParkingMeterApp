package com.park.parkingmeterapp.purchase;

import android.util.Log;

public class PurchasePresenterImpl implements PurchasePresenter{

    private static final String TAG = "PurchasePresenterImpl";
    private static final float MAX_PRICE = 1000.0f;
    private static final float MIN_PRICE = 1.75f;

    private static final int MAX_TIME = 1000;
    private static final int MIN_TIME = 1;

    private float price = MIN_PRICE;
    private int time = MIN_TIME;

    private PurchaseView purchaseView;
    public PurchasePresenterImpl(PurchaseView purchaseView){
        this.purchaseView = purchaseView;
    }
    String strPrice="", strTime="";
    @Override
    public void onPlusClicked() {

        price += MIN_PRICE;
        time += MIN_TIME;
        Log.e(TAG, "onIncrease: price " + price + " time " + time);
        if(price >= MAX_PRICE){
            price = MAX_PRICE;
        }

        if(time >= MAX_PRICE){
            time = MAX_TIME;
        }

        if(time < 9){
            strTime = "0" + String.valueOf(time) + ":00";
        } else {
            strTime = String.valueOf(time) + ":00";
        }

        strPrice = String.valueOf(price);

        purchaseView.setTimeAndPrice(strTime,strPrice);
    }

    @Override
    public void onMinusClicked() {

        price -= MIN_PRICE;
        time -= MIN_TIME;
        Log.e(TAG, "onDecrease: price " + price + " time " + time);

        if(time < MIN_TIME){
            time = MIN_TIME;
        }

        if(price < MIN_PRICE){
            price = MIN_PRICE;
        }

        if(time < 9){
            strTime = "0" + String.valueOf(time) + ":00";
        } else {
            strTime = String.valueOf(time) + ":00";
        }

        strPrice = String.valueOf(price);
        purchaseView.setTimeAndPrice(strTime,strPrice);
    }

    @Override
    public void onDoneClicked() {
        if(strPrice.length()==0)
        {
            price = MIN_PRICE;
            time = MIN_TIME;
            if(time < 9){
                strTime = "0" + String.valueOf(time) + ":00";
            } else {
                strTime = String.valueOf(time) + ":00";
            }

            strPrice = String.valueOf(price);
        }
        purchaseView.done(strTime, strPrice);
    }

    @Override
    public void onCancelClicked() {
        purchaseView.cancel();
    }
}
