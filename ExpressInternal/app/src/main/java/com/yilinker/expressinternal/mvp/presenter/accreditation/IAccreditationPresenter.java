package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;

/**
 * Created by wagnavu on 3/16/16.
 */
public interface IAccreditationPresenter {

    public void onCreate();
    public void onSaveButtonClick();
    public void onDataUpdate(AccreditationRequirement data);
    public void onItemClick(AccreditationRequirement item);
    public void onCameraResult();
    public void onFirstNameTextChanged(String firstName);
    public void onLastNameTextChanged(String lastName);
    public void onBirthdayTextChanged(String birthday);
    public void onGenderChanged(String gender);

}
