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
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.TabModel;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ActivityJobDetailsMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/10/16.
 */
public class ActivityScanToDetails extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener, ResponseHandler {

    private static final int REQUEST_GET_JODETAILS = 1000;

    private static final int TYPE_SINGLE = 0;
    private static final int TYPE_BULK = 1;

    private QRCodeReaderView qrReader;
    private RecyclerView rvTab;
    private ImageButton btnFlash;

    private RequestQueue requestQueue;

    //For Beep Sound
    private MediaPlayer mp;

    //For Flash
    private boolean isFlashOn;

    private String currentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_scanner);

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

        currentID = s;
        requestDetails(currentID);
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
        }


    }

    @Override
    protected void handleRefreshToken() {

        int currentRequest = getCurrentRequest();

        switch (currentRequest){

            case REQUEST_GET_JODETAILS:

                requestDetails(currentID);

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case REQUEST_GET_JODETAILS:

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                break;
        }


        currentID = "";
    }


    private void initViews(){

        qrReader = (QRCodeReaderView) findViewById(R.id.qrreader);
        rvTab = (RecyclerView) findViewById(R.id.rvTab);
        btnFlash = (ImageButton) findViewById(R.id.btnFlash);

        qrReader.setOnQRCodeReadListener(this);

        hideMenuButton();

        //Set the title in the action bar
        setActionBarTitle(getString(R.string.actionbar_title_qrcode));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnFlash.setOnClickListener(this);
    }

    private void goToDetails(JobOrder jobOrder){

        Intent intent = new Intent(ActivityScanToDetails.this, ActivityJobDetailsMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(ActivityJobDetailsMain.ARG_JOB, jobOrder);

        startActivity(intent);

    }

    private void requestDetails(String waybillNo){


        Request request = JobOrderAPI.getJobOrderDetailsByWaybillNo(REQUEST_GET_JODETAILS, waybillNo, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

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
