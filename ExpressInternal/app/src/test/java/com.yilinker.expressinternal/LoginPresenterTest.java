package com.yilinker.expressinternal;

import android.content.Context;

import com.yilinker.expressinternal.business.ApplicationClass;
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

        Context context = mock(ApplicationClass.class);
        view = mock(ActivityLogin.class);
        presenter = new LoginPresenter(context, view);

    }

    @Test
    public void testNoUserName(){

        presenter.attemptLogin("", "123455");

    }

    @Test
    public void testNoUserPassword(){

        presenter.attemptLogin("Name", "");

    }

}
