package com.park.parkingmeterapp.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("web-services.php?web_method=getCountry")
    Call<String> getCountry();

    @GET("web-services.php?web_method=getState")
    Call<String> getStates(@Query("country_id") String id);

    @GET("web-services.php?web_method=login")
    Call<String> doLogin(@Query("email") String email, @Query("password") String password, @Query("device_token") String deviceToken);

    @GET("web-services.php?web_method=getProfile")
    Call<String> getProfile(@Query("auth_token") String authToken);

    @GET("web-services.php?web_method=logout")
    Call<String> logOut(@Query("auth_token") String authToken);

    @GET("web-services.php?web_method=forgotPassword")
    Call<String> forgotPassword(@Query("email") String email);

    @GET("web-services.php?web_method=updatePreference")
    Call<String> updatePref(@Query("auth_token") String authToken, @Query("radius") String radius,
                            @Query("auth_transaction") String auth_transaction);

    @GET("web-services.php?web_method=getPreference")
    Call<String> getPref(@Query("auth_token") String authToken);

    @GET("web-services.php?web_method=social_user_signup")
    Call<String> doSocialLogin(@Query("email") String email, @Query("password") String password,
                               @Query("fname") String fname, @Query("lname") String lname,
                               @Query("device_token") String deviceToken);

    @GET("web-services.php?web_method=signup")
    Call<String> doSignUp(@Query("email") String email, @Query("password") String password,
                          @Query("fname") String fname, @Query("lname") String lname,
                          @Query("business_name") String businessName, @Query("address") String address,
                          @Query("address2") String address2, @Query("city") String city,
                          @Query("state") String state, @Query("zip") String zip,
                          @Query("country") String country, @Query("phone") String phone,
                          @Query("fax") String fax, @Query("subscribe") String subscribe);

    @GET("web-services.php?web_method=updateProfile")
    Call<String> updateProfile(@Query("auth_token") String auth_token, @Query("email") String email,
                               @Query("fname") String fname, @Query("lname") String lname,
                               @Query("business_name") String businessName, @Query("address") String address,
                               @Query("address2") String address2, @Query("city") String city,
                               @Query("state") String state, @Query("zip") String zip,
                               @Query("country") String country, @Query("phone") String phone,
                               @Query("fax") String fax, @Query("subscribe") String subscribe);

    @GET("web-services.php?web_method=getMeters")
    Call<String> getMarkerDetails(@Query("auth_token") String authToken,
                                  @Query("latitude") String latitude,
                                  @Query("longitude") String longitude);

    @GET("web-services.php?web_method=checkAvailability")
    Call<String> getAvailability(@Query("auth_token") String authToken,
                                 @Query("selected_date") String seldate,
                                 @Query("selected_time") String selTIme,
                                 @Query("post") String post);
}
