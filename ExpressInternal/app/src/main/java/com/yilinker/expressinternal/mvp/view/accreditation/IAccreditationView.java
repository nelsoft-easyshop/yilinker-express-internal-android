package com.yilinker.expressinternal.mvp.view.accreditation;

import android.net.Uri;

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
    public void launchCamera(Uri uri);
    public void launchGallery();
    public void resetItem(AccreditationRequirement item);
    public String compressImage(String path);
    public void enableSaveButton(boolean isEnabled);
    public void showScreenLoader(boolean isShown);
    public void goToConfirmation();
}
