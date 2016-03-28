package com.yilinker.expressinternal.controllers.checklist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.helper.RecyclerViewWrapContentHelper;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.confirmpackage.ActivityConfirmPackage;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityComplete;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityComplete2;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.controllers.joborderlist.AdapterJobOrderList;
import com.yilinker.expressinternal.controllers.signature.ActivitySignature;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.PackageType;
import com.yilinker.expressinternal.model.Rider;
//import com.yilinker.expressinternal.mvp.view.images.ActivityImageGallery;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ActivityCompleteJODetails;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class ActivityChecklist extends BaseActivity implements RecyclerViewClickListener<ChecklistItem>, ResponseHandler, DialogDismissListener {

    public static final String ARG_JOB_ORDER = "jobOrder";

    private static final int REQUEST_DIALOG_UPDATE = 2000;

    public static final int REQUEST_SIGNATURE = 1000;
    public static final int REQUEST_SUBMIT_SIGNATURE = 1001;
    public static final int REQUEST_SUBMIT_RATING = 1002;
    public static final int REQUEST_UPDATE = 1003;
    public static final int REQUEST_UPLOAD_IMAGES = 1004;

    private static final int REQUEST_LAUNCH_CAMERA_ID = 2000;
    private static final int REQUEST_LAUNCH_CAMERA_PICTURE = 2001;
    private static final int REQUEST_GALLERY_ID = 2002;
    private static final int REQUEST_GALLERY_PICTURE = 2003;
    private static final int REQUEST_CONFIRM_PACKAGE = 2004;

    private final static String KEY_PHOTO_URI = "photoUri";
    private final static String KEY_CLICKED_POSITION = "clickedPosition";
    private final static String KEY_LIST = "list";
    private final static String KEY_JOB_ORDER = "job-order";

    private static int CHECKLIST_PACKAGE_CONFIRMED = 1;
    //    private static  int CHECKLIST_VALID_ID = 2;
//    private static  int CHECKLIST_RECIPIENT_PICTURE = 3;
    private static int CHECKLIST_SIGNATURE = 4;

    public static final String ARG_WAYBILL_NO = "waybillNo";
    public static final String ARG_JOBORDER_NO = "jobOrderNo";
    public static final String ARG_IMAGES = "images";
    public static final String ARG_SIGNATURE = "signature";
    public static final String ARG_RATING = "rating";

    private RelativeLayout rlProgress;
    private TextView tvJobOrderNo;
    private TextView tvItem;

    private Button btnConfirm;

    private RecyclerView rvChecklist;
    private AdapterChecklist adapter;
    private List<ChecklistItem> items;

    private JobOrder jobOrder;

    //For submission
    private String signatureImage;
    private int rating;

    private RequestQueue requestQueue;
    SyncDBTransaction syncTransaction;

    private Uri photoUri;
    //    private String validIdImage;
