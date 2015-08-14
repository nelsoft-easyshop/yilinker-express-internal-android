package com.yilinker.expressinternal.controllers.qrscanner;

import android.app.Activity;
import android.graphics.Camera;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.yilinker.expressinternal.R;


/**
 * Created by J.Bautista
 */
public class ActivityScanner extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrReader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        qrReader = (QRCodeReaderView) findViewById(R.id.qrreader);

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
    }

    @Override
    public void onQRCodeRead(String s, PointF[] pointFs) {

    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }


}
