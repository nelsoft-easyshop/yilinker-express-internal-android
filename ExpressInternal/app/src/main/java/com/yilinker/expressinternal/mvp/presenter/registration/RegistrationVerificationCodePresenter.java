package com.yilinker.expressinternal.mvp.presenter.registration;

import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.presenter.registration.IRegistrationVerificationCodePresenter;
import com.yilinker.expressinternal.mvp.view.registration.IActivityRegistrationVerificationCodeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 3/8/2016.
 */
public class RegistrationVerificationCodePresenter extends RequestPresenter<Object, IActivityRegistrationVerificationCodeView> implements
        IRegistrationVerificationCodePresenter, ResponseHandler{

    private final static int GET_VERIFICATION_REQUEST_CODE = 2000;
    private final static int VERIFY_CODE_REQUEST_CODE = 2001;

    private final static String GET_VERIFICATION_REQUEST_TAG = "get-verification";
    private final static String VERIFY_CODE_REQUEST_TAG = "verify-code";

    private String[] request_tags = {GET_VERIFICATION_REQUEST_TAG, VERIFY_CODE_REQUEST_TAG};

    @Override
    protected void updateView() {
    }


    @Override
    public void getVerificationCode() {
//        view().showLoader(true);
        requestVerificationCode();
    }

    @Override
    public void validateInput(String inputCode) {
        view().showErrorMessage(false,"");

        if (inputCode.length()<1){
            view().showErrorMessage(true,"Code is empty");

        }else {
            requestVerifyCode(inputCode);
        }
    }

    @Override
    public void onPause() {
        //TODO cancel request here
//        view().cancelRequest(getRequestTags());
    }

    private List<String> getRequestTags(){
        List<String> lists = new ArrayList<>();
        for (String item : request_tags){
            lists.add(item);
        }
        return lists;
    }


    private void requestVerificationCode(){
        //TODO Add api call
//        view().addRequest();
    }

    private void requestVerifyCode(String input){
        //TODO Add Api call here
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){
            case GET_VERIFICATION_REQUEST_CODE:
                view().showLoader(false);
                view().handleGetVerificationCodeResponse(object.toString());
                break;

            case VERIFY_CODE_REQUEST_CODE:
                view().showLoader(false);
                view().handleVerifyResponse(object.toString());
                break;

            default:
                break;

        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode) {
            case GET_VERIFICATION_REQUEST_CODE:
                view().showLoader(false);
                view().handleGetVerificationCodeResponse(message);
                break;

            case VERIFY_CODE_REQUEST_CODE:
                view().showLoader(false);
                view().showErrorMessage(true,message);
                break;
            default:
                break;
        }
    }
}
