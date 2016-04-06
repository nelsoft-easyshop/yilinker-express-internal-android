package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationRequirementView;

/**
 * Created by wagnavu on 3/16/16.
 */
public abstract class AccreditationRequirementPresenter<P extends AccreditationRequirementView> extends BasePresenter<AccreditationRequirement, P> {

    public abstract void onValueChanged(Object object);
}
