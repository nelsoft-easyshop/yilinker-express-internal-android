package com.yilinker.expressinternal.mvp.model;

/**
 * Created by J.Bautista on 2/22/16.
 */
public class Login {

    private String accessToken;
    private String refreshToken;

    public Login(com.yilinker.core.model.Login login){

        this.accessToken = login.getAccess_token();
        this.refreshToken = login.getRefresh_token();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
