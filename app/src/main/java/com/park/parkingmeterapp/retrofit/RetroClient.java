package com.park.parkingmeterapp.retrofit;

import retrofit2.Retrofit;

/**
 * Created by Jitesh Dalsaniya on 24-Nov-16.
 */

public class RetroClient {

    private static final String ROOT_URL = "http://54.162.93.65/services/";

    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(new ToStringConverterFactory())
                .build();
    }

    public static ApiService getApiService(){
        return getRetrofitInstance().create(ApiService.class);
    }
}