//    private String recipientPicture;
    private String newStatus;

    private PackageType packageFee;

    private int clickedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();
        syncTransaction = new SyncDBTransaction(this);

        getData();

        initViews();

        bindViews();

    }

    @Override
    protected void onPause() {
        super.onPause();

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
        rlProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (photoUri != null) {
            outState.putString(KEY_PHOTO_URI, photoUri.toString());

        }
        outState.putParcelable(KEY_JOB_ORDER, jobOrder);
        outState.putInt(KEY_CLICKED_POSITION, clickedPosition);
        outState.putParcelableArrayList(KEY_LIST, (ArrayList) items);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            photoUri = Uri.parse(savedInstanceState.getString(KEY_PHOTO_URI));
            clickedPosition = savedInstanceState.getInt(KEY_CLICKED_POSITION);
            jobOrder = savedInstanceState.getParcelable(KEY_JOB_ORDER);

            items.clear();
            items.addAll((List) savedInstanceState.getParcelableArrayList(KEY_LIST));
        }
    }

    @Override
    public void onItemClick(int position, ChecklistItem object) {

        clickedPosition = position;

        String checkListItem = object.getTitle();

        if (checkListItem.equalsIgnoreCase(getString(R.string.checklist_delivery_signature))) {

//            if(!items.get(CHECKLIST_SIGNATURE).isChecked()) {
//                showSignature();
//            }
//            else{
//
//                //TODO Show signature image
//            }

            showSignature();

            return;
        } else if (checkListItem.equalsIgnoreCase(getString(R.string.checklist_delivery_valid_id))) {

            if (!items.get(position).isChecked()) {


                launchCamera(REQUEST_LAUNCH_CAMERA_ID);
            } else {

                ArrayList<String> images = new ArrayList<>();
//                images.add(validIdImage);
                String image = items.get(clickedPosition).getAttachedItem().getString("image");
                images.add(image);

                showImageGallery(images, REQUEST_GALLERY_ID);

            }

            return;
        } else if (checkListItem.equalsIgnoreCase(getString(R.string.checklist_delivery_picture))) {

//            if(!items.get(CHECKLIST_RECIPIENT_PICTURE).isChecked()) {
            if (!items.get(position).isChecked()) {


                launchCamera(REQUEST_LAUNCH_CAMERA_PICTURE);
            } else {

                ArrayList<String> images = new ArrayList<>();
//                images.add(recipientPicture);
                String image = items.get(clickedPosition).getAttachedItem().getString("image");
                images.add(image);

                showImageGallery(images, REQUEST_GALLERY_PICTURE);
            }

            return;
        } else if (checkListItem.equalsIgnoreCase(getString(R.string.checklist_delivery_package))) {
//            if(!items.get(CHECKLIST_PACKAGE_CONFIRMED).isChecked()) {

            showPackageConfirmation();
            return;

//            }
        }


        object.setIsChecked(!object.isChecked());
        adapter.notifyItemChanged(position);

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_SIGNATURE:

                    handleSignature(data);
                    break;

                case REQUEST_LAUNCH_CAMERA_ID:

                    updateValidIDChecklist();
                    break;

                case REQUEST_LAUNCH_CAMERA_PICTURE:

                    updateRecipientPictureChecklist();
                    break;

                case REQUEST_GALLERY_ID:

                    photoUri = Uri.parse(data.getStringExtra(ActivityImageGallery.ARG_NEW_PHOTO));
                    updateValidIDChecklist();
                    break;

                case REQUEST_GALLERY_PICTURE:

                    photoUri = Uri.parse(data.getStringExtra(ActivityImageGallery.ARG_NEW_PHOTO));
                    updateRecipientPictureChecklist();
                    break;

                case REQUEST_CONFIRM_PACKAGE:

                    handleConfirmPackage(data);
                    break;

            }
        }

    }

    private void initViews() {

        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);
        tvJobOrderNo = (TextView) findViewById(R.id.tvJobOrderNo);
        tvItem = (TextView) findViewById(R.id.tvItem);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        rvChecklist = (RecyclerView) findViewById(R.id.rvChecklist);

        btnConfirm.setEnabled(false);

        //For Action Bar
        setActionBarTitle(jobOrder.getStatus());
        setActionBarBackgroundColor(R.color.marigold);

        btnConfirm.setOnClickListener(this);

        rlProgress.setVisibility(View.GONE);

        setAdapter();

        //Set Button's Text
        String buttonText = null;
        if (jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)) {

            buttonText = getString(R.string.checklist_delivery_complete);
        } else if (jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)) {

            buttonText = getString(R.string.checklist_pickup_complete);
        }

        btnConfirm.setText(buttonText);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id) {

            case R.id.btnConfirm:

                handleCompleteButton();
                break;
        }

    }

    private void bindViews() {

//        tvJobOrderNo.setText(jobOrder.getJobOrderNo());

        tvJobOrderNo.setText(jobOrder.getWaybillNo());
//        //For Items
//        List<String> items  = jobOrder.getItems();
//        StringBuilder builder = new StringBuilder();
//        for (String item : items){
//            builder.append(item);
//            builder.append(", ");
//        }
//
//        int start = builder.length() - 2;
//        int end = builder.length() - 1;
//        builder.delete(start, end);

//        tvItem.setText(builder.toString());
        tvItem.setText(jobOrder.getPackageDescription());
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

//        ImageUtility.clearCache();

        switch (requestCode) {

//            case REQUEST_SUBMIT_SIGNATURE:
//
////                requestSubmitRating();
//                requestUploadImages();
//                break;

            case REQUEST_SUBMIT_RATING:

//                requestUploadImages();
                break;

            case REQUEST_UPLOAD_IMAGES:

//                requestUpdate(JobOrderConstant.JO_COMPLETE);
                break;

            case REQUEST_UPDATE:

                String status = jobOrder.getStatus();
                if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)) {

                    Toast.makeText(getApplicationContext(), getString(R.string.checklist_job_updated), Toast.LENGTH_LONG).show();
                    goToHome();
                    startPickupService();

                } else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)) {

                    Toast.makeText(getApplicationContext(), getString(R.string.checklist_job_completed), Toast.LENGTH_LONG).show();
                    startChecklistService();

                    goToCompleteScreen();

                    finish();

//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            goToCompleteScreen();
//
//                            finish();
//                        }
//                    }, 2000);



                }

                break;

        }

    }


    @Override
    protected void handleRefreshToken() {

        int currentRequest = getCurrentRequest();
        switch (currentRequest) {

            case REQUEST_SUBMIT_SIGNATURE:

                requestSubmitSignature();
                break;

            case REQUEST_SUBMIT_RATING:

                requestSubmitRating();
                break;

//            case REQUEST_UPLOAD_IMAGES:
//
//                requestUploadImages();
//                break;

            case REQUEST_UPDATE:

                requestUpdate(newStatus);
                break;
        }


    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

//        ImageUtility.clearCache();

        switch (requestCode) {

//            case REQUEST_SUBMIT_SIGNATURE:
//
//                handleFailedSubmitSignature();
//
//                break;
//
//            case REQUEST_SUBMIT_RATING:
//
//                handleFailedSubmitRating();
//
//                break;
//
//            case REQUEST_UPLOAD_IMAGES:
//
//                handleFailedUploadImages();
//
//                break;

            case REQUEST_UPDATE:

                handleFailedRequestUpdate();


                break;

            default:

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                rlProgress.setVisibility(View.GONE);

                break;

        }
    }

    private void setAdapter() {

        items = new ArrayList<>();

        String status = jobOrder.getStatus();

        String[] arrayItems = null;

        if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)) {
            arrayItems = getResources().getStringArray(R.array.checklist_delivery);

        } else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)) {
            arrayItems = getResources().getStringArray(R.array.checklist_claiming);
        } else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)) {
            arrayItems = getResources().getStringArray(R.array.checklist_pickup);
        }

        ChecklistItem item = null;
        int size = arrayItems.length;
        for (int i = 0; i < size; i++) {

            item = new ChecklistItem();
            item.setTitle(arrayItems[i]);

            items.add(item);
        }

        if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY) && jobOrder.getAmountToCollect() == 0) {

//            CHECKLIST_RECIPIENT_PICTURE -= 1;
//            CHECKLIST_SIGNATURE -= 1;
//            CHECKLIST_VALID_ID -= 1;

            items.remove(0);
        } else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP) && jobOrder.getAmountToCollect() == 0) {

            items.remove(0);
        }


        adapter = new AdapterChecklist(items, this);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerViewWrapContentHelper layoutManager = new RecyclerViewWrapContentHelper(this, LinearLayoutManager.VERTICAL, false);
        rvChecklist.setLayoutManager(layoutManager);
        rvChecklist.setHasFixedSize(true);
        rvChecklist.setAdapter(adapter);

        rvChecklist.smoothScrollToPosition(0);

    }

    private boolean isComplete() {

        boolean isComplete = true;
        for (ChecklistItem item : items) {

            if (!item.isChecked()) {
                isComplete = false;
                break;
            }

        }

        return isComplete;
    }

    private void setConfirmButton(boolean isEnabled) {

        int textColor = 0;

        if (isEnabled) {

            btnConfirm.setBackgroundResource(R.drawable.bg_btn_orangeyellow);
            textColor = getResources().getColor(R.color.white);
        } else {

            btnConfirm.setBackgroundResource(R.color.white_gray);
            textColor = getResources().getColor(R.color.warm_gray);
        }


        btnConfirm.setTextColor(textColor);
        btnConfirm.setEnabled(isEnabled);
    }


    private void showSignature() {

        Intent intent = new Intent(ActivityChecklist.this, ActivitySignature.class);
        intent.putExtra(ActivitySignature.ARG_IMAGE_FILE, signatureImage);

        startActivityForResult(intent, REQUEST_SIGNATURE);
    }

    private void showPackageConfirmation() {

        Intent intent = new Intent(ActivityChecklist.this, ActivityConfirmPackage.class);
        intent.putExtra(ARG_JOB_ORDER, jobOrder.getJobOrderNo());
        intent.putExtra(ActivityConfirmPackage.ARG_PACKAGE_FEE, packageFee);

        startActivityForResult(intent, REQUEST_CONFIRM_PACKAGE);

    }

    private void getData() {

        jobOrder = getIntent().getParcelableExtra(ARG_JOB_ORDER);

    }

    private void handleCompleteButton() {

        String type = jobOrder.getType();
        String status = jobOrder.getStatus();

        if (status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY)) {


        } else if (status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP)) {


        } else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)) {

            //Check if the job order is within the rider's area. If yes, show the dialog for selecting new status, else update the status to For Drop-off

            Rider rider = ((ApplicationClass) BaseApplication.getInstance()).getRider();

            if (rider.getAreaCode().equals(jobOrder.getAreaCode())) {

                showNewStatusDialog();
            } else {

                requestUpdate(JobOrderConstant.JO_CURRENT_DROPOFF);
            }

        } else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)) {

            requestUpdate(JobOrderConstant.JO_COMPLETE);
//            requestSubmitRating();
//            requestSubmitSignature();
//            requestUploadImages();
        } else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)) {


        } else if (status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)) {


        }

    }

    private void requestSubmitSignature() {

        rlProgress.setVisibility(View.VISIBLE);

//        String image = convertSignatureToString();
        String image = signatureImage;

        Request request = JobOrderAPI.uploadSignature(REQUEST_SUBMIT_SIGNATURE, jobOrder.getJobOrderNo(), image, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }


    private void requestSubmitRating() {

        Request request = JobOrderAPI.addRating(REQUEST_SUBMIT_RATING, jobOrder.getJobOrderNo(), rating, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

//    private void requestUploadImages(){
//
//       List<String> images = new ArrayList<>();
//
//        Uri uri = Uri.parse(validIdImage);
////        images.add(ImageUtility.compressCameraFileBitmap(uri.getEncodedPath()));
//        images.add(ImageUtility.compressCameraFileBitmap(uri.getEncodedPath()));
//
//        uri = Uri.parse(recipientPicture);
////        images.add(ImageUtility.compressCameraFileBitmap(uri.getEncodedPath()));
//        images.add(ImageUtility.compressCameraFileBitmap(uri.getEncodedPath()));
//
//        Request request = JobOrderAPI.uploadJobOrderImages(REQUEST_UPLOAD_IMAGES, jobOrder.getWaybillNo(), images, this);
//        request.setTag(ApplicationClass.REQUEST_TAG);
//
//        requestQueue.add(request);
//
//    }

    private void requestUpdate(String newStatus) {

        rlProgress.setVisibility(View.VISIBLE);

        this.newStatus = newStatus;

        Request request = JobOrderAPI.updateStatus(REQUEST_UPDATE, jobOrder.getJobOrderNo(), newStatus, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void handleFailedRequestUpdate() {

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(REQUEST_UPDATE);
        request.setKey(String.format("%s%s", jobOrder.getJobOrderNo(), String.valueOf(REQUEST_UPDATE)));
        request.setId(jobOrder.getJobOrderNo());
        request.setData(newStatus);
        request.setSync(false);

        syncTransaction.add(request);

        //start service even update failed
        if (jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)) {
            startChecklistService();
        } else if (jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)) {
            startPickupService();
        }

        goToHome();

    }

    /**
     * Starts service for other checklist requests
     */

    private void startChecklistService() {

        Intent i = new Intent(this, ServiceDeliveryChecklist.class);

        String[] images = new String[2];

        int validIdPosition = getChecklistItem(getString(R.string.checklist_delivery_valid_id));
        int recipientImagePosition = getChecklistItem(getString(R.string.checklist_delivery_picture));

        String validIdImage = items.get(validIdPosition).getAttachedItem().getString("image");
        String recipientPicture = items.get(recipientImagePosition).getAttachedItem().getString("image");

        Uri uri = Uri.parse(validIdImage);
        images[0] = ImageUtility.compressCameraFileBitmap(uri.getEncodedPath(), getApplicationContext());

        uri = Uri.parse(recipientPicture);
        images[1] = ImageUtility.compressCameraFileBitmap(uri.getEncodedPath(), getApplicationContext());

        i.putExtra(ARG_WAYBILL_NO, jobOrder.getWaybillNo());
        i.putExtra(ARG_JOBORDER_NO, jobOrder.getJobOrderNo());
        i.putExtra(ARG_RATING, String.valueOf(rating));
        i.putExtra(ARG_SIGNATURE, signatureImage);
        i.putExtra(ARG_IMAGES, images);

        this.startService(i);

    }

    private void startPickupService() {

        Intent i = new Intent(this, ServicePickupChecklist.class);


    }

    private String convertSignatureToString() {

        String result = null;
        File file = new File(signatureImage);

        FileInputStream imageInFile = null;
        try {

            imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);

            result = Base64.encodeToString(imageData, Base64.DEFAULT);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;

    }

    //Old version
//    private void goToCompleteScreen() {
//
//        Intent intent = new Intent(ActivityChecklist.this, ActivityComplete.class);
//        intent.putExtra(ActivityComplete.ARG_JOB_ORDER, jobOrder);
//        intent.putExtra(ActivityComplete.ARG_FROM_HOME, false);
//        startActivity(intent);
//    }

    //New version
    private void goToCompleteScreen() {

//        Intent intent = new Intent(ActivityChecklist.this, ActivityComplete2.class);
        Intent intent = new Intent(ActivityChecklist.this, ActivityCompleteJODetails.class);
        intent.putExtra(ActivityComplete.ARG_JOB_ORDER, jobOrder);
        intent.putExtra(ActivityComplete.ARG_FROM_HOME, false);
        startActivity(intent);
    }

    private void showNewStatusDialog() {

        FragmentDialogUpdateStatus dialog = FragmentDialogUpdateStatus.createInstance(REQUEST_DIALOG_UPDATE);
        dialog.show(getFragmentManager(), null);

    }

    @Override
    public void onDialogDismiss(int requestCode, Bundle bundle) {

        if (bundle != null) {

            String newStatus = bundle.getString(FragmentDialogUpdateStatus.ARG_NEW_STATUS);
            requestUpdate(newStatus);
        }
    }

    //Old version
//    private void goToHome() {
//
//        Intent intent = new Intent(ActivityChecklist.this, ActivityJobOrderList.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//
//    }

    //New version
    private void goToHome() {

        Intent intent = new Intent(ActivityChecklist.this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void launchCamera(int requestCode) {

        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));

        try
        {

            File folder = new File(Environment.getExternalStorageDirectory().toString(), ImageUtility.TEMP_IMAGE_FOLDER);

            if(!folder.exists()) folder.mkdirs();

            File outputFile = new File(String.format("%s/%s/%s.jpeg", Environment.getExternalStorageDirectory().toString(), ImageUtility.TEMP_IMAGE_FOLDER, tempFileName));

            photoUri = Uri.fromFile(outputFile);

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, requestCode);

        }
        catch (Exception e){
            Log.d("Image Exception", e.getMessage());
        }

    }

    private void handleSignature(Intent data) {

        signatureImage = data.getStringExtra(ActivitySignature.ARG_IMAGE_FILE);
        rating = data.getIntExtra(ActivitySignature.ARG_RATING, 0);

        int position = items.size() - 1;
        items.get(position).setIsChecked(true);
        adapter.notifyItemChanged(position);

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    private void handleConfirmPackage(Intent data) {

        packageFee = data.getParcelableExtra(ActivityConfirmPackage.ARG_PACKAGE_FEE);

        int position;

        if (items.size() == 5) {
            position = CHECKLIST_PACKAGE_CONFIRMED;
        } else {
            position = 0;
        }

        items.get(position).setIsChecked(true);
        adapter.notifyItemChanged(position);

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    private void showImageGallery(ArrayList<String> images, int requestCode) {
//
//        Intent intent = new Intent(ActivityChecklist.this, ActivityImageGallery.class);
//        intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, images);
//        intent.putExtra(ActivityImageGallery.ARG_TYPE, ImagePagerAdapter.TYPE_URI);
//        intent.putExtra(ActivityImageGallery.ARG_RETAKE, true);
//        startActivityForResult(intent, requestCode);

        Intent intent = new Intent(ActivityChecklist.this, ActivityImageGallery.class);
        intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, images);
        intent.putExtra(ActivityImageGallery.ARG_TYPE, ImagePagerAdapter.TYPE_URI);
        intent.putExtra(ActivityImageGallery.ARG_RETAKE, true);
        startActivityForResult(intent, requestCode);

    }

    private void updateValidIDChecklist() {

        String validIdImage = photoUri.toString();

        //Put image in attachedItem field of the checklist item
        Bundle bundle = new Bundle();
        bundle.putString("image", validIdImage);
        items.get(clickedPosition).setAttachedItem(bundle);

        items.get(clickedPosition).setIsChecked(true);
        adapter.notifyItemChanged(clickedPosition);


        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    private void updateRecipientPictureChecklist() {


        String recipientPicture = photoUri.toString();

        //Put image in attachedItem field of the checklist item
        Bundle bundle = new Bundle();
        bundle.putString("image", recipientPicture);
        items.get(clickedPosition).setAttachedItem(bundle);

        items.get(clickedPosition).setIsChecked(true);
        adapter.notifyItemChanged(clickedPosition);

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    private int getChecklistItem(String name) {

        int position = -1;
        for (int i = 0; i < items.size(); i++) {

            ChecklistItem item = items.get(i);

            if (item.getTitle().equalsIgnoreCase(name)) {
                position = i;
                break;
            }
        }

        return position;
    }
}
