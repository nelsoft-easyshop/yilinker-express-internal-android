package com.yilinker.expressinternal.controllers.qrscanner;

import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.adapters.AdapterTab;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityComplete;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityJobOderDetail;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityProblematic;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.TabModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by J.Bautista
 */
public class ActivitySingleScanner extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener{

    public static final String ARG_TEXT = "text";

    private List<JobOrder> jobOrderList = new ArrayList<>();

    private static final int TYPE_SINGLE = 0;

    private QRCodeReaderView qrReader;
    private ImageButton btnFlash;

    private int scannerType = TYPE_SINGLE;

    private RequestQueue requestQueue;

    private String currentID = "";

    //For Beep Sound
    private MediaPlayer mp;

    //For Flash
    private boolean isFlashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_scanner);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        initViews();
//        getJobOrders();

        setSound();

//        qrReader.getCameraManager().getCamera().setParameters();

    }

    private void getJobOrders() {

        jobOrderList = getIntent().getParcelableArrayListExtra(ActivityJobOrderList.ARG_CURRENT_LIST);

    }

    @Override
    protected void onResume() {
        super.onResume();

        qrReader.getCameraManager().startPreview();

    }

    @Override
    protected void onPause() {
        super.onPause();

        qrReader.getCameraManager().stopPreview();
        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnFlash:

                setFlash();
                break;

        }
    }

    @Override
    protected void handleRefreshToken() {

    }

    @Override
    public void onQRCodeRead(String s, PointF[] pointFs) {

        playSound();

        //TODO Verify Code
        boolean isValid = verifyCode();

        if(!isValid){

            //TODO Show message here
            return;
        }

        String waybillNo = s;

//        qrReader.getCameraManager().stopPreview();

        currentID = waybillNo;
        findWayBillNo(waybillNo);

    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }



    private void initViews(){

        qrReader = (QRCodeReaderView) findViewById(R.id.qrreader);
        btnFlash = (ImageButton) findViewById(R.id.btnFlash);

        qrReader.setOnQRCodeReadListener(this);

        hideMenuButton();

        //Set the title in the action bar
        setActionBarTitle(getString(R.string.actionbar_title_qrcode_single_scanning));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnFlash.setOnClickListener(this);

    }

    private void findWayBillNo(String waybillNo) {

//        for (int i = 0; i < jobOrderList.size(); i++) {

//            if ((jobOrderList.get(i).getWaybillNo().toLowerCase()).contains(waybillNo.toLowerCase())) {
                Intent result = new Intent();
//                result.putExtra(ActivityJobOrderList.ARG_OPEN_JO, waybillNo);
                result.putExtra(ARG_TEXT, waybillNo);
                setResult(RESULT_OK, result);
                finish();
//            }
//        }

    }

    private boolean verifyCode(){

        //TODO Check if the QR code is valid
        return true;
    }

    private void setSound(){

        int maxVolume = 50;

        float log1=(float)(Math.log(maxVolume-maxVolume)/Math.log(maxVolume));

        mp = MediaPlayer.create(this, R.raw.beep);
        mp.setVolume(1.0f, 1.0f);

    }

    private void playSound(){

        mp.start();
    }

    private void setFlash(){

        isFlashOn = !isFlashOn;

        Camera camera = qrReader.getCameraManager().getCamera();
        Camera.Parameters p = camera.getParameters();

        if(isFlashOn){

            btnFlash.setImageResource(R.drawable.ic_flash_on);
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);


        }
        else{
            btnFlash.setImageResource(R.drawable.ic_flash_off);
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        }

        qrReader.getCameraManager().getCamera().setParameters(p);
    }


}
