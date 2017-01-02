package com.park.parkingmeterapp.model;

/**
 * Created by Jitesh Dalsaniya on 14-Dec-16.
 */

public class MarkerDetail {

    public static final String KEY_AREA = "area";
    public static final String KEY_POST = "post";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    String area, post, latitude, longitude;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
