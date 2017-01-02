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

        if(InternetConnection.checkConnection(context)){
            loginView.showProgressDialog();
            ApiService api = RetroClient.getApiService();
            Random random = new Random();
            int deviceToken = random.nextInt(25555555);
            Call<String> call = api.doLogin(username, password,String.valueOf(deviceToken));

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
        if(InternetConnection.checkConnection(context)){
            loginView.showProgressDialog();
            ApiService api = RetroClient.getApiService();
            Random random = new Random();
            int deviceToken = random.nextInt(25555555);
            Call<String> call = api.doSocialLogin(email,"",fname,lname,String.valueOf(deviceToken));
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
        if(InternetConnection.checkConnection(context)){
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

    private void parseForgotPasswordResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject j = result.getJSONObject(0);
            String message = j.optString("message");
            loginView.showDialog(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseLoginResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject j = result.getJSONObject(0);
            String code = j.optString("code");
            String message = j.optString("message");
            if(code.equals("1")){
                String authToken = j.optString("auth_token");
                ParkApp.preferences.setAuthToken(authToken);
                loginView.navigateToHome();
            } else {
                loginView.showSnackBar(message);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
