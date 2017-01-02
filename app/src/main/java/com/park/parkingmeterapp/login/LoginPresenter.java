package com.park.parkingmeterapp.login;

/**
 * Created by Jitesh Dalsaniya on 28-Nov-16.
 */

public interface LoginPresenter {

    void onLoginClicked();
    void onFacebookLoggedIn();
    void onGoogleLoggedIn(String email, String fname, String lname);
    void onForgotPasswordClicked(String email);
}
