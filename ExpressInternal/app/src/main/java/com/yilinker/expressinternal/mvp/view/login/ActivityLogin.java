package com.yilinker.expressinternal.mvp.view.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.dashboard.ActivityDashboard;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.login.LoginPresenter;

/**
 * Created by J.Bautista on 2/22/16.
 */
public class ActivityLogin extends Activity implements ILoginView, View.OnClickListener {

    private RelativeLayout rlProgress;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Reuse previous presenter if available
        if(savedInstanceState == null){

            presenter = new LoginPresenter(this);
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }


        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnLogin:

                login();
                break;

        }
    }

    @Override
    public void initViews() {

        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

        btnLogin.setOnClickListener(this);

        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void goToMainScreen() {

        Intent goToDashBoard = new Intent(ActivityLogin.this, ActivityDashboard.class);
        startActivity(goToDashBoard);
        finish();

    }

    @Override
    public void showErrorMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInvalidInputMessage() {

        showErrorMessage(getString(R.string.login_error_incomplete_fields));
    }

    @Override
    public void showLoader(boolean isShown) {

        int visibility = 0;

        if(isShown){

            visibility = View.VISIBLE;
        }
        else{

            visibility = View.GONE;
        }

        rlProgress.setVisibility(visibility);
    }


    private void login(){

        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        String userName = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        presenter.attemptLogin(userName, password);
    }
}
