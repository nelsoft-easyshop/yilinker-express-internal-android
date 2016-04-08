package com.yilinker.expressinternal.mvp.view.login;

/**
 * Created by J.Bautista on 2/19/16.
 */
public interface ILoginView {

    public void initViews();
    public void goToMainScreen();
    public void showErrorMessage(String message);
    public void showInvalidInputMessage();
    public void showLoader(boolean showLoader);

}
