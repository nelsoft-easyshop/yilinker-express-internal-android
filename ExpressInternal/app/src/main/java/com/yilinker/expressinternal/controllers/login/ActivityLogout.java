package com.yilinker.expressinternal.controllers.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;

import java.io.IOException;

/**
 * Created by J.Bautista
 */
public class ActivityLogout extends BaseActivity implements ResponseHandler{

    private static final int REQUEST_LOGOUT = 1000;

    private EditText etUsername;
    private EditText etPassword;
    private Button btnConfirm;

    private RelativeLayout rlProgress;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        initViews();

    }

    @Override
    protected void onPause() {
        super.onPause();

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
    }

    private void initViews(){

        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(this);

        //Set Actionbar
        setActionBarTitle("");
        hideMenuButton();

        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnConfirm:

                if(allowLogout()){

                    requestLogout();
                }
                else{

                    Toast.makeText(getApplicationContext(), getString(R.string.login_error_incomplete_fields), Toast.LENGTH_LONG).show();
                }

                break;

        }
    }

    private boolean allowLogout(){

        String strName = etUsername.getText().toString().trim();
        String strPassword = etPassword.getText().toString().trim();

        boolean allow = strName.length() > 0 & strPassword.length() > 0;

        return allow;
    }

    private void requestLogout(){

        rlProgress.setVisibility(View.VISIBLE);

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        Request request = RiderAPI.logout(REQUEST_LOGOUT, password, username, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_LOGOUT:

                ApplicationClass appClass = (ApplicationClass)BaseApplication.getInstance();

                appClass.logoutRider();

                //Stop location service
                appClass.stopLocationService();

                goToLogin();

                break;

        }

    }

    @Override
    protected void handleRefreshToken() {

        requestLogout();

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);

    }

    private void goToLogin(){

        Intent intent = new Intent(ActivityLogout.this, ActivityLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
