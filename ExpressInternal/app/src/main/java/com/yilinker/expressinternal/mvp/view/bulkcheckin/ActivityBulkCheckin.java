package com.yilinker.expressinternal.mvp.view.bulkcheckin;

import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.customviews.CustomQRCodeReaderView;
import com.yilinker.expressinternal.mvp.model.BulkCheckinItem;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.bulkcheckin.BulkCheckinPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseQRScannerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 4/5/16.
 */
public class ActivityBulkCheckin extends BaseQRScannerActivity implements IBulkCheckinView {

    private BulkCheckinPresenter presenter;

    private BulkCheckinAdapter adapter;

    private RecyclerView rvItems;
    private ImageView ivFocusArea;
    private CustomQRCodeReaderView qrCodeReaderView;
    private Handler handler;
    private boolean stopScan;


    private Runnable delayScan = new Runnable() {
        @Override
        public void run() {

            qrCodeReaderView.getCameraManager().startPreview();
            stopScan = false;
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_toolbar_registration);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_checkin);

        if(savedInstanceState == null){

            presenter = new BulkCheckinPresenter();
            initializeViews(null);
            presenter.bindView(this);
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){

//            qrCodeReaderView.setFocusAreas(createFocusAreas());
//            qrCodeReaderView.setCameraPreviewSize(getPreviewHeight(), getPreviewWidth());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);

    }

    @Override
    protected void onPause() {

        presenter.onPause();
        presenter.unbindView();

        super.onPause();
    }

    @Override
    public void initializeViews(View parent) {
        super.initializeViews(parent);

        qrCodeReaderView = (CustomQRCodeReaderView) findViewById(R.id.qrcodeReader);
        ivFocusArea = (ImageView) findViewById(R.id.ivFocusArea);

        qrCodeReaderView.setOnQRCodeReadListener(this);

        setQrReader(qrCodeReaderView);

        setActionBar();
        setAdapter();
    }

    @Override
    protected List<Camera.Area> createFocusAreas() {

        int[] location = new int[2];
        ivFocusArea.getLocationOnScreen(location);

        int areaDimension = getResources().getDimensionPixelSize(R.dimen.qrscanner_focusarea_dimension);

        //Get dimension of the preview container
        View container = (View) qrCodeReaderView.getParent();
        int width = container.getWidth();
        int height = container.getHeight();


//        int x1 = ivFocusArea.getLeft() * (2000 / width) - 1000;
//        int x2 = ivFocusArea.getTop() * (2000 / width) - 1000;
//        int y1 = ivFocusArea.getRight() * (2000 / height) - 1000;
//        int y2 = ivFocusArea.getBottom() * (2000 / height) - 1000;

        int x1 = -10;
        int x2 = 10;
        int y1 = -10;
        int y2 = 10;

        Toast.makeText(getApplicationContext(), String.format("x1: %d, x2: %d, y1: %d, y2: %d", x1, x2, y1, y2), Toast.LENGTH_LONG).show();

        Rect focusArea = new Rect(x1,y1, x2, y2);

        Camera.Area area = new Camera.Area(focusArea, 1000);

        List<Camera.Area> focusAreas = new ArrayList<>();
        focusAreas.add(area);

        return focusAreas;
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        if(!stopScan) {

            stopScan = true;

            qrCodeReaderView.getCameraManager().stopPreview();

            super.onQRCodeRead(text, points);

            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
//        getQrReader().getCameraManager().stopPreview();
            presenter.onQRCodeScan(text);


            if(handler == null){

                handler = new Handler();
            }

            handler.postDelayed(delayScan , 2000);

        }

    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void addItem(BulkCheckinItem item) {

        adapter.addItem(item);

        rvItems.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void updateItem(BulkCheckinItem item) {

        adapter.updateItem(item);
    }

    @Override
    public void addRequest(Request request) {

        addRequestToQueue(request);
    }

    @Override
    public void cancelRequest(List<String> requests) {

        cancelRequests(requests);
    }

    private void setAdapter(){

        adapter = new BulkCheckinAdapter();

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setAutoMeasureEnabled(true);

        rvItems.setLayoutManager(layoutManager);

        rvItems.setAdapter(adapter);

    }

    private void setActionBar(){

        setActionBarTitle(getString(R.string.bulk_checkin_scanqrcode));

    }

    private int getPreviewHeight(){

        //Get dimension of the preview container
        View container = (View) qrCodeReaderView.getParent();
        int height = container.getHeight();

        return height;

    }

    private int getPreviewWidth(){

        //Get dimension of the preview container
        View container = (View) qrCodeReaderView.getParent();
        int width = container.getWidth();

        return width;

    }
}
