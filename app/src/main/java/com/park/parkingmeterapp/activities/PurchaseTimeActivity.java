package com.park.parkingmeterapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.park.parkingmeterapp.R;
import com.park.parkingmeterapp.fragment.ParkLaterFragment;
import com.park.parkingmeterapp.purchase.PurchasePresenterImpl;
import com.park.parkingmeterapp.purchase.PurchaseView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseTimeActivity extends AppCompatActivity implements PurchaseView {

    @BindView(R.id.btnIncrease)
    ImageView btnIncrease;

    @BindView(R.id.btnDecrease)
    ImageView btnDecrease;

    @BindView(R.id.btnDone)
    ImageView btnDone;

    @BindView(R.id.btnCancel)
    ImageView btnCancel;

    @BindView(R.id.txtPrice)
    TextView txtPrice;

    @BindView(R.id.txtTime)
    TextView txtTime;

    private PurchasePresenterImpl purchasePresenter;
    String lat , lng ;
    String area = "", post = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_time);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
          Bundle b =  getIntent().getExtras();

            area = getIntent().getExtras().getString(ParkLaterFragment.KEY_AREA);
            post = getIntent().getExtras().getString(ParkLaterFragment.KEY_POST);
            lat = getIntent().getExtras().getString(ParkLaterFragment.KEY_LATITUDE);
            lng = getIntent().getExtras().getString(ParkLaterFragment.KEY_LONGITUDE);
        }
        purchasePresenter = new PurchasePresenterImpl(this);
    }

    @OnClick(R.id.btnIncrease)
    public void onIncrease() {
        purchasePresenter.onPlusClicked();
    }

    @OnClick(R.id.btnDecrease)
    public void onDecrease() {
        purchasePresenter.onMinusClicked();
    }

    @OnClick(R.id.btnDone)
    public void onDone() {
        purchasePresenter.onDoneClicked();
    }

    @OnClick(R.id.btnCancel)
    public void onCancel() {
        purchasePresenter.onCancelClicked();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setTimeAndPrice(String time, String price) {
        txtPrice.setText("$" + price);
        txtTime.setText(time);
    }

    @Override
    public void done(String time,  String amount) {
        Intent Kintnet = new Intent(PurchaseTimeActivity.this, PaymentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.time), time);
        bundle.putString(getString(R.string.area), area);
        bundle.putString(getString(R.string.amount), amount);
        bundle.putString(getString(R.string.post), post);
        bundle.putString(getString(R.string.longitude), lat);
        bundle.putString(getString(R.string.latitude), lng);
        Kintnet.putExtras(bundle);
        startActivity(Kintnet);
        PurchaseTimeActivity.this.finish();
    }

    @Override
    public void cancel() {
        PurchaseTimeActivity.this.finish();
    }

}
