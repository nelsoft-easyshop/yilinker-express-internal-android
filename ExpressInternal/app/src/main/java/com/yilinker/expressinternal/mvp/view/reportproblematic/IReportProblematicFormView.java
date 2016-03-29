package com.yilinker.expressinternal.mvp.view.reportproblematic;

import android.database.Cursor;
import android.net.Uri;

import com.yilinker.expressinternal.mvp.view.RequestBaseView;

import java.util.ArrayList;

/**
 * Created by patrick-villanueva on 3/29/2016.
 */
public interface IReportProblematicFormView extends RequestBaseView{

    public void goToImages(ArrayList<String> images);
    public void launchCamera(Uri photoUri);
    public Cursor getCursor(Uri contentUri, String[] projection );
    public void onSuccess(String message);
    public void showErrorMessage(String errorMessage);
    public void setSelectedProblematicType(String problematicType);
}
