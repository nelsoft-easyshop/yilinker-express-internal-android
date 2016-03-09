package com.yilinker.expressinternal.mvp.view.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.login.IActivityRegistrationCompleteSignUpView;
import com.yilinker.expressinternal.mvp.presenter.login.RegistrationCompleteSignUpPresenter;
import com.yilinker.expressinternal.mvp.presenter.login.RegistrationVerificationCodePresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;

import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public class ActivityRegistrationCompleteSignUp extends BaseFragmentActivity
        implements IActivityRegistrationCompleteSignUpView, View.OnClickListener{

    private EditText etPassword;
    private EditText etConfirmPassword;
    private RelativeLayout rlProgress;

    private RegistrationCompleteSignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){

            presenter = new RegistrationCompleteSignUpPresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        setContentView(R.layout.activity_registration_complete_sign_up);
        initializeViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
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
    public void initializeViews() {

        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etPassword = (EditText) findViewById(R.id.etPassword);
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        TextView tvTermsAndConditions = (TextView) findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setOnClickListener(this);

        rlProgress.setVisibility(View.GONE);

    }

    @Override
    public void addRequest(Request request) {
        addRequestToQueue(request);
    }

    @Override
    public void cancelRequest(List<String> requestTags) {
        cancelRequests(requestTags);
    }

    @Override
    public void showLoader(boolean isToShow) {
        rlProgress.setVisibility(isToShow? View.VISIBLE: View.GONE);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleSignUpResponse(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tvTermsAndConditions:
                //TODO show terms and conditions page
                break;

            case R.id.btnSignUp:
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                presenter.validateInputs(password, confirmPassword);
                break;

            default:
                break;
        }
    }
}
