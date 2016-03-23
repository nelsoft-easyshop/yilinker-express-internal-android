package com.yilinker.expressinternal.mvp.presenter.registration;

import com.android.volley.Request;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.api.express.RegistrationApi;
import com.yilinker.core.model.Login;
import com.yilinker.core.model.OAuthentication;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.presenter.registration.IRegistrationSignUpPresenter;
import com.yilinker.expressinternal.mvp.view.registration.IActivityRegistrationSignUpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public class RegistrationSignUpPresenter extends RequestPresenter<Object,IActivityRegistrationSignUpView>
        implements IRegistrationSignUpPresenter {

    private final static int GET_VERIFICATION_CODE_REQUEST_CODE = 2000;
    private final static int LOGIN_FOR_VERIFICATION_REQUEST_CODE = 2001;
    private final static String VERIFICATION_CODE_REQUEST_TAG = "verification-code";
    private final static String LOGIN_FOR_VERIFICATION_REQUEST_TAG = "verification-login";

    private String mobileNumber;
    private String accessToken;
    private boolean isSameNumber = false;

    @Override
    protected void updateView() {
        view().onSignUpClick(true);
    }

    @Override
    public void validateInput(String mobileNumber, String savedMobileNumber) {

        if (mobileNumber.length()<10){
            view().showErrorMessage();
        }else{
            if (savedMobileNumber!=null){

                if (savedMobileNumber.equals(mobileNumber)){
                    isSameNumber = true;
                    requestGetToken();

                }else {
                    this.mobileNumber = String.format("0%s",mobileNumber);
                    isSameNumber  =false;
                    requestGetToken();
                }

            }else {
                isSameNumber = false;
                this.mobileNumber = String.format("0%s",mobileNumber);
                requestGetToken();
            }
        }
    }

    @Override
    public void onPause() {
        view().cancelRequests(getRequestTags());
    }

    private List<String> getRequestTags(){
        List<String> tags = new ArrayList<>();
        tags.add(VERIFICATION_CODE_REQUEST_TAG);
        tags.add(LOGIN_FOR_VERIFICATION_REQUEST_TAG);

        return tags;
    }

    private void requestVerificationCode(String mobileNumber, String accessToken){
        view().showLoader(true);
        Request request = RegistrationApi.getVerificationCode(GET_VERIFICATION_CODE_REQUEST_CODE, mobileNumber,accessToken, this, new ExpressErrorHandler(this,GET_VERIFICATION_CODE_REQUEST_CODE));
        request.setTag(VERIFICATION_CODE_REQUEST_TAG);
        view().addRequestToQueue(request);

    }

    private void requestGetToken(){

        view().showLoader(true);
        OAuthentication oAuth = new OAuthentication();
        oAuth.setClientId(APIConstant.OAUTH_VERIFICATION_CLIENT_ID);
        oAuth.setClientSecret(APIConstant.OAUTH_VERIFICATION_CLIENT_SECRET);
        oAuth.setGrantType(APIConstant.OAUTH_VERIFICATION_GRANT_TYPE);
        Request request = RiderAPI.loginForVerificationCode(LOGIN_FOR_VERIFICATION_REQUEST_CODE, oAuth,
                this, new ExpressErrorHandler(this,LOGIN_FOR_VERIFICATION_REQUEST_CODE));

        request.setTag(LOGIN_FOR_VERIFICATION_REQUEST_TAG);
        view().addRequestToQueue(request);
    }



    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case LOGIN_FOR_VERIFICATION_REQUEST_CODE:
                view().showLoader(false);
                Login login = (Login) object;
                accessToken = login.getAccess_token();
                view().setAccessToken(accessToken);
                if (isSameNumber){
                    view().onSignUpClick(false);
                }else {

//                    requestVerificationCode(mobileNumber, accessToken);
                    view().onSignUpClick(true);
                }
                break;

            case GET_VERIFICATION_CODE_REQUEST_CODE:
                view().showLoader(false);
                view().onSignUpClick(true);
                break;

            default:
                break;

        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
//        super.onFailed(requestCode, message);

        switch (requestCode){

            case LOGIN_FOR_VERIFICATION_REQUEST_CODE:
                view().showLoader(false);
                view().showValidationError(message);

                break;

            case GET_VERIFICATION_CODE_REQUEST_CODE:
                view().showLoader(false);
                view().showValidationError(message);
                break;

            default:
                break;

        }

     }

}
