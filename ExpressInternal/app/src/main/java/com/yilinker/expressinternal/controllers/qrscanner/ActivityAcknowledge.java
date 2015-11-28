package com.yilinker.expressinternal.controllers.qrscanner;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.model.JobOrder;

/**
 * Created by J.Bautista
 */
public class ActivityAcknowledge extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener, ResponseHandler {

    private static final int REQUEST_ACKNOWLEDGE = 1000;

    private QRCodeReaderView qrReader;
    private ImageButton btnFlash;
    private RelativeLayout rlProgress;

    private RequestQueue requestQueue;

    //For Beep Sound
    private MediaPlayer mp;

    //For Flash
    private boolean isFlashOn;

    private AlertDialog alertDialog;

    private String waybillNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledge);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        initViews();

        setSound();

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

            case R.id.btnOk:

                requestAcknowledge(waybillNo);
                alertDialog.dismiss();

                break;

            case R.id.btnCancel:

                alertDialog.dismiss();
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_ACKNOWLEDGE:

                handleAcknowledgeResponse(object);
                break;
        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case REQUEST_ACKNOWLEDGE:

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                rlProgress.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    protected void handleRefreshToken() {

        int currentRequest = getCurrentRequest();

        switch (currentRequest){

            case REQUEST_ACKNOWLEDGE:

                requestAcknowledge(waybillNo);
                break;


        }

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {


        if(!alertDialog.isShowing()) {

            playSound();

            waybillNo = text;

            alertDialog.show();
        }

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
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

        qrReader.setOnQRCodeReadListener(this);

        hideMenuButton();

        //Set the title in the action bar
        setActionBarTitle(getString(R.string.actionbar_title_acknowledge));


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnFlash.setOnClickListener(this);

        rlProgress.setVisibility(View.GONE);

        createDialog();
    }

    private void createDialog(){

        if(alertDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.layout_dialog_acknowledge, null);
            dialogBuilder.setView(dialogView);

            Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
            Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

            btnOk.setOnClickListener(this);
            btnCancel.setOnClickListener(this);

            alertDialog = dialogBuilder.create();
        }
    }


    private void setSound(){

        int maxVolume = 50;

        float log1=(float)(Math.log(maxVolume-maxVolume)/Math.log(maxVolume));

        mp = MediaPlayer.create(this, R.raw.beep);
        mp.setVolume(1.0f, 1.0f);

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

    private void playSound(){

        mp.start();
    }

    private void requestAcknowledge(String waybillNo){

        rlProgress.setVisibility(View.VISIBLE);

        Request request = RiderAPI.acknowledgePackage(REQUEST_ACKNOWLEDGE, waybillNo, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);
    }

    private void handleAcknowledgeResponse(Object object){

        JobOrder jobOrder = new JobOrder((com.yilinker.core.model.express.internal.JobOrder) object);
        goToChecklist(jobOrder);

        rlProgress.setVisibility(View.GONE);
    }

    private void goToChecklist(JobOrder jobOrder){

        Intent intent = new Intent(ActivityAcknowledge.this, ActivityChecklist.class);
        intent.putExtra(ActivityChecklist.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);
    }
}
