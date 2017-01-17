package com.park.parkingmeterapp.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.park.parkingmeterapp.ParkApp;
import com.park.parkingmeterapp.R;
import com.park.parkingmeterapp.retrofit.ApiService;
import com.park.parkingmeterapp.retrofit.RetroClient;
import com.park.parkingmeterapp.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImpl implements LoginPresenter {

    private static final String TAG = "LoginPresenterImpl";
    private LoginView loginView;
    private Context context;

    public LoginPresenterImpl(Context context, LoginView loginView) {
        this.loginView = loginView;
        this.context = context;
    }

    @Override
    public void onLoginClicked() {
        String username = loginView.getUsername();
        if (TextUtils.isEmpty(username)) {
            loginView.showUsernameError(R.string.please_enter_email);
            return;
        }

        String password = loginView.getPassword();
        if (TextUtils.isEmpty(password)) {
            loginView.showPasswordError(R.string.please_enter_password);
            return;
        }

        if (InternetConnection.checkConnection(context)) {
            loginView.showProgressDialog();
            ApiService api = RetroClient.getApiService();
            Log.e("getNotificationToken()", ParkApp.preferences.getNotificationToken() + "");
            if (ParkApp.preferences.getNotificationToken().trim().length() < 2) {
                ParkApp.preferences.setNotificationToken("123");
            }
            Call<String> call = api.doLogin(username, password, ParkApp.preferences.getNotificationToken());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    loginView.hideProgressDialog();
                    Log.e(TAG, "onResponse: " + response.body());
                    parseLoginResponse(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    loginView.hideProgressDialog();
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onFacebookLoggedIn() {

    }

    @Override
    public void onGoogleLoggedIn(String email, String fname, String lname) {
        if (InternetConnection.checkConnection(context)) {
            loginView.showProgressDialog();
            ApiService api = RetroClient.getApiService();
            Random random = new Random();
            int deviceToken = random.nextInt(25555555);
            Call<String> call = api.doSocialLogin(email, "", fname, lname, String.valueOf(deviceToken));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    loginView.hideProgressDialog();
                    Log.e(TAG, "onResponse onGoogleLoggedIn : " + response.body());
                    parseLoginResponse(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    loginView.hideProgressDialog();
                    Log.e(TAG, "onFailure onGoogleLoggedIn : " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onForgotPasswordClicked(String email) {
        if (InternetConnection.checkConnection(context)) {
            loginView.showProgressDialog();
            ApiService api = RetroClient.getApiService();
            Call<String> call = api.forgotPassword(email);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    loginView.hideProgressDialog();
                    Log.e(TAG, "onResponse: " + response.body());
                    parseForgotPasswordResponse(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    loginView.hideProgressDialog();
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }


    public void CallGetProfile() {
        if (InternetConnection.checkConnection(context)) {
            loginView.showProgressDialog();
            ApiService api = RetroClient.getApiService();
            Log.e("getAuthToken()", ParkApp.preferences.getAuthToken() + "");
            Call<String> call = api.getProfile(ParkApp.preferences.getAuthToken());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    loginView.hideProgressDialog();
                    Log.e(TAG, "onResponse: " + response.body());
                    parseGETProfileResponse(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    loginView.hideProgressDialog();
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void parseForgotPasswordResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject j = result.getJSONObject(0);
            String message = j.optString("message");
            loginView.showDialog(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseLoginResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject j = result.getJSONObject(0);
            String code = j.optString("code");
            String message = j.optString("message");
            if (code.equals("1")) {
                String authToken = j.optString("auth_token");
                ParkApp.preferences.setAuthToken(authToken);
                ParkApp.preferences.setUSER_EMAIL(loginView.getUsername());
                CallGetProfile();
            } else {
                loginView.showSnackBar(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseGETProfileResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result2 = jsonObject.getJSONObject("result");
            JSONObject result = result2.getJSONObject("rows");

            String code = result.optString("code");
            String message = result.optString("message");
            if (code.equals("1")) {
                JSONObject j = result.getJSONObject("0");
                ParkApp.preferences.setUSER_CUSTID(j.optString("customer_id"));
                ParkApp.preferences.setUSER_FNAME(j.optString("firstname"));
                ParkApp.preferences.setUSER_lNAME(j.optString("lastname"));
                ParkApp.preferences.setUSER_ADDRESS1(j.optString("address_1"));
                ParkApp.preferences.setUSER_ADDRESS2(j.optString("address_2"));
                ParkApp.preferences.setUSER_CITY(j.optString("city"));
                ParkApp.preferences.setUSER_STATE(j.optString("state_id"));
                ParkApp.preferences.setUSER_COUNTRY(j.optString("country_id"));
                ParkApp.preferences.setUSER_ZIP(j.optString("postcode"));
                ParkApp.preferences.setUSER_EMAIL(j.optString("email"));
                ParkApp.preferences.setUSER_TELEPHONE(j.optString("telephone"));
                ParkApp.preferences.setUSER_FAX(j.optString("fax"));
                ParkApp.preferences.setUSER_COMPANY(j.optString("company"));
                ParkApp.preferences.setUSER_NEWSLETER(j.optString("newsletter"));
                ParkApp.preferences.setUSER_BADGE(j.optString("badge"));

                loginView.navigateToHome();
            } else {
                loginView.showSnackBar(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
