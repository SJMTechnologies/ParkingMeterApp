package com.park.parkingmeterapp.model;

/**
 * Created by Jitesh Dalsaniya on 24-Nov-16.
 */

public class Country {

    public static final String COUNTRY_ID = "country_id";
    public static final String COUNTRY_NAME = "name";
    String id, name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
