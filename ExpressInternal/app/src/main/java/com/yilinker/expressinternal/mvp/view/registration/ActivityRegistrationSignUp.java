package com.yilinker.expressinternal.mvp.view.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.registration.RegistrationSignUpPresenter;
import com.yilinker.expressinternal.mvp.view.BaseActivity;
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

        //Set the layout of the actionbar
//        setActionBarLayout(R.layout.layout_toolbar_registration);

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


//        /***set action bar title and background color*/
//        setActionBarTitle(getString(R.string.registration_create_account));
//        setActionBarBackgroundColor(R.color.marigold);
    }

    @Override
    public void onSignUpClick() {

        Intent goToCodeVerification = new Intent(this,ActivityRegistrationVerificationCode.class);
        goToCodeVerification.putExtra(KEY_MOBILE_NUMBER,etMobileNumber.getText().toString());
        startActivity(goToCodeVerification);
        overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
    }


    @Override
    public void onSignInClick() {

        Intent goToLogIn = new Intent(this, com.yilinker.expressinternal.controllers.login.ActivityLogin.class);
        startActivity(goToLogIn);
        finish();
    }


    @Override
    public void showErrorMessage() {

        Toast.makeText(getApplicationContext(),getString(R.string.login_error_mobile_required),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
//        super.onClick(v);

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
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void showLoader(boolean isShown) {

    }
}
