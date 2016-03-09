package com.yilinker.expressinternal.mvp.view.login;

import android.content.Intent;
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
import com.yilinker.expressinternal.mvp.presenter.login.IRegistrationVerificationCodePresenter;
import com.yilinker.expressinternal.mvp.presenter.login.RegistrationSignUpPresenter;
import com.yilinker.expressinternal.mvp.presenter.login.RegistrationVerificationCodePresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;

import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public class ActivityRegistrationVerificationCode extends BaseFragmentActivity implements IActivityRegistrationVerificationCodeView, View.OnClickListener {

    private String mobileNumber;
    private TextView tvErrorMessage;
    private EditText etCode;
    private RelativeLayout rlProgress;

    private RegistrationVerificationCodePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){

            presenter = new RegistrationVerificationCodePresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        setContentView(R.layout.activity_registration_verification_code);
        initData();
        initializeViews();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    private void initData(){
        mobileNumber = getIntent().getStringExtra(ActivityRegistrationSignUp.KEY_MOBILE_NUMBER);
    }


    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
        presenter.getVerificationCode();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.onPause();
        presenter.unbindView();
    }


    @Override
    public void initializeViews() {
        TextView tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
        tvMobileNumber.setText(mobileNumber);

        Button btnVerify =(Button) findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(this);

        TextView tvResendVerification = (TextView) findViewById(R.id.tvResendVerification);
        tvResendVerification.setOnClickListener(this);

        etCode = (EditText) findViewById(R.id.etCode);
        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void handleGetVerificationCodeResponse(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoader(boolean isToShow) {
        rlProgress.setVisibility(isToShow? View.VISIBLE:View.GONE);
    }

    @Override
    public void showErrorMessage(boolean isToShow, String errorMessage) {

        tvErrorMessage.setVisibility(isToShow? View.VISIBLE: View.GONE);
        tvErrorMessage.setText(errorMessage);
    }

    @Override
    public void handleVerifyResponse(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        goBackToSignUp();
    }

    private void goBackToSignUp(){
        Intent intent = new Intent();
        intent.putExtra(ActivityRegistrationSignUp.KEY_VERIFICATION_CODE,etCode.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
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
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnVerify:
//                presenter.getVerificationCode();
//                temp
                handleVerifyResponse("my message");
                break;

            case R.id.tvResendVerification:
                presenter.validateInput(etCode.getText().toString());
                break;

            default:
                break;

        }
    }

}
