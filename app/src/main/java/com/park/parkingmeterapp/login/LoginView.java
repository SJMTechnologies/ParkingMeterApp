package com.park.parkingmeterapp.login;

/**
 * Created by Jitesh Dalsaniya on 28-Nov-16.
 */

public interface LoginView {

    String getUsername();
    String getPassword();

    void showUsernameError(int resId);
    void showPasswordError(int resId);
    void showSnackBar(String resId);
    void navigateToHome();
    void showProgressDialog();
    void hideProgressDialog();
    void showDialog(String msg);
}
