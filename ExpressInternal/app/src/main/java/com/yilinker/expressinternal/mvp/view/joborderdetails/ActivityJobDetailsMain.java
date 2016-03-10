package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.contact.ActivityContact;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityJobOderDetail;
import com.yilinker.expressinternal.controllers.navigation.ActivityNavigation;
import com.yilinker.expressinternal.controllers.printer.FragmentDialogPrinterList;
import com.yilinker.expressinternal.controllers.qrcode.ActivityQRCode;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.JobDetailsMainPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class ActivityJobDetailsMain extends BaseFragmentActivity implements IJobDetailsMainView, View.OnClickListener {

    public static final String ARG_JOB = "job";
    private static final int REQUEST_DIALOG_PRINT = 2001;

    private JobDetailsMainPresenter presenter;

    private ImageButton btnContact;
    private ImageButton btnPrint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborderdetail2);

        if(savedInstanceState == null){

            presenter = new JobDetailsMainPresenter();
        }
        else {

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        initializeViews(null);


        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void initializeViews(View parent) {

        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        ImageButton btnQrCode = (ImageButton) findViewById(R.id.btnQRCode);
        ImageButton btnMap = (ImageButton) findViewById(R.id.btnMap);
        btnContact = (ImageButton) findViewById(R.id.btnContact);
        ImageButton btnImage = (ImageButton) findViewById(R.id.btnImage);
        btnPrint = (ImageButton) findViewById(R.id.btnPrint);


        btnBack.setOnClickListener(this);
        btnQrCode.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnPrint.setOnClickListener(this);

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void showContactScreen(JobOrder jobOrder) {

        Intent intent = new Intent(ActivityJobDetailsMain.this, ActivityContact.class);
        intent.putExtra(ActivityContact.ARG_NAME, jobOrder.getRecipient());
        intent.putExtra(ActivityContact.ARG_CONTACT_NO, jobOrder.getContactNo());
        startActivity(intent);
    }

    @Override
    public void showPrintScreen(JobOrder jobOrder) {

        FragmentDialogPrinterList dialog = FragmentDialogPrinterList.createInstance(REQUEST_DIALOG_PRINT, jobOrder);
        dialog.show(getFragmentManager(), null);

    }

    @Override
    public void showQRCodeScreen(JobOrder jobOrder) {

        Intent intent = new Intent(ActivityJobDetailsMain.this, ActivityQRCode.class);
        intent.putExtra(ActivityQRCode.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);
    }

    @Override
    public void showImages(List<String> imageUrls) {

        if (imageUrls.size() > 0) {

            Intent intent = new Intent(ActivityJobDetailsMain.this, ActivityImageGallery.class);
            intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, (ArrayList) imageUrls);
            intent.putExtra(ActivityImageGallery.ARG_TYPE, ImagePagerAdapter.TYPE_URL);
            startActivity(intent);

        } else {


            Toast.makeText(getApplicationContext(), getString(R.string.joborderdetail_error_no_image), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showNavigationMapScreen(JobOrder jobOrder) {


        Intent intent = new Intent(ActivityJobDetailsMain.this, ActivityNavigation.class);
        intent.putExtra(ActivityNavigation.ARG_DESTINATION_LAT, jobOrder.getLatitude());
        intent.putExtra(ActivityNavigation.ARG_DESTINATION_LONG, jobOrder.getLongitude());
        startActivity(intent);
    }

    @Override
    public void showOpenDetails(JobOrder jobOrder) {

        Fragment fragment = FragmentOpenJob.createInstance(jobOrder);
        replaceFragment(R.id.flContainer,fragment);

    }

    @Override
    public void showCurrentPickupDetails(JobOrder jobOrder) {

        Fragment fragment = FragmentCurrentPickupJob.createInstance(jobOrder);
        replaceFragment(R.id.flContainer,fragment);
    }

    @Override
    public void showDropoffDetails(JobOrder jobOrder) {

        Fragment fragment = FragmentCurrentDropoff.createInstance(jobOrder);
        replaceFragment(R.id.flContainer,fragment);
    }

    @Override
    public void showCurrentDeliveryDetails(JobOrder jobOrder) {

        Fragment fragment = FragmentCurrentDelivery.createInstance(jobOrder);
        replaceFragment(R.id.flContainer,fragment);



    }

    @Override
    public void showProblematicDeliveryDetails(JobOrder jobOrder) {

        Fragment fragment = FragmentCurrentProblematic.createInstance(jobOrder);
        replaceFragment(R.id.flContainer, fragment);
    }

    /**
     * Do all layout changes here for main activity on certain job order statuses
     * @param status
     */
    @Override
    public void updateViewForJobOrder(String status) {

        if(status.equalsIgnoreCase(getString(R.string.update_for_dropoff))) {
            btnContact.setVisibility(View.GONE);
        } else if (status.equalsIgnoreCase(getString(R.string.joborderdetail_problematic))) {
            btnPrint.setVisibility(View.GONE);
        }

    }


    private void getData(){

        Bundle arguments = getIntent().getExtras();

        JobOrder jobOrder = arguments.getParcelable(ARG_JOB);

        presenter.setModel(jobOrder);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnBack:

                onBackPressed();
                break;

            case R.id.btnQRCode:

                presenter.onQrButtonClick();
                break;

            case R.id.btnMap:

                presenter.onMapButtonClick();
                break;

            case R.id.btnContact:

                presenter.onContactButtonClick();
                break;

            case R.id.btnImage:

                presenter.onImageButtonClick();
                break;

            case R.id.btnPrint:

                presenter.onPrintButtonClick();
                break;

        }

    }
}
