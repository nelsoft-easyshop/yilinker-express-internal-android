package com.yilinker.expressinternal;

import com.yilinker.expressinternal.mvp.model.Login;
import com.yilinker.expressinternal.mvp.presenter.login.LoginPresenter;
import com.yilinker.expressinternal.mvp.view.login.ActivityLogin;
import com.yilinker.expressinternal.mvp.view.login.ILoginView;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by J.Bautista on 2/22/16.
 */
public class LoginPresenterTest {

    private ActivityLogin view;
    private LoginPresenter presenter;

    @Before
    public void setup(){

        presenter = new LoginPresenter();
        view = mock(ActivityLogin.class);


    }

    @Test
    public void testNoUserName(){

        presenter.attemptLogin("", "123455");

    }

}
