package com.park.parkingmeterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.park.parkingmeterapp.ParkApp;
import com.park.parkingmeterapp.R;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;
    String strTime = "";
    String strAmount = "";
    String strArea = "";
    String strPost = "";
    String strlatitude = "";
    String strLongitude = "";
    String sdate = "", stime = "";
    boolean isfromparklater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            strTime = getIntent().getExtras().getString(getString(R.string.time));
            strAmount = getIntent().getExtras().getString(getString(R.string.amount));
            strArea = getIntent().getExtras().getString(getString(R.string.area));
            strPost = getIntent().getExtras().getString(getString(R.string.post));
            strlatitude = getIntent().getExtras().getString(getString(R.string.latitude));
            strLongitude = getIntent().getExtras().getString(getString(R.string.longitude));
            sdate = getIntent().getExtras().getString(getString(R.string.seldate));
            stime = getIntent().getExtras().getString(getString(R.string.seltime));
            isfromparklater = getIntent().getExtras().getBoolean(getString(R.string.park_later));

        }
        if (!isfromparklater) {
            Calendar c =Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            sdate = (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
            stime = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

        }
        webView.getSettings().setJavaScriptEnabled(true);

        Log.e("Url", "http://54.162.93.65/services/ekashu/index.php?auth_token=" + ParkApp.preferences.getAuthToken() + "&fname=" + ParkApp.preferences.getUSER_FNAME() + "&lname=" + ParkApp.preferences.getUSER_lNAME() + "&address=" + ParkApp.preferences.getUSER_ADDRESS1() + "&city=" + ParkApp.preferences.getUSER_CITY() + "&zone_id=" + ParkApp.preferences.getUSER_STATE() + "&zip=" + ParkApp.preferences.getUSER_ZIP() + "&country_id=" + ParkApp.preferences.getUSER_COUNTRY() + "&email=" + ParkApp.preferences.getUSER_EMAIL()
                + "&longitude=" + strLongitude + "&latitude=" + strlatitude + "&area=" + strArea + "&post=" + strPost + "&total_time=" + strTime + "" + "&amount=" + strAmount + "&selected_date=" + sdate + "&selected_time=" + stime);
        //       Log.e("url2", "http://54.162.93.65/services/ekashu/index.php?auth_token=" + ParkApp.preferences.getAuthToken() + "&fname=" + "Ramesh" + "&lname=" + "Solanki" + "&address=" + "D302 Parswanath" + "&city=" + "Ahmedabad" + "&zone_id=" + "Gujarat" + "&zip=" + "382424" + "&country_id=" + "India" + "&email=" + ParkApp.preferences.getUSER_EMAIL()
        //             + "&longitude=" + strLongitude + "&latitude=" + strlatitude + "&area=" + strArea + "&post=" + strPost + "&total_time=" + strTime + "" + "&amount=" + strAmount + "");
//        webView.loadUrl("http://54.162.93.65/services/ekashu/index.php?auth_token=" + ParkApp.preferences.getAuthToken() + "&fname=" + ParkApp.preferences.getUSER_FNAME() + "&lname=" + ParkApp.preferences.getUSER_lNAME() + "&address=" + ParkApp.preferences.getUSER_ADDRESS() + "&city=" + ParkApp.preferences.getUSER_CITY() + "&zone_id=" + ParkApp.preferences.getUSER_STATE() + "&zip=" + ParkApp.preferences.getUSER_ZIP() + "&country_id=" + ParkApp.preferences.getUSER_COUNTRY() + "&email=" + ParkApp.preferences.getUSER_EMAIL()
//                + "&longitude=" + strLongitude + "&latitude=" + strlatitude + "&area=" + strArea + "&post=" + strPost + "&total_time=" + strTime + "" + "&amount=" + strAmount + "");
        webView.loadUrl("http://54.162.93.65/services/ekashu/index.php?auth_token=" + ParkApp.preferences.getAuthToken() + "&fname=" + ParkApp.preferences.getUSER_FNAME() + "&lname=" + ParkApp.preferences.getUSER_lNAME() + "&address=" + ParkApp.preferences.getUSER_ADDRESS1() + "&city=" + ParkApp.preferences.getUSER_CITY() + "&zone_id=" + ParkApp.preferences.getUSER_STATE() + "&zip=" + ParkApp.preferences.getUSER_ZIP() + "&country_id=" + ParkApp.preferences.getUSER_COUNTRY() + "&email=" + ParkApp.preferences.getUSER_EMAIL()
                + "&longitude=" + strLongitude + "&latitude=" + strlatitude + "&area=" + strArea + "&post=" + strPost + "&total_time=" + strTime + "" + "&amount=" + strAmount + "&selected_date=" + sdate + "&selected_time=" + stime);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
    }
}
