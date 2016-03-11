package com.yilinker.expressinternal.mvp.view.registration;

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
import com.yilinker.expressinternal.mvp.presenter.registration.RegistrationVerificationCodePresenter;
import com.yilinker.expressinternal.mvp.view.BaseActivity;

import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public class ActivityRegistrationVerificationCode extends BaseActivity implements IActivityRegistrationVerificationCodeView, View.OnClickListener {

    private String mobileNumber;
    private TextView tvErrorMessage;
    private EditText etCode;

    private View viewLoader;
    private Button btnVerify;

    private RegistrationVerificationCodePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_toolbar_registration);

        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){

            presenter = new RegistrationVerificationCodePresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        setContentView(R.layout.activity_registration_verification_code);
        initData();
        initializeViews(null);
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
    public void initializeViews(View parent) {
        TextView tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
        tvMobileNumber.setText(getFormatterMobileNumber());

        btnVerify =(Button) findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(this);

        TextView tvResendVerification = (TextView) findViewById(R.id.tvResendVerification);
        tvResendVerification.setOnClickListener(this);

        viewLoader = findViewById(R.id.viewLoader);
        etCode = (EditText) findViewById(R.id.etCode);
        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
    }

    private String getFormatterMobileNumber(){

        return String.format("%s%s",
                getString(R.string.registration_mobile_number_start), mobileNumber);
    }

    @Override
    public void handleGetVerificationCodeResponse(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoader(boolean isToShow) {
        viewLoader.setVisibility(isToShow? View.VISIBLE:View.GONE);
        if (isToShow){
            btnVerify.setText(getString(R.string.registration_verifying));
        }else {
            btnVerify.setText(getString(R.string.registration_verify));
        }

    }

    @Override
    public void showErrorMessage(boolean isToShow, String errorMessage) {

        tvErrorMessage.setVisibility(isToShow? View.VISIBLE: View.GONE);
        tvErrorMessage.setText(errorMessage);
    }

    @Override
    public void handleVerifyResponse(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//        goBackToSignUp();
        goToCompleteSignUp();
    }

    private void goBackToSignUp(){
        Intent intent = new Intent();
        intent.putExtra(ActivityRegistrationSignUp.KEY_VERIFICATION_CODE,etCode.getText().toString());
        intent.putExtra(ActivityRegistrationSignUp.KEY_MOBILE_NUMBER, mobileNumber);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void goToCompleteSignUp(){
        Intent intent = new Intent(this, ActivityRegistrationCompleteSignUp.class);
        intent.putExtra(ActivityRegistrationSignUp.KEY_VERIFICATION_CODE, etCode.getText().toString());
        intent.putExtra(ActivityRegistrationSignUp.KEY_MOBILE_NUMBER, mobileNumber);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);

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
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnVerify:
//                presenter.validateInput(etCode.getText().toString());
//                temp TODO to be remove
                handleVerifyResponse("verified");
                break;

            case R.id.tvResendVerification:
                Toast.makeText(getApplicationContext(),"Resend Verification",Toast.LENGTH_SHORT).show();
//                presenter.getVerificationCode();
                break;

            default:
                break;

        }
    }

}
