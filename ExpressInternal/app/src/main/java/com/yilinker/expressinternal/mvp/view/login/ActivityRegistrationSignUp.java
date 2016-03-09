package com.yilinker.expressinternal.mvp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.login.*;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.cashManagement.CashManagementPresenter;
import com.yilinker.expressinternal.mvp.presenter.login.RegistrationSignUpPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;

/**
 * Created by Patrick on 3/8/2016.
 */
public class ActivityRegistrationSignUp extends BaseFragmentActivity implements IActivityRegistrationSignUpView, View.OnClickListener{

    private final static int VERIFICATION_REQUEST_CODE = 2000;
    public final static String KEY_MOBILE_NUMBER = "mobile-number";
    public final static String KEY_VERIFICATION_CODE = "verification-code";

    EditText etMobileNumber;
    RegistrationSignUpPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){
            presenter = new RegistrationSignUpPresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        setContentView(R.layout.activity_registration_sign_up);

        initializeViews(null);
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
    public void initializeViews(View parent) {

        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        TextView tvSignIn = (TextView) findViewById(R.id.tvSignIn);

        btnSignUp.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void onSignUpClick() {

        Intent goToCodeVerification = new Intent(this,ActivityRegistrationVerificationCode.class);
        goToCodeVerification.putExtra(KEY_MOBILE_NUMBER,getFormatterMobileNumber());
        startActivityForResult(goToCodeVerification, VERIFICATION_REQUEST_CODE);
    }

    private String getFormatterMobileNumber(){

        String formatterNumber = String.format("%s%s",
                getString(R.string.registration_mobile_number_start),etMobileNumber.getText().toString());

        return formatterNumber;
    }

    @Override
    public void onSignInClick() {
        Intent goToLogIn = new Intent(this, com.yilinker.expressinternal.controllers.login.ActivityLogin.class);
        startActivity(goToLogIn);
    }

    private void goToCompleteSignUp(String verificationCode){

        Intent goToCompleteSignUp = new Intent(this, ActivityRegistrationCompleteSignUp.class);
        goToCompleteSignUp.putExtra(KEY_MOBILE_NUMBER,etMobileNumber.getText().toString());
        goToCompleteSignUp.putExtra(KEY_VERIFICATION_CODE, verificationCode);
        startActivity(goToCompleteSignUp);

    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSignUp:

                presenter.validateInput(etMobileNumber.getText().toString());
                break;

            case R.id.tvSignIn:
                onSignInClick();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case VERIFICATION_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    //TODO get verification code from data
                    String verificationCode = data.getStringExtra(KEY_VERIFICATION_CODE);
                    goToCompleteSignUp(verificationCode);
                }

                break;

            default:
                break;
        }
    }
}
