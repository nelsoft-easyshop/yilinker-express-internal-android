package com.yilinker.expressinternal.controllers.qrscanner;

import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.adapters.AdapterTab;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityComplete;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityJobOderDetail;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.TabModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by J.Bautista
 */
public class ActivityScanner extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener, TabItemClickListener, ResponseHandler{

    private static final int REQUEST_GET_JODETAILS = 1000;
    private static final int REQUEST_ACCEPT_JOB = 1001;

    private static final int STATUS_OPEN = 0;
    private static final int STATUS_CURRENT = 1;
    private static final int STATUS_COMPLETED = 2;
    private static final int STATUS_PROBLEMATIC = 3;


    private static final int TYPE_SINGLE = 0;
    private static final int TYPE_BULK = 1;

    private QRCodeReaderView qrReader;
    private RecyclerView rvTab;
    private ImageButton btnFlash;

    //For Tabs
    private List<TabModel> tabItems;
    private AdapterTab adapterTab;

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
        setContentView(R.layout.activity_scanner);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        initViews();

        setSound();

//        qrReader.getCameraManager().getCamera().setParameters();

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

        switch (scannerType){

            case TYPE_SINGLE:


                currentID = waybillNo;
                requestDetails(waybillNo);

                break;

            case TYPE_BULK:

                if(!currentID.equalsIgnoreCase(waybillNo)) {

                    requestAcceptJob(waybillNo);
                }

                break;

        }

    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_GET_JODETAILS:

                JobOrder jobOrder = new JobOrder((com.yilinker.core.model.express.internal.JobOrder) object);
                goToDetails(jobOrder);

                break;

            case REQUEST_ACCEPT_JOB:

                Toast.makeText(getApplicationContext(), "Job accepted!", Toast.LENGTH_SHORT).show();

                break;


        }


    }

    @Override
    protected void handleRefreshToken() {

        int currentRequest = getCurrentRequest();

        switch (currentRequest){

            case REQUEST_GET_JODETAILS:

                requestDetails(currentID);
                break;

            case REQUEST_ACCEPT_JOB:

                requestAcceptJob(currentID);
                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case REQUEST_GET_JODETAILS:

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                break;

            case REQUEST_ACCEPT_JOB:

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                break;

        }


        currentID = "";
    }

    @Override
    public void onTabItemClick(int position) {

        switch (position){

            case TYPE_SINGLE:

                scannerType = TYPE_SINGLE;

                break;

            case TYPE_BULK:

                scannerType = TYPE_BULK;

                break;

        }

    }

    private void initViews(){

        qrReader = (QRCodeReaderView) findViewById(R.id.qrreader);
        rvTab = (RecyclerView) findViewById(R.id.rvTab);
        btnFlash = (ImageButton) findViewById(R.id.btnFlash);

        qrReader.setOnQRCodeReadListener(this);

        hideMenuButton();

        //Set the title in the action bar
        setTitle(getString(R.string.actionbar_title_qrcode));

        setTabs();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnFlash.setOnClickListener(this);

    }

    private void setTabs(){

        tabItems = new ArrayList<TabModel>();
        String[] arrayTabItems = getResources().getStringArray(R.array.array_scanner_tab);

        TabModel tab;
        int i = 0;

        for(String item : arrayTabItems){

            tab = new TabModel();
            tab.setId(i);
            tab.setTitle(item);
            tabItems.add(tab);
            i++;
        }

        tabItems.get(0).setIsSelected(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapterTab = new AdapterTab(R.layout.layout_tab_item, tabItems, this);
        adapterTab.setEqualWidth(getWindowManager(), 2);
        rvTab.setLayoutManager(layoutManager);
        rvTab.setAdapter(adapterTab);
    }

    private void goToDetails(JobOrder jobOrder){

        Intent intent = null;

        if(jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_COMPLETE)){
            intent = new Intent(ActivityScanner.this, ActivityComplete.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(ActivityJobOderDetail.ARG_JOB_ORDER, jobOrder);
        }
        else {

            intent = new Intent(ActivityScanner.this, ActivityJobOderDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(ActivityJobOderDetail.ARG_JOB_ORDER, jobOrder);

            int status = 0;
            if(jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_OPEN)){

                status = STATUS_OPEN;
            }
            else if(jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF) || jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP) || jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

                status = STATUS_CURRENT;
            }
            else{

                status = STATUS_PROBLEMATIC;

            }

            intent.putExtra(ActivityJobOderDetail.ARG_CURRENT_STATUS, status);
        }

        startActivity(intent);

    }

    private void requestAcceptJob(String waybillNo){

        currentID = waybillNo;

        Request request = RiderAPI.acceptJobOrder(REQUEST_ACCEPT_JOB, waybillNo, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestDetails(String waybillNo){


        Request request = JobOrderAPI.getJobOrderDetailsByWaybillNo(REQUEST_GET_JODETAILS, waybillNo, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

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
