package com.park.parkingmeterapp.model;

/**
 * Created by Jitesh Dalsaniya on 24-Nov-16.
 */

public class State {

    public static final String STATE_ZONE_ID = "zone_id";
    public static final String STATE_NAME = "name";
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
