package com.park.parkingmeterapp.register;

import com.park.parkingmeterapp.model.Register;

public interface RegisterView {
    Register getRegisterData();

    void showError(int resId);
    void showError(String msg);
    void startActivity();
    void showDialog(String msg);
    void showProgressDialog();
    void hideProgressDialog();
    void setUpdateData(Register register);
}
