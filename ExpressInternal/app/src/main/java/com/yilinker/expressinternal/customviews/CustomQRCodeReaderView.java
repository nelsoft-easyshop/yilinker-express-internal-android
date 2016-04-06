package com.yilinker.expressinternal.customviews;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.client.android.camera.open.CameraManager;

import java.util.List;

/**
 * Created by J.Bautista on 4/5/16.
 */
public class CustomQRCodeReaderView extends QRCodeReaderView implements Camera.AutoFocusCallback{

    private List<Camera.Area> focusAreas;

    public CustomQRCodeReaderView(Context context) {
        super(context);
    }

    public CustomQRCodeReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        super.surfaceChanged(holder, format, width, height);
        
    }

    public void setFocusAreas(List<Camera.Area> focusAreas){

        this.focusAreas = focusAreas;


        CameraManager cameraManager = getCameraManager();
        Camera camera = cameraManager.getCamera();

        Camera.Parameters parameters = camera.getParameters();

        if(focusAreas != null){

            camera.cancelAutoFocus();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setFocusAreas(focusAreas);
            camera.setParameters(parameters);
            camera.autoFocus(this);
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

        if(success){

            camera.cancelAutoFocus();

        }
    }
}
