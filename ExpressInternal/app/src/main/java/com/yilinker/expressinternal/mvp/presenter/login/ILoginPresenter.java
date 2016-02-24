package com.yilinker.expressinternal.mvp.presenter.login;

/**
 * Created by J.Bautista on 2/19/16.
 */
public interface ILoginPresenter {

    public void attemptLogin(String username, String password);
    public boolean hasEnteredValidInputs(String username, String password);
    public void saveTokens();
    public void requestLogin(String username, String password);
    public void requestVerifyRider();

}
