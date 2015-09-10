package com.yilinker.expressinternal.controllers.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.Login;
import com.yilinker.core.model.OAuthentication;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.controllers.dashboard.ActivityDashboard;

public class ActivityLogin extends Activity implements View.OnClickListener, ResponseHandler {

    private static final int REQUEST_LOGIN = 1000;

    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private RelativeLayout rlProgress;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ApplicationClass app = (ApplicationClass)ApplicationClass.getInstance();

        if(app.isLoggedIn()){

            goToDashboard();
        }

        requestQueue = app.getRequestQueue();

        initViews();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        Login login = (Login) object;

        BaseApplication app = ApplicationClass.getInstance();
        app.saveAccessToken(login.getAccess_token());
        app.saveRefreshToken(login.getRefresh_token());

        goToDashboard();

        rlProgress.setVisibility(View.GONE);

    }

    @Override
    public void onFailed(int requestCode, String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnLogin:

                if(allowLogin()) {
                    requestLogin();
                }
                else{

                    Toast.makeText(getApplicationContext(), getString(R.string.login_error_incomplete_fields), Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private void initViews(){

        btnLogin = (Button) findViewById(R.id.btnLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

        btnLogin.setOnClickListener(this);

        rlProgress.setVisibility(View.GONE);

    }

    private void requestLogin(){

        rlProgress.setVisibility(View.VISIBLE);

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        OAuthentication oAuth = new OAuthentication();
        oAuth.setClientId(APIConstant.OAUTH_CLIENT_ID);
        oAuth.setGrantType(APIConstant.OAUTH_GRANT_TYPE_PASSWORD);
        oAuth.setClientSecret(APIConstant.OAUTH_CLIENT_SECRET);
        oAuth.setPassword(password);
        oAuth.setUsername(username);

        Request request = RiderAPI.loginByUsername(REQUEST_LOGIN, oAuth, this);
        requestQueue.add(request);
    }

    private void goToDashboard(){

        Intent goToDashBoard = new Intent(ActivityLogin.this, ActivityDashboard.class);
        startActivity(goToDashBoard);
        finish();
    }

    private boolean allowLogin(){

        String strName = etUsername.getText().toString().trim();
        String strPassword = etPassword.getText().toString().trim();

        boolean allow = strName.length() > 0 & strPassword.length() > 0;

        return allow;
    }

}