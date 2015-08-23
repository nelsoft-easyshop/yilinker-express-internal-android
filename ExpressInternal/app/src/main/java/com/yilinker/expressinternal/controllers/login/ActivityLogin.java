package com.yilinker.expressinternal.controllers.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yilinker.core.api.UserApi;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.dashboard.ActivityDashboard;

public class ActivityLogin extends Activity implements View.OnClickListener, ResponseHandler {

    private static final int REQUEST_LOGIN = 1000;

    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        goToDashboard();

    }

    @Override
    public void onFailed(int requestCode, String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                Intent goToDashBoard = new Intent(ActivityLogin.this, ActivityDashboard.class);
                startActivity(goToDashBoard);
                break;
        }
    }

    private void initViews(){

        btnLogin = (Button) findViewById(R.id.btnLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);

        btnLogin.setOnClickListener(this);

    }

    private void requestLogin(){

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        UserApi.login(REQUEST_LOGIN,"password", username, password, this);
    }

    private void goToDashboard(){

        Intent goToDashBoard = new Intent(ActivityLogin.this, ActivityDashboard.class);
        startActivity(goToDashBoard);
    }

}