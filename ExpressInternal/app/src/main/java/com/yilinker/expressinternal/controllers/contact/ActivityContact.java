package com.yilinker.expressinternal.controllers.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView tvName;
    private TextView tvContactNo;

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

        bindView();

    }
    private void initViews(){

        btnCall = (ImageButton) findViewById(R.id.btnCall);
        btnSMS = (ImageButton) findViewById(R.id.btnSMS);
        tvContactNo = (TextView) findViewById(R.id.tvContactNo);
        tvName = (TextView) findViewById(R.id.tvContacName);


        //For Action Bar
        setActionBarTitle(getString(R.string.actionbar_title_contact_details));
        setActionBarBackgroundColor(R.color.marigold);

        btnCall.setOnClickListener(this);
        btnSMS.setOnClickListener(this);

    }

    private void bindView(){

        tvContactNo.setText(contactNo == null ? "-" : contactNo);
        tvName.setText(name == null ? "-" : name);

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

    @Override
    protected void handleRefreshToken() {

    }

    private void getData(){

        Intent intent = getIntent();
        contactNo = intent.getStringExtra(ARG_CONTACT_NO);
        name = intent.getStringExtra(ARG_NAME);

    }

    private void call(){

        if(contactNo != null) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(String.format("tel:%s", contactNo)));
            startActivity(intent);
        }else{

            Toast.makeText(getApplicationContext(), getString(R.string.contact_error_no_number), Toast.LENGTH_LONG).show();
        }

    }

    private void sendSMS(){

        if(contactNo != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contactNo, null));
            startActivity(intent);
        }
        else{

            Toast.makeText(getApplicationContext(), getString(R.string.contact_error_no_number), Toast.LENGTH_LONG).show();
        }

    }

}
