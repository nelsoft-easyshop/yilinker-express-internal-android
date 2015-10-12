package com.yilinker.expressinternal.controllers.checklist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityComplete;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.controllers.signature.ActivitySignature;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.model.JobOrder;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class ActivityChecklist extends BaseActivity implements RecyclerViewClickListener<ChecklistItem>, ResponseHandler, DialogDismissListener{

    public static final String ARG_JOB_ORDER = "jobOrder";

    private static final int REQUEST_DIALOG_UPDATE = 2000;

    private static final int REQUEST_SIGNATURE = 1000;
    private static final int REQUEST_SUBMIT_SIGNATURE = 1001;
    private static final int REQUEST_SUBMIT_RATING = 1002;
    private static final int REQUEST_UPDATE = 1003;
    private static final int REQUEST_UPLOAD_IMAGES = 1004;

    private static final int REQUEST_LAUNCH_CAMERA_ID = 2000;
    private static final int REQUEST_LAUNCH_CAMERA_PICTURE = 2001;

    private static final int CHECKLIST_VALID_ID = 2;
    private static final int CHECKLIST_RECIPIENT_PICTURE = 3;
    private static final int CHECKLIST_SIGNATURE = 4;

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

    private Uri photoUri;
    private String validIdImage;
    private String recipientPicture;
    private String newStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

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
    public void onItemClick(int position, ChecklistItem object) {

        String checkListItem = object.getTitle();

        if(checkListItem.equalsIgnoreCase(getString(R.string.checklist_delivery_signature))){

            if(!items.get(CHECKLIST_SIGNATURE).isChecked()) {
                showSignature();
            }
            else{

                //TODO Show signature image
            }

            return;
        }
        else if(checkListItem.equalsIgnoreCase(getString(R.string.checklist_delivery_valid_id))){

            if(!items.get(CHECKLIST_VALID_ID).isChecked()) {

                launchCamera(REQUEST_LAUNCH_CAMERA_ID);
            }
            else{

                ArrayList<String> images = new ArrayList<>();
                images.add(validIdImage);

                showImageGallery(images);

            }

            return;
        }
        else if(checkListItem.equalsIgnoreCase(getString(R.string.checklist_delivery_picture))){

            if(!items.get(CHECKLIST_RECIPIENT_PICTURE).isChecked()) {

                launchCamera(REQUEST_LAUNCH_CAMERA_PICTURE);
            }
            else {

                ArrayList<String> images = new ArrayList<>();
                images.add(recipientPicture);

                showImageGallery(images);
            }

            return;
        }


        object.setIsChecked(!object.isChecked());
        adapter.notifyItemChanged(position);

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){

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
            }
        }

    }

    private void initViews(){

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
        if(jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            buttonText = getString(R.string.checklist_delivery_complete);
        }
        else if(jobOrder.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            buttonText = getString(R.string.checklist_pickup_complete);
        }

        btnConfirm.setText(buttonText);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnConfirm:

                handleCompleteButton();
                break;
        }

    }

    private void bindViews(){

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

        ImageUtility.clearCache();

        switch (requestCode){

            case REQUEST_SUBMIT_SIGNATURE:

//                requestSubmitRating();
                requestUploadImages();
                break;

            case REQUEST_SUBMIT_RATING:

                requestUploadImages();
                break;

            case REQUEST_UPLOAD_IMAGES:

                requestUpdate(JobOrderConstant.JO_COMPLETE);
                break;

            case REQUEST_UPDATE:

                String status = jobOrder.getStatus();
                if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

                    Toast.makeText(getApplicationContext(), "Job order status is updates!", Toast.LENGTH_LONG).show();
                    goToHome();
                }
                else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

                    Toast.makeText(getApplicationContext(), "Job order is completed!", Toast.LENGTH_LONG).show();
                    goToCompleteScreen();
                    finish();
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

            case REQUEST_UPLOAD_IMAGES:

                requestUploadImages();
                break;

            case REQUEST_UPDATE:

                requestUpdate(newStatus);
                break;
        }


    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        ImageUtility.clearCache();

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
    }

    private void setAdapter(){

        items = new ArrayList<>();

        String status = jobOrder.getStatus();

        String[] arrayItems = null;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)) {
            arrayItems = getResources().getStringArray(R.array.checklist_delivery);
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)){
            arrayItems = getResources().getStringArray(R.array.checklist_claiming);
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){
            arrayItems = getResources().getStringArray(R.array.checklist_pickup);
        }

        ChecklistItem item = null;
        int size = arrayItems.length;
        for(int i = 0; i < size; i++){

            item = new ChecklistItem();
            item.setTitle(arrayItems[i]);

            items.add(item);
        }


        adapter = new AdapterChecklist(items, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChecklist.setLayoutManager(layoutManager);
        rvChecklist.setAdapter(adapter);

    }

    private boolean isComplete(){

        boolean isComplete = true;
        for(ChecklistItem item : items){

            if(!item.isChecked()){
                isComplete = false;
                break;
            }

        }

        return isComplete;
    }

    private void setConfirmButton(boolean isEnabled){

        int textColor = 0;

        if(isEnabled){

            btnConfirm.setBackgroundResource(R.drawable.bg_btn_orangeyellow);
            textColor = getResources().getColor(R.color.white);
        }
        else{

            btnConfirm.setBackgroundResource(R.color.white_gray);
            textColor = getResources().getColor(R.color.warm_gray);
        }


        btnConfirm.setTextColor(textColor);
        btnConfirm.setEnabled(isEnabled);
    }


    private void showSignature(){

        Intent intent = new Intent(ActivityChecklist.this, ActivitySignature.class);
        startActivityForResult(intent, REQUEST_SIGNATURE);

    }

    private void getData(){

        jobOrder = getIntent().getParcelableExtra(ARG_JOB_ORDER);

    }

    private void handleCompleteButton(){

        String type = jobOrder.getType();
        String status = jobOrder.getStatus();

        if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY)){



        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP)){


        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            showNewStatusDialog();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            requestSubmitSignature();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)){


        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)){


        }

    }

    private void requestSubmitSignature(){

        rlProgress.setVisibility(View.VISIBLE);

        String image = convertSignatureToString();

        Request request = JobOrderAPI.uploadSignature(REQUEST_SUBMIT_SIGNATURE, jobOrder.getJobOrderNo(), image, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestSubmitRating(){

        Request request = JobOrderAPI.addRating(REQUEST_SUBMIT_RATING, jobOrder.getJobOrderNo(), rating, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestUploadImages(){

        List<String> images = new ArrayList<>();

        Uri uri = Uri.parse(validIdImage);
//        images.add(ImageUtility.compressCameraFileBitmap(uri.getEncodedPath()));
        images.add(ImageUtility.compressCameraFileBitmap(uri.getEncodedPath()));

        uri = Uri.parse(recipientPicture);
//        images.add(ImageUtility.compressCameraFileBitmap(uri.getEncodedPath()));
        images.add(ImageUtility.compressCameraFileBitmap(uri.getEncodedPath()));

        Request request = JobOrderAPI.uploadJobOrderImages(REQUEST_UPLOAD_IMAGES, jobOrder.getWaybillNo(), images, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestUpdate(String newStatus){

        this.newStatus = newStatus;

        Request request = JobOrderAPI.updateStatus(REQUEST_UPDATE, jobOrder.getJobOrderNo(), newStatus, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private String convertSignatureToString(){

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

    private void goToCompleteScreen(){

        Intent intent = new Intent(ActivityChecklist.this, ActivityComplete.class);
        intent.putExtra(ActivityComplete.ARG_JOB_ORDER, jobOrder);
        intent.putExtra(ActivityComplete.ARG_FROM_HOME, false);
        startActivity(intent);
    }

    private void showNewStatusDialog(){

        FragmentDialogUpdateStatus dialog = FragmentDialogUpdateStatus.createInstance(REQUEST_DIALOG_UPDATE);
        dialog.show(getFragmentManager(), null);

    }

    @Override
    public void onDialogDismiss(int requestCode, Bundle bundle) {

        if(bundle != null) {

            String newStatus = bundle.getString(FragmentDialogUpdateStatus.ARG_NEW_STATUS);
            requestUpdate(newStatus);
        }
    }

    private void goToHome(){

        Intent intent = new Intent(ActivityChecklist.this, ActivityJobOrderList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void launchCamera(int requestCode){

//        try {
//
//            File outputDir = getExternalCacheDir();
//            File outputFile = File.createTempFile("image", ".jpg", outputDir);

        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));
        File outputFile = new File(android.os.Environment.getExternalStorageDirectory(), tempFileName);

        photoUri = Uri.fromFile(outputFile);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, requestCode);

//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void handleSignature(Intent data){

        signatureImage = data.getStringExtra(ActivitySignature.ARG_IMAGE_FILE);
        rating = data.getIntExtra(ActivitySignature.ARG_RATING, 0);

        Toast.makeText(getApplicationContext(), signatureImage, Toast.LENGTH_LONG).show();

        int position = items.size() - 1;
        items.get(position).setIsChecked(true);
        adapter.notifyItemChanged(position);

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    private void showImageGallery(ArrayList<String> images){

        Intent intent = new Intent(ActivityChecklist.this, ActivityImageGallery.class);
        intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, images);
        intent.putExtra(ActivityImageGallery.ARG_TYPE, ImagePagerAdapter.TYPE_URI);
        startActivity(intent);

    }

    private void updateValidIDChecklist(){

        int position = CHECKLIST_VALID_ID;
        items.get(position).setIsChecked(true);
        adapter.notifyItemChanged(position);

        validIdImage = photoUri.toString();

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

    private void updateRecipientPictureChecklist(){

        int position = CHECKLIST_RECIPIENT_PICTURE;
        items.get(position).setIsChecked(true);
        adapter.notifyItemChanged(position);

        recipientPicture = photoUri.toString();

        //Check if all items are checked to enable Confirm button
        setConfirmButton(isComplete());

    }

//    private void showImageGallery(){
//
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        galleryIntent.setType("image/*");
//        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),
//                REQUEST_SHOW_GALLERY);
//    }
}
