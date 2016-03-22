package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;

/**
 * Created by wagnavu on 3/16/16.
 */
public interface IAccreditationPresenter {

    public void onCreate();
    public void onSaveButtonClick();
    public void onDataUpdate(AccreditationRequirement data);
}
