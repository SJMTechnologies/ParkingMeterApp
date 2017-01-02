package com.park.parkingmeterapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.park.parkingmeterapp.R;
import com.park.parkingmeterapp.adapter.PlaceArrayAdapter;

/**
 * Created by Jitesh Dalsaniya on 05-Nov-16.
 */
public class PlacesActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final String TAG = PlacesActivity.class.getSimpleName();
    private AutoCompleteTextView txtAutoComplete;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    Location mLastLocation;
    private LocationRequest mLocationRequest;
    double lat, lon;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                Log.e(TAG, "onReceive: Action " + intent.getAction());
                if (mGoogleApiClient != null) {
                    if (mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                    }
                    mGoogleApiClient.connect();
                }
            }
        }
    };

    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_activity);
        intentFilter = new IntentFilter();
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(receiver, intentFilter);
        txtAutoComplete = (AutoCompleteTextView) findViewById(R.id.txtAutoComplete);

        if (mGoogleApiClient == null) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(PlacesActivity.this)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        txtAutoComplete.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(PlacesActivity.this, android.R.layout.simple_list_item_1,
                null, null);
        txtAutoComplete.setAdapter(mPlaceArrayAdapter);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(i);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);

            CharSequence attributions = places.getAttributions();

            Log.e(TAG, "Latitude and Longitude : " + place.getLatLng().latitude + " " + place.getLatLng().longitude);
            InputMethodManager imm = (InputMethodManager) PlacesActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txtAutoComplete.getWindowToken(), 0);
            String address = Html.fromHtml(place.getAddress() + "").toString();
            Log.e(TAG, "onResult: Address " + address);
            places.release();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
//        Toast.makeText(PlacesActivity.this,"Connected !", Toast.LENGTH_SHORT).show();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();
        }
        LatLng latLng = new LatLng(lat, lon);
        LatLngBounds latLngBounds = toBounds(latLng, 1600.34d);
//        openAutocompleteActivity(latLngBounds);
        mPlaceArrayAdapter.setLatLngBound(latLngBounds);
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.e(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: ");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }

    public LatLngBounds toBounds(LatLng center, double radius){
        LatLng southWest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0),255);
        LatLng northEast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0),45);
        Log.e(TAG, "toBounds: South West Bound " + southWest.toString());
        Log.e(TAG, "toBounds: North East Bound " + southWest.toString());
        Log.e(TAG, "toBounds: Current Location center " + center.toString());
        return new LatLngBounds(southWest, northEast);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
