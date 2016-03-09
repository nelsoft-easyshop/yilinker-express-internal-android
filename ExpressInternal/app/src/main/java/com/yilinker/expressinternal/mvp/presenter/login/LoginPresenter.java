package com.yilinker.expressinternal.mvp.presenter.login;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.OAuthentication;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.mvp.model.Login;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.login.ActivityLogin;
import com.yilinker.expressinternal.mvp.view.login.ILoginView;

/**
 * Created by J.Bautista on 2/19/16.
 */
public class LoginPresenter extends BasePresenter<Object, ActivityLogin> implements ILoginPresenter, ResponseHandler {

    private static final int REQUEST_LOGIN = 1000;
    private static final int REQUEST_VERIFY_RIDER = 1001;

    private ILoginView view;

    private ApplicationClass applicationClass;
    private RequestQueue requestQueue;

    private Login login;

    public LoginPresenter(Context context){

        this.applicationClass = (ApplicationClass)context;
        this.requestQueue = applicationClass.getRequestQueue();
        this.view = view();

    }

    @Override
    public void attemptLogin(String username, String password) {

        view.showLoader();

        boolean toContinue = hasEnteredValidInputs(username, password);

        if(toContinue){

            requestLogin(username, password);
        }
        else{

            view.showInvalidInputMessage();
            view.hideLoader();
        }

    }

    @Override
    public boolean hasEnteredValidInputs(String username, String password) {

        String strName = username.trim();
        String strPassword = password.trim();

        boolean isValid = strName.length() > 0 & strPassword.length() > 0;

        return isValid;
    }

    @Override
    public void saveTokens() {

        ApplicationClass appClass = (ApplicationClass) ApplicationClass.getInstance();
        appClass.saveAccessToken(login.getAccessToken());
        appClass.saveRefreshToken(login.getRefreshToken());
    }

    @Override
    public void requestLogin(String username, String password) {

        OAuthentication oAuth = new OAuthentication();
        oAuth.setClientId(applicationClass.getString(R.string.client_id));
        oAuth.setGrantType(APIConstant.OAUTH_GRANT_TYPE_PASSWORD);
        oAuth.setClientSecret(applicationClass.getString(R.string.client_secret));
        oAuth.setPassword(password);
        oAuth.setUsername(username);

        Request request = RiderAPI.loginByUsername(REQUEST_LOGIN, oAuth, applicationClass.getCurrentLocale(), this);
        requestQueue.add(request);

    }

    @Override
    public void requestVerifyRider() {

        Request request = RiderAPI.verifyRider(REQUEST_VERIFY_RIDER, this);
        requestQueue.add(request);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        switch (requestCode){

            case REQUEST_LOGIN:

                //Get the tokens from the response
                login = new Login((com.yilinker.core.model.Login)object);

                requestVerifyRider();
                break;

            case REQUEST_VERIFY_RIDER:

                saveTokens();
                view.goToMainScreen();

                break;

        }
    }

    @Override
    public void onFailed(int requestCode, String message) {

        switch (requestCode){

            case REQUEST_LOGIN:

                view.showErrorMessage(message);
                break;

            case REQUEST_VERIFY_RIDER:

                view.showErrorMessage(message);
                break;

        }

        view.hideLoader();
    }

    @Override
    protected void updateView() {


    }
}
