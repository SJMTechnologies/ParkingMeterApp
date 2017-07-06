package com.park.parkingmeterapp.register;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.park.parkingmeterapp.ParkApp;
import com.park.parkingmeterapp.R;
import com.park.parkingmeterapp.model.Register;
import com.park.parkingmeterapp.retrofit.ApiService;
import com.park.parkingmeterapp.retrofit.RetroClient;
import com.park.parkingmeterapp.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenterImpl implements RegisterPresenter {

    private static final String TAG = "RegisterPresenterImpl";
    private RegisterView registerView;
    private Context context;

    public RegisterPresenterImpl(Context context, RegisterView registerView) {
        this.registerView = registerView;
        this.context = context;
    }

    @Override
    public void onRegisteredClicked() {
        boolean isUpdate = ParkApp.preferences.getIsUpdate();
        Register register = registerView.getRegisterData();
        if (TextUtils.isEmpty(register.getFirstName())) {
            registerView.showError(R.string.please_enter_first_name);
            return;
        }

        if (TextUtils.isEmpty(register.getLastName())) {
            registerView.showError(R.string.please_enter_last_name);
            return;
        }

        if (TextUtils.isEmpty(register.getEmail())) {
            registerView.showError(R.string.please_email);
        } else {
            Log.e(TAG, "onRegisteredClicked: Email Validation IN " + Patterns.EMAIL_ADDRESS.matcher(register.getEmail()).matches());
            if (!Patterns.EMAIL_ADDRESS.matcher(register.getEmail()).matches()) {
                registerView.showError(R.string.valid_email);
                return;
            }
        }

        if (TextUtils.isEmpty(register.getTelephone())) {
            registerView.showError(R.string.please_enter_mobile);
            return;
        }

        if (TextUtils.isEmpty(register.getAddressOne())) {
            registerView.showError(R.string.please_enter_address);
            return;
        }

        if (TextUtils.isEmpty(register.getCity())) {
            registerView.showError(R.string.please_enter_city);
            return;
        }

        if (TextUtils.isEmpty(register.getPostalCode())) {
            registerView.showError(R.string.please_enter_postal_code);
            return;
        }

        if (!isUpdate) {
            if (TextUtils.isEmpty(register.getPassword())) {
                registerView.showError(R.string.please_enter_password);
                return;
            } else {
                String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
                if (!register.getPassword().matches(PASSWORD_PATTERN)) {
                    registerView.showError(R.string.password_message);
                    return;
                }
            }

            if (TextUtils.isEmpty(register.getConfirmPassword())) {
                registerView.showError(R.string.please_enter_confirm_password);
                return;
            }

            if (!TextUtils.isEmpty(register.getPassword()) && !TextUtils.isEmpty(register.getConfirmPassword()) &&
                    !register.getPassword().equals(register.getConfirmPassword())) {
                registerView.showError(R.string.password_same);
                return;
            }
        }

        if (InternetConnection.checkConnection(context)) {
            registerView.showProgressDialog();
            ApiService api = RetroClient.getApiService();
            String phone = register.getTelephone();
            String p = phone.replace("(", "").replace(" ", "").replace(")", "").replace("-", "");
            Log.e(TAG, "onRegisteredClicked: getSubscribe " + register.isSubscribe());
            Call<String> call;
            Log.e(TAG, "onRegisteredClicked: isUpdate " + isUpdate);
            if (isUpdate) {
                String authToken = ParkApp.preferences.getAuthToken();
                call = api.updateProfile(authToken, register.getEmail().trim(), register.getFirstName(),
                        register.getLastName(), register.getBusinessName(), register.getAddressOne(),
                        register.getAddressTwo(), register.getCity(), register.getRegionOrState(),
                        register.getPostalCode(), register.getCountry(), p,
                        register.getFax(), String.valueOf(register.isSubscribe()));
            } else {
                call = api.doSignUp(register.getEmail().trim(), register.getPassword(), register.getFirstName(),
                        register.getLastName(), register.getBusinessName(), register.getAddressOne(),
                        register.getAddressTwo(), register.getCity(), register.getRegionOrState(),
                        register.getPostalCode(), register.getCountry(), p,
                        register.getFax(), String.valueOf(register.isSubscribe()));


            }
            Log.e("call", call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    registerView.hideProgressDialog();
                    Log.e(TAG, "onResponse: Error " + response.message());
                    Log.e(TAG, "onResponse: body " + response.body());
                    parseJson(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    registerView.hideProgressDialog();
                }
            });
        } else {
            registerView.showError(R.string.connect_internet);
        }
    }

    @Override
    public void setDataForUpdate() {
        if (InternetConnection.checkConnection(context)) {
            registerView.showProgressDialog();
            String authToken = ParkApp.preferences.getAuthToken();
            ApiService api = RetroClient.getApiService();
            Call<String> call = api.getProfile(authToken);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    registerView.hideProgressDialog();
                    Log.e(TAG, "onResponse: setDataForUpdate " + response.body());
                    getProfileFromJson(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "onFailure: setDataForUpdate" + t.getMessage());
                }
            });
        } else {
            registerView.showError(R.string.connect_internet);
        }
    }

    private void getProfileFromJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject rows = result.getJSONObject("rows");
            String code = rows.optString("code");
            if (code.equals("1")) {
                JSONObject j = rows.getJSONObject("0");
                Log.e(TAG, "getProfileFromJson: j " + j.toString());
                Register register = new Register();
                register.setFirstName(j.optString("firstname"));
                register.setLastName(j.optString("lastname"));
                register.setAddressOne(j.optString("address_1"));
                register.setAddressTwo(j.optString("address_2"));
                register.setCity(j.optString("city"));
                register.setRegionOrState(j.optString("state_id"));
                register.setCountry(j.optString("country_id"));
                register.setPostalCode(j.optString("postcode"));
                register.setEmail(j.optString("email"));
                register.setTelephone(j.optString("telephone"));
                register.setFax(j.optString("fax"));
                register.setBusinessName(j.optString("company"));
                if (j.optString("newsletter").equals("1")) {
                    register.setSubscribe(true);
                } else {
                    register.setSubscribe(false);
                }
                Log.e(TAG, "getProfileFromJson: register.isSubscribe() " + register.isSubscribe());
                registerView.setUpdateData(register);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJson(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("result");
                JSONObject result = array.getJSONObject(0);
                String code = result.optString("code");
                String msg = result.optString("message");
                Log.e(TAG, "parseJson: response " + jsonObject.toString());
                Log.e(TAG, "parseJson: response code " + code);
                if (code.equals("1")) {
                    registerView.showDialog(msg);
                } else {
                    registerView.showError(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "parseJson: response Null");
        }
    }

}
