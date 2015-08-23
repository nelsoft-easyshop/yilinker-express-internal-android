package com.yilinker.expressinternal.controllers.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;

/**
 * Created by J.Bautista
 */
public class ActivityContact extends BaseActivity {

    public static final String ARG_CONTACT_NO = "contactNo";
    public static final String ARG_NAME = "name";

    private ImageButton btnCall;
    private ImageButton btnSMS;

    private String name;
    private String contactNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initViews();

        getData();

    }
    private void initViews(){

        btnCall = (ImageButton) findViewById(R.id.btnCall);
        btnSMS = (ImageButton) findViewById(R.id.btnSMS);


        //For Action Bar
        setTitle("For Pickup");
        setActionBarBackgroundColor(R.color.marigold);

        btnCall.setOnClickListener(this);
        btnSMS.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnCall:

                call();
                break;

            case R.id.btnSMS:

                sendSMS();
                break;

        }

    }

    private void getData(){

        Intent intent = getIntent();
        contactNo = intent.getStringExtra(ARG_CONTACT_NO);
        name = intent.getStringExtra(ARG_NAME);

    }

    private void call(){

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(String.format("tel:%s", contactNo)));
        startActivity(intent);

    }

    private void sendSMS(){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contactNo, null));
        startActivity(intent);

    }
}
