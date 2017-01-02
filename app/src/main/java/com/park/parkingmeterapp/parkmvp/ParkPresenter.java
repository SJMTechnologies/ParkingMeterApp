package com.park.parkingmeterapp.parkmvp;

import com.google.android.gms.maps.GoogleMap;
import com.park.parkingmeterapp.model.MarkerDetail;

import java.util.List;

/**
 * Created by Jitesh Dalsaniya on 21-Dec-16.
 */

public interface ParkPresenter {

    List<MarkerDetail> loadMarkers(double latitude, double longitude);
    void addMarkers(GoogleMap mGoogleMap, List<MarkerDetail> markerList);
    void addMarkers(GoogleMap mGoogleMap, double latitude, double longitude, String address);
}
