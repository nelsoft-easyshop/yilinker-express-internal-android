package com.yilinker.expressinternal.controllers.cashmanagement;

import android.os.Bundle;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;

/**
 * Created by J.Bautista
 */
public class ActivityCashManagement extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_management);

        hideMenuButton();

    }
}
