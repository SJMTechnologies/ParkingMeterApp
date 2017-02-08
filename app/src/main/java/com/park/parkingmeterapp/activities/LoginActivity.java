package com.park.parkingmeterapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.park.parkingmeterapp.ParkApp;
import com.park.parkingmeterapp.R;
import com.park.parkingmeterapp.login.LoginPresenterImpl;
import com.park.parkingmeterapp.login.LoginView;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LoginView {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 0x1;

    BaseAnimatorSet bas_in;
    BaseAnimatorSet bas_out;
    //email
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.txtNeedAnAccount)
    TextView txtNeedAnAccount;
    @BindView(R.id.txtForgotPassword)
    TextView txtForgotPassword;
    @BindView(R.id.btnFacebook)
    ImageView btnFacebook;
    @BindView(R.id.btnGooglePlus)
    ImageView btnGooglePlus;
    private ProgressDialog pd;

//    private Databasehelper db;
    private CallbackManager mCallBackManager;
    private LoginManager loginManager;
    private GoogleApiClient mGoogleApiClient;
    private LoginPresenterImpl loginPresenterImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        printKeyHash();
        bas_in = new FlipVerticalSwingEnter();
        bas_out = new FadeExit();
//        pref = getSharedPreferences(Constant.PREF_NAME, 0);
//        boolean isLoggedIn = pref.getBoolean(Constant.USER_LOGIN, false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        if (isLoggedIn) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        }

        loginPresenterImpl = new LoginPresenterImpl(LoginActivity.this, this);
       // ParkApp.preferences.clearPref();
        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Firbase id login", "Refreshed token: " + refreshedToken);
            ParkApp.preferences.setNotificationToken(refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

       if(ParkApp.preferences.getAuthToken().length() > 0){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            LoginActivity.this.finish();
        }else
        {
            ParkApp.preferences.clearPref();
            try {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("Firbase id login", "Refreshed token: " + refreshedToken);
                ParkApp.preferences.setNotificationToken(refreshedToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    this.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.txtNeedAnAccount)
    public void onRegister() {
        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.txtForgotPassword)
    public void onForgotPassword() {
        dialog();
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {
        loginPresenterImpl.onLoginClicked();
    }

    @OnClick(R.id.btnFacebook)
    public void onFacebookPressed() {
        Log.e(TAG, "onFacebookPressed: ");
        facebookLogin();
    }

    @OnClick(R.id.txtNeedAnAccount)
    public void onNeedAccountPressed() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void facebookLogin() {
        mCallBackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.logOut();
        loginManager = LoginManager.getInstance();
//        Log.e(TAG, "facebookLogin: " + loginManager.getLoginBehavior().toString());
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));
        loginManager.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Success + " + loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e(TAG, "onCompleted: Facebook Logged In ... " + object.toString());
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        loginManager.logOut();
                        facebookLogin();
                    }
                }
            }
        });
    }

    @OnClick(R.id.btnGooglePlus)
    void signInWithGooglePlus() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        if (mCallBackManager != null) {
            mCallBackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("name", acct.getDisplayName());
//            startActivity(intent);
            Log.e(TAG, "handleSignInResult: acct.getGivenName() " + acct.getGivenName() + " email " + acct.getEmail() + " acct.getGivenName() " + acct.getFamilyName());
            loginPresenterImpl.onGoogleLoggedIn(acct.getEmail(), acct.getGivenName(), acct.getFamilyName());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public String getUsername() {
        return etEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public void showUsernameError(int resId) {
        etEmail.setError(getString(resId));
        etEmail.requestFocus();
    }

    @Override
    public void showPasswordError(int resId) {
        etPassword.setError(getString(resId));
        etPassword.requestFocus();
    }

    @Override
    public void showSnackBar(String resId) {
        Snackbar.make(btnLogin, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    @Override
    public void showProgressDialog() {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage(getString(R.string.please_wait));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void showDialog(String msg) {
        showSuccessDialog(msg);
    }

    private void showSuccessDialog(String msg) {

        Log.e(TAG, "showSuccessDialog: ");
        final MaterialDialog dialog = new MaterialDialog(LoginActivity.this);
        OnBtnClickL onBtnClickL1 = new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        };

        OnBtnClickL onBtnClickL2 = new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
            }
        };

        OnBtnClickL[] clickLs = new OnBtnClickL[]{onBtnClickL2, onBtnClickL1};
        dialog.setOnBtnClickL(clickLs);
        dialog.isTitleShow(false)
                .content(msg)
                .btnText("Okay", "")
                .showAnim(bas_in)
                .dismissAnim(bas_out)
                .show();
    }

    public void dialog() {
        // Creating alert Dialog with one Button
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Forgot Password");

        // Setting Dialog Message
        alertDialog.setMessage("Enter your email");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setView(input);
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        String email = input.getText().toString();
                        loginPresenterImpl.onForgotPasswordClicked(email);
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }
}
