package com.yilinker.expressinternal.mvp.view.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.view.base.BaseFragmentActivity;

/**
 * Created by Patrick on 3/9/2016.
 */
public class ActivityRegistrationTermsAndConditions extends BaseFragmentActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        initializeViews(null);
    }

    @Override
    public void initializeViews(View parent) {
        ImageView ivClose = (ImageView) findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ivClose:

                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
                break;

            default:
                break;
        }

    }
}
