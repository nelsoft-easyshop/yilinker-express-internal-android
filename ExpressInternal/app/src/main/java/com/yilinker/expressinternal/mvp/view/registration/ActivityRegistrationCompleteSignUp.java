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
import com.yilinker.expressinternal.controllers.login.ActivityLogin;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.registration.RegistrationCompleteSignUpPresenter;
import com.yilinker.expressinternal.mvp.view.BaseActivity;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;

import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public class ActivityRegistrationCompleteSignUp extends BaseActivity
        implements IActivityRegistrationCompleteSignUpView, View.OnClickListener{

    private final static int TERMS_CONDITIONS_REQUEST_CODE = 200;

    private EditText etPassword;
    private EditText etConfirmPassword;
    private RelativeLayout rlProgress;
    private View viewLoader;
    private Button btnSignUp;

    private RegistrationCompleteSignUpPresenter presenter;

    private String mobileNumber, verificationCode;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setActionBarLayout(R.layout.layout_toolbar_registration);
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){

            presenter = new RegistrationCompleteSignUpPresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        initData();

        setContentView(R.layout.activity_registration_complete_sign_up);
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
        presenter.onPause();
        presenter.unbindView();
    }

    private void initData(){
        mobileNumber = getIntent().getStringExtra(ActivityRegistrationSignUp.KEY_MOBILE_NUMBER);
        verificationCode = getIntent().getStringExtra(ActivityRegistrationSignUp.KEY_VERIFICATION_CODE);
        accessToken = getIntent().getStringExtra(ActivityRegistrationSignUp.KEY_ACCESS_TOKEN);
    }

    @Override
    public void initializeViews(View parent) {

        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etPassword = (EditText) findViewById(R.id.etPassword);
        viewLoader = findViewById(R.id.viewLoader);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        TextView tvTermsAndConditions = (TextView) findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setOnClickListener(this);

        EditText etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etMobileNumber.setText(String.format("%s%s",
                getString(R.string.registration_mobile_number_start),mobileNumber));

    }

    private String getFormatterMobileNumber(){

        return String.format("0%s",mobileNumber);
    }


    @Override
    public void addRequest(Request request) {
        addRequestToQueue(request);
    }


    @Override
    public void showLoader(boolean isToShow) {
        viewLoader.setVisibility(isToShow? View.VISIBLE: View.GONE);
        if (isToShow){
            btnSignUp.setText(getString(R.string.login_completing_sign_up));
        }else{
            btnSignUp.setText(getString(R.string.login_complete_sign_up));
        }

    }

    @Override
    public void showErrorMessage(String errorMessage) {

        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidationError(int errorCode) {
        String errorMessage = "";

        switch (errorCode){
            case 1:
                errorMessage = getString(R.string.registration_password_required);
                break;

            case 2:
                errorMessage = getString(R.string.registration_coonfirm_password_not_match);
                break;

            case 3:
                errorMessage = getString(R.string.registration_password_less_than_min);

            default:
                break;
        }

        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleSignUpResponse(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();

        Intent goToLogin = new Intent(ActivityRegistrationCompleteSignUp.this, ActivityLogin.class);
        goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToLogin);

        finish();    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.tvTermsAndConditions:
                Intent goToTermsAndConditions = new Intent(this, ActivityRegistrationTermsAndConditions.class);
                startActivityForResult(goToTermsAndConditions,TERMS_CONDITIONS_REQUEST_CODE);

                break;

            case R.id.btnSignUp:
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                presenter.validateInputs(getFormatterMobileNumber(),password, confirmPassword,accessToken);
                break;

            default:
                break;
        }
    }
}
