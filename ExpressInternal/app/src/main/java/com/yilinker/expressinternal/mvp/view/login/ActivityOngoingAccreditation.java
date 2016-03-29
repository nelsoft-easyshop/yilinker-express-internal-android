package com.yilinker.expressinternal.mvp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.login.*;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;

/**
 * Created by patrick-villanueva on 3/29/2016.
 */
public class ActivityOngoingAccreditation extends BaseFragmentActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_accreditation);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initializeViews(null);
    }

    @Override
    public void initializeViews(View parent) {

        Button btnGotIt = (Button) findViewById(R.id.btnGotIt);
        btnGotIt.setOnClickListener(this);

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnGotIt:
                Intent goToLogIn = new Intent(ActivityOngoingAccreditation.this, com.yilinker.expressinternal.controllers.login.ActivityLogin.class);
                goToLogIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goToLogIn);
                finish();
                break;

            default:

                break;

        }
    }
}
