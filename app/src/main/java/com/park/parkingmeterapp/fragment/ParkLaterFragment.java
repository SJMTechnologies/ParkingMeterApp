package com.park.parkingmeterapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.park.parkingmeterapp.ParkApp;
import com.park.parkingmeterapp.R;
import com.park.parkingmeterapp.activities.PurchaseTimeActivity;
import com.park.parkingmeterapp.adapter.PlaceArrayAdapter;
import com.park.parkingmeterapp.model.MarkerDetail;
import com.park.parkingmeterapp.parkmvp.ParkPresenterImpl;
import com.park.parkingmeterapp.parkmvp.ParkView;
import com.park.parkingmeterapp.retrofit.ApiService;
import com.park.parkingmeterapp.retrofit.RetroClient;
import com.park.parkingmeterapp.utils.InternetConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkLaterFragment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ParkView {

    public static final String KEY_IS_FROM_PARK_LATER = "IS_FROM_PARK_LATER";
    public static final String KEY_LATITUDE = "key_latitude";
    public static final String KEY_LONGITUDE = "key_longitude";
    public static final String KEY_AREA = "key_area";
    public static final String KEY_POST = "key_post";
    private static final String TAG = "ParkLaterFragment";
    private String strAddress = "";
    Location mLastLocation;
    double lat, lon;
    IntentFilter intentFilter;
    @BindView(R.id.txtAutoComplete)
    AutoCompleteTextView txtAutoComplete;
    boolean isFromParkLater;
    private GoogleMap mParkLaterGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Unbinder unbinder;
    public static List<MarkerDetail> markerDetailList;
    private ParkPresenterImpl parkPresenter;
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
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

    private boolean isFirstTime;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);

            Log.e(TAG, "Latitude and Longitude : " + place.getLatLng().latitude + " " + place.getLatLng().longitude);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txtAutoComplete.getWindowToken(), 0);
            if (isFromParkLater) {
                Log.e(TAG, "onResult: " + markerDetailList.size());
            }

