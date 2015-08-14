package com.yilinker.expressinternal.controllers.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.dashboard.ActivityDashboard;

public class ActivityLogin extends Activity implements View.OnClickListener {

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
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

}