package com.yilinker.expressinternal.mvp.presenter.registration;

import android.os.CountDownTimer;

import com.android.volley.Request;
import com.yilinker.core.api.express.RegistrationApi;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.registration.IActivityRegistrationVerificationCodeView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private boolean isTimerFinished = true;
    private String minutes = "00";
    private String seconds = "00";
    private long remainingTime = 60000;
    private static final String FORMAT2 = "%02d";

    private CountDownTimer countDownTimer;

    @Override
    protected void updateView() {
    }

    private void setCountDownTimer(long remainingTime){

        countDownTimer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                minutes = String.format(FORMAT2, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
                seconds = String.format(FORMAT2, TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                isTimerFinished = false;
                view().setRemainingTime(minutes+":"+seconds);

            }

            @Override
            public void onFinish() {
                isTimerFinished = true;
                view().setRemainingTime("0");
                view().saveCurrentTime(null);
                setCountDownTimer(60000);
            }

        };

    }

    /****method to start the timer*/
    public void startTimer(){
        countDownTimer.start();
    }

    public void stopTimer(){
        isTimerFinished = true;

        if (countDownTimer!=null){

            countDownTimer.cancel();
        }

    }

    @Override
    public void validateInput(String inputCode, String mobileNumber, String accessToken) {
        view().showErrorMessage(false,"");

        if (inputCode.length()<1){
            view().showValidationError(1);

        }else {
            requestVerifyCode(inputCode, mobileNumber,accessToken);
        }
    }

    @Override
    public void onPause() {
        view().cancelRequest(getRequestTags());
        if (countDownTimer!=null)
        {
            isTimerFinished = true;
            countDownTimer.cancel();
        }
    }

    private List<String> getRequestTags(){
        List<String> lists = new ArrayList<>();
        for (String item : request_tags){
            lists.add(item);
        }
        return lists;
    }

    @Override
    public void getRemainingTime(String remainingTime,String mobileNumber, String accessToken) {

        if (remainingTime != null){

            long currentTime = System.currentTimeMillis();
            long savedTime = Long.valueOf(remainingTime);
            long timeLapsed = currentTime - savedTime;

            /***to check if saved time is less than 1 minute*/
            if (timeLapsed < 60000 ){
                this.remainingTime = this.remainingTime - timeLapsed;
                setCountDownTimer(this.remainingTime);
                isTimerFinished = false;
                startTimer();
                this.remainingTime = 60000;

            }else {
                /***to check if saved time exceed to 1 minute*/
                setCountDownTimer(60000);
                view().saveCurrentTime(null);
                isTimerFinished = true;
                requestVerificationCode(mobileNumber,accessToken);
            }
        }else{
            /***if no saved time available*/
//            setCountDownTimer(60000);
            isTimerFinished = false;
            setCountDownTimer(60000);
            view().saveCurrentTime(String.valueOf(System.currentTimeMillis()));
            startTimer();
        }
    }

    @Override
    public void getVerificationCode(String mobileNumber, String accessToken) {
        if (isTimerFinished){

            requestVerificationCode(mobileNumber,accessToken);
            //TODO move this to onSuccess of request verification
//            setCountDownTimer(60000);
//            view().saveCurrentTime(String.valueOf(System.currentTimeMillis()));
//            startTimer();
        }
    }

    private void requestVerificationCode(String mobileNumber,String accessToken){

        view().showErrorMessage(false,"");
        view().showGetVerificationLoader(true);
        Request request = RegistrationApi.getVerificationCode(GET_VERIFICATION_REQUEST_CODE, mobileNumber,accessToken, this, new ExpressErrorHandler(this,VERIFY_CODE_REQUEST_CODE));
        request.setTag(GET_VERIFICATION_REQUEST_TAG);
        view().addRequest(request);

    }

    private void requestVerifyCode(String code, String mobileNumber, String accessToken){
        Request request = RegistrationApi.verifyCode(VERIFY_CODE_REQUEST_CODE,code, mobileNumber,accessToken, this, new ExpressErrorHandler(this,VERIFY_CODE_REQUEST_CODE));
        request.setTag(VERIFY_CODE_REQUEST_TAG);
        view().addRequest(request);
        view().showVerifyLoader(true);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){
            case GET_VERIFICATION_REQUEST_CODE:
                view().showGetVerificationLoader(false);

                view().handleGetVerificationCodeResponse(object.toString());
                setCountDownTimer(60000);
                view().saveCurrentTime(String.valueOf(System.currentTimeMillis()));
                startTimer();

                break;

            case VERIFY_CODE_REQUEST_CODE:
                stopTimer();
                view().showVerifyLoader(false);
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
                view().handleGetVerificationCodeErrorResponse(message);
                break;

            case VERIFY_CODE_REQUEST_CODE:
                view().showVerifyLoader(false);
                view().showErrorMessage(true,message);
                break;
            default:
                break;
        }
    }
}