//            if(markerDetailList != null){
//
//            } else {
//                Log.e(TAG, "onResult: markerDetailList null");
//            }

            places.release();
        }
    };

    public void addMarkers(GoogleMap mGoogleMap, List<MarkerDetail> markerList) {
        Log.e(TAG, "addMarkers: " + markerDetailList.size());
        for (MarkerDetail markerDetail : markerDetailList) {
            MarkerOptions markerOptions = new MarkerOptions();
            Log.e(TAG, "addMarkerToMap: markerDetail.getLatitude() " + markerDetail.getLatitude() + " " + markerDetail.getLongitude());
            LatLng position = new LatLng(Double.parseDouble(markerDetail.getLatitude()), Double.parseDouble(markerDetail.getLongitude()));
            markerOptions.position(position);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
            markerOptions.title(markerDetail.getPost());
            markerOptions.snippet(markerDetail.getArea());
            mGoogleMap.addMarker(markerOptions);
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(position, 15);
            mGoogleMap.animateCamera(cameraPosition);

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Log.e(" parkletar 1 lat|LNg",marker.getPosition().latitude + " " + marker.getPosition().longitude );
                    startActivity(marker.getPosition().latitude, marker.getPosition().longitude, marker.getSnippet(), marker.getTitle());
                }
            });
        }
    }

    public void loadMarkers(double latitude, double longitude) {

        if (InternetConnection.checkConnection(getActivity())) {
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
                        getMarkerJson(response.body());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "onFailure: loadMarkers " + t.getMessage());
                }
            });
        }
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
            } /*else {
                parkView.showToast(message);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "getMarkerJson: markerDetailList " + ParkLaterFragment.markerDetailList.size());
        addMarkers(mParkLaterGoogleMap, markerDetailList);
        return markerDetailList;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(i);
            assert item != null;
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    public static ParkLaterFragment newInstance(boolean isFromParkLater) {
        Log.e(TAG, "newInstance: isFromParkLater " + isFromParkLater);
        Bundle args = new Bundle();

        ParkLaterFragment fragment = new ParkLaterFragment();
        args.putBoolean(KEY_IS_FROM_PARK_LATER, isFromParkLater);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            Log.e("mGoogleApiClient", "onCreate mGoogleApiClient connect " + mGoogleApiClient.isConnecting());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park_later, container, false);

        unbinder = ButterKnife.bind(this, view);
        if (mGoogleApiClient == null) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Places.GEO_DATA_API)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parkPresenter = new ParkPresenterImpl(this, getActivity());
        markerDetailList = new ArrayList<>();
        intentFilter = new IntentFilter();
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        getActivity().registerReceiver(locationReceiver, intentFilter);
        if (InternetConnection.checkConnection(getActivity())) {
            SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.park_later_map);


            fm.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mParkLaterGoogleMap = googleMap;
                    CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom
                            (new LatLng(Double.parseDouble("36.840180"), Double.parseDouble("-75.978080")), 12);
                    mParkLaterGoogleMap.animateCamera(cameraPosition);
                    Log.e(TAG, "onMapReady: " + mParkLaterGoogleMap.isMyLocationEnabled());
                    if(!isFromParkLater){
                        double latitude = 36.840180;
                        double longitude = -75.978080;
                        LatLng latLng = new LatLng(latitude, longitude);
                        new ReverseGeocodingTask(getActivity()).execute(latLng);
                    } else {
                        loadMarkers(0.0,0.0);
                    }
                }
            });
        }

        Bundle bundle = getArguments();
        assert bundle != null;
        isFromParkLater = bundle.getBoolean(KEY_IS_FROM_PARK_LATER);
        txtAutoComplete.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                null, null);
        txtAutoComplete.setAdapter(mPlaceArrayAdapter);

        int visibility = isFromParkLater ? View.VISIBLE : View.GONE;
        Log.e(TAG, "onCreateView: visibility " + visibility);
        txtAutoComplete.setVisibility(visibility);

        return view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mPlaceArrayAdapter.setLatLngBound(latLngBounds);
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!isFromParkLater && isFirstTime) {
            if (mParkLaterGoogleMap != null) {
                mParkLaterGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                mParkLaterGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                isFirstTime = false;
                if (isAdded()) {
                    new ReverseGeocodingTask(getActivity()).execute(latLng);
                }
            }
        }
    }

    public LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southWest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 255);
        LatLng northEast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        Log.e(TAG, "toBounds: South West Bound " + southWest.toString());
        Log.e(TAG, "toBounds: North East Bound " + southWest.toString());
        Log.e(TAG, "toBounds: Current Location center " + center.toString());
        return new LatLngBounds(southWest, northEast);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void startActivity(double latitude, double longitude, String area, String post) {

        Intent intent = new Intent(getActivity(), PurchaseTimeActivity.class);
        Bundle bundle = new Bundle();
        Log.e("lat|lng",latitude + "  " + longitude);
        bundle.putString(KEY_LATITUDE,latitude+"");
        bundle.putString(KEY_LONGITUDE,longitude+"");
        bundle.putString(KEY_AREA,area);
        bundle.putString(KEY_POST,post);
        intent.putExtras(bundle);

        getActivity().startActivity(intent);
    }

    @Override
    public void showToast(String message) {
        Snackbar.make(txtAutoComplete, message, Snackbar.LENGTH_SHORT).show();
    }

    public class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;
        double latitude = 0.0, longitude = 0.0;

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(mContext);
            latitude = params[0].latitude;
            longitude = params[0].longitude;

            List<Address> addresses = null;
            String addressText = "";

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getAddressLine(1),
                        address.getCountryName());
            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            if (addressText != null && addressText.length() > 0) {
                Log.e(TAG, "onPostExecute: addressText " + addressText);
                strAddress = addressText;
                parkPresenter.addMarkers(mParkLaterGoogleMap,latitude,longitude,strAddress);
            }
        }
    }
}
