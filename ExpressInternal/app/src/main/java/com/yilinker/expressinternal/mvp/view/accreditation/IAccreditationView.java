package com.yilinker.expressinternal.mvp.view.accreditation;

import com.android.volley.Request;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;

import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public interface IAccreditationView {

    public void goToLoginScreen();
    public void showErrorMessage(String message);
    public void showErrorMessageByType(int type);
    public void loadRequirementsList(List<AccreditationRequirement> requirements);
    public void addRequest(Request request);

}
