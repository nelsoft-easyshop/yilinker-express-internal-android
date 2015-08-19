package com.yilinker.expressinternal.controllers.qrscanner;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.adapters.AdapterTab;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.TabModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by J.Bautista
 */
public class ActivityScanner extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener, TabItemClickListener, ResponseHandler{

    private static final int TYPE_SINGLE = 0;
    private static final int TYPE_BULK = 1;

    private QRCodeReaderView qrReader;
    private RecyclerView rvTab;

    //For Tabs
    private List<TabModel> tabItems;
    private AdapterTab adapterTab;

    private int scannerType = TYPE_SINGLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        initViews();

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

        //TODO Verify Code
        boolean isValid = verifyCode();

        if(!isValid){

            //TODO Show message here
            return;
        }

        switch (scannerType){

            case TYPE_SINGLE:

                goToDetails();
                Toast.makeText(getApplicationContext(), "Single scanner", Toast.LENGTH_LONG).show();

                break;

            case TYPE_BULK:

                updateJobOrderStatus();
                Toast.makeText(getApplicationContext(), "Bulk scanner", Toast.LENGTH_LONG).show();

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

    }

    @Override
    public void onFailed(int requestCode, String message) {

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

        qrReader.setOnQRCodeReadListener(this);

        hideMenuButton();

        //Set the title in the action bar
        setTitle(getString(R.string.actionbar_title_qrcode));

        setTabs();

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

    private void goToDetails(){


    }

    private void updateJobOrderStatus(){


    }

    private boolean verifyCode(){

        //TODO Check if the QR code is valid

        return true;
    }
}
