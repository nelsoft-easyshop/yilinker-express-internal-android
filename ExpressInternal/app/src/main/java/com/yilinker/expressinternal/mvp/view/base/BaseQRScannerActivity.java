package com.yilinker.expressinternal.mvp.view.base;

import android.graphics.PointF;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.client.android.camera.open.CameraManager;
import com.yilinker.expressinternal.R;

import java.util.List;

/**
 * Created by J.Bautista on 4/5/16.
 */
public abstract class BaseQRScannerActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

    //For Beep Sound
    private MediaPlayer mediaPlayer;

    private Camera camera;
    private QRCodeReaderView qrReader;

    private List<Camera.Area> focusAreas;

    protected abstract List<Camera.Area> createFocusAreas();

    protected void setQrReader(QRCodeReaderView qrReader){

        this.qrReader = qrReader;
    }

    protected QRCodeReaderView getQrReader(){

        return this.qrReader;
    }

    @Override
    public void initializeViews(View parent) {

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
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        playSound();

    }

    private void setSound() {

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        mediaPlayer.setVolume(1.0f, 1.0f);

    }

    private void playSound() {

        mediaPlayer.start();
    }

}
