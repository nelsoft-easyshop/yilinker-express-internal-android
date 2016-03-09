package com.yilinker.expressinternal.mvp.presenter.registration;

import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.registration.IRegistrationSignUpPresenter;
import com.yilinker.expressinternal.mvp.view.registration.IActivityRegistrationSignUpView;

/**
 * Created by Patrick on 3/8/2016.
 */
public class RegistrationSignUpPresenter extends BasePresenter<Object,IActivityRegistrationSignUpView>
        implements IRegistrationSignUpPresenter {

    @Override
    protected void updateView() {
        view().onSignUpClick();
    }

    @Override
    public void validateInput(String mobileNumber) {
        if (mobileNumber.length()<10){
            view().showErrorMessage("Invalid mobile number");
        }else{
            updateView();
        }
    }
}
