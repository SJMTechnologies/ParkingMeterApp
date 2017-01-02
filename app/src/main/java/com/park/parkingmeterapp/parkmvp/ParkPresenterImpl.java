package com.park.parkingmeterapp.parkmvp;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.park.parkingmeterapp.ParkApp;
import com.park.parkingmeterapp.fragment.ParkLaterFragment;
import com.park.parkingmeterapp.model.MarkerDetail;
import com.park.parkingmeterapp.retrofit.ApiService;
import com.park.parkingmeterapp.retrofit.RetroClient;
import com.park.parkingmeterapp.utils.InternetConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkPresenterImpl implements ParkPresenter {

    private static final String TAG = "ParkPresenterImpl";
    private ParkView parkView;
    private Context context;
    private List<MarkerDetail> markerDetailsList;
    public ParkPresenterImpl(ParkView parkView, Context context) {
        this.parkView = parkView;
        this.context = context;
    }

    @Override
    public List<MarkerDetail> loadMarkers(double latitude, double longitude) {

        if (InternetConnection.checkConnection(context)) {
            ApiService api = RetroClient.getApiService();
            String authToken = ParkApp.preferences.getAuthToken();
            Log.e(TAG, "loadMarkers: authToken " + authToken);
            Call<String> call = api.getMarkerDetails(authToken, String.valueOf("36.840197"), String.valueOf("-75.973216"));

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e(TAG, "onResponse: loadMarkers " + response.isSuccessful());
                    Log.e(TAG, "onResponse: loadMarkers " + response.body());
                    if (response.isSuccessful()) {
                         markerDetailsList = getMarkerJson(response.body());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "onFailure: loadMarkers " + t.getMessage());
                }
            });
        }
        return markerDetailsList;
    }

    private List<MarkerDetail> getMarkerJson(String body) {
        List<MarkerDetail> markerDetailList = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(body);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject row = result.getJSONObject("rows");
            String code = row.optString("code");
            String message = row.optString("message");
            Log.e(TAG, "getMarkerJson: code " + code);
            if (code.equals("1")) {
                for (int i = 0; i < row.length() - 1; i++) {
                    JSONObject j = row.getJSONObject(String.valueOf(i));
                    MarkerDetail md = new MarkerDetail();
                    md.setArea(j.optString(MarkerDetail.KEY_AREA));
                    md.setPost(j.optString(MarkerDetail.KEY_POST));
                    md.setLatitude(j.optString(MarkerDetail.KEY_LATITUDE));
                    md.setLongitude(j.optString(MarkerDetail.KEY_LONGITUDE));
                    Log.e(TAG, "getMarkerJson: md " + md.toString());
                    ParkLaterFragment.markerDetailList.add(md);
                }
            } else {
                parkView.showToast(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "getMarkerJson: markerDetailList " + ParkLaterFragment.markerDetailList.size());
        return markerDetailList;
    }

    @Override
    public void addMarkers(GoogleMap mGoogleMap, List<MarkerDetail> markerList) {
        Log.e(TAG, "addMarkers: " + markerList.size());
        for (MarkerDetail markerDetail : markerList) {
            MarkerOptions markerOptions = new MarkerOptions();
            Log.e(TAG, "addMarkerToMap: markerDetail.getLatitude() " + markerDetail.getLatitude() + " " + markerDetail.getLongitude());
            LatLng position = new LatLng(Double.parseDouble(markerDetail.getLatitude()), Double.parseDouble(markerDetail.getLongitude()));
            markerOptions.position(position);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
            markerOptions.title(markerDetail.getPost());
            markerOptions.snippet(markerDetail.getArea());
            mGoogleMap.addMarker(markerOptions);
        }

        LatLng position = new LatLng(Double.parseDouble(markerList.get(0).getLatitude()), Double.parseDouble(markerList.get(0).getLongitude()));
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(position, 15);
        mGoogleMap.animateCamera(cameraPosition);

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                parkView.startActivity(marker.getPosition().latitude, marker.getPosition().longitude, marker.getSnippet(), marker.getTitle());
            }
        });
    }

    @Override
    public void addMarkers(GoogleMap mGoogleMap, double latitude, double longitude, String address) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng position = new LatLng(latitude, longitude);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        markerOptions.title(address);
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                parkView.startActivity(marker.getPosition().latitude, marker.getPosition().longitude, marker.getSnippet(), marker.getTitle());
            }
        });
    }
}
