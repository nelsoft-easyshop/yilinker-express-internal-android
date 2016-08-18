package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.base.BaseFragment;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.express.internal.ProblematicJobOrder;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class FragmentReportForm extends BaseFragment implements View.OnClickListener, ResponseHandler{

    public static final String ARG_TYPE = "type";
    public static final String ARG_JONUMBER = "jobOrderNo";
    public static final String ARG_WAYBILL_NUMBER = "waybill-number";
    private final static String KEY_PHOTO_URI = "photoUri";
    private final static String KEY_IMAGES_LIST = "images";

    private static final int REQUEST_LAUNCH_CAMERA = 1000;
    private static final int REQUEST_SHOW_GALLERY  = 1001;

    private RelativeLayout rlProgress;
    private TextView tvTitle;
    private ImageButton btnCamera;
    private Button btnSubmit;
    private EditText etRemarks;
    private TextView tvViewImages;

    private ArrayList<String> images;
    private String[] imageFiles;

    private int type;
    private String jobOrderNo;
    private String waybillNumber;

    private String problemType;
    private Uri photoUri;

    private RequestQueue requestQueue;
    private SyncDBTransaction syncDBTransaction;
    private ProblematicJobOrder report;


    public static FragmentReportForm createInstance(Bundle bundle){

        FragmentReportForm fragment = new FragmentReportForm();

        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();
        images = new ArrayList<>();
        syncDBTransaction = new SyncDBTransaction(getActivity());

        getData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_problematic_reportform, null);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (photoUri != null) {

            outState.putString(KEY_PHOTO_URI, photoUri.toString());
            outState.putStringArrayList(KEY_IMAGES_LIST, (ArrayList) images);
        }

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {

            photoUri = Uri.parse(savedInstanceState.getString(KEY_PHOTO_URI));
            images.clear();
            images.addAll(savedInstanceState.getStringArrayList(KEY_IMAGES_LIST));

        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null) {

            photoUri = Uri.parse(savedInstanceState.getString(KEY_PHOTO_URI));

        }

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {

            photoUri = Uri.parse(savedInstanceState.getString(KEY_PHOTO_URI));

        }
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {

        rlProgress = (RelativeLayout) view.findViewById(R.id.rlProgress);
        btnCamera = (ImageButton) view.findViewById(R.id.btnCamera);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        etRemarks = (EditText) view.findViewById(R.id.etRemarks);
        tvViewImages = (TextView) view.findViewById(R.id.tvViewImages);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);


        switch (type){

            case JobOrderConstant.PROBLEMATIC_DAMAGED:

                problemType = getString(R.string.problematic_option_other_problems);
                break;

            case JobOrderConstant.PROBLEMATIC_RECIPIENT_NOT_FOUND:

                problemType = getString(R.string.problematic_option_recipient_not_found);
                break;

            case JobOrderConstant.PROBLEMATIC_REJECTED:

                problemType = getString(R.string.problematic_option_recipient_refused_to_accept);
                break;

            case JobOrderConstant.PROBLEMATIC_UNABLE_TO_PAY:

                problemType = getString(R.string.problematic_option_payment_not_yet_ready);
                break;

        }

        tvTitle.setText(problemType);

        btnCamera.setOnClickListener(this);
        tvViewImages.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        tvViewImages.setVisibility(View.GONE);
        rlProgress.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){

            case R.id.btnCamera:

                uploadPhoto();
                break;

            case R.id.btnSubmit:

                submitReport();
                break;

            case R.id.tvViewImages:

                viewImages();
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){

            switch (requestCode){

                case REQUEST_SHOW_GALLERY:

                    photoUri = data.getData();
                    images.add(photoUri.toString());

                    break;

                case REQUEST_LAUNCH_CAMERA:

                    images.add(photoUri.toString());

                    break;

            }

            if(tvViewImages.getVisibility() == View.GONE){

                tvViewImages.setVisibility(View.VISIBLE);
            }

        }
        else{

            //TODO Show error message
        }


    }




    @Override
    public void onSuccess(int requestCode, Object object) {

//        Toast.makeText(getActivity(), (String)object, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
//
//        ImageUtility.clearCache();
        startProblematicService();
        goToHome();
    }

    @Override
    public void onFailed(int requestCode, String message) {

//        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//
//        ImageUtility.clearCache();
        handleFailedReportProblematic();
        rlProgress.setVisibility(View.GONE);
    }

    private void handleFailedReportProblematic(){

        SyncDBObject request = new SyncDBObject();
        request.setRequestType(ActivityProblematic.REQUEST_SUBMIT_REPORT);
        request.setKey(String.format("%s%s", jobOrderNo, String.valueOf(ActivityProblematic.REQUEST_SUBMIT_REPORT)));
        request.setId(jobOrderNo);
        request.setData(String.format("%s|%s",report.getNotes(), String.valueOf(report.getProblemTypeId())));
        request.setSync(false);

        syncDBTransaction.add(request);
        startProblematicService();
        goToHome();
    }

    private void getData(){

        Bundle bundle = getArguments();
        type = bundle.getInt(ARG_TYPE);
        jobOrderNo = bundle.getString(ARG_JONUMBER);
        waybillNumber = bundle.getString(ARG_WAYBILL_NUMBER);

    }

    private void launchCamera() {

        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));
        File outputFile = new File(android.os.Environment.getExternalStorageDirectory(), tempFileName);

        photoUri = Uri.fromFile(outputFile);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_LAUNCH_CAMERA);

    }

//    private void launchCamera(){
//
//        File outputDir = getActivity().getExternalCacheDir();
//        File outputFile = null;
//
//        try {
//            outputFile = File.createTempFile("image", ".jpg", outputDir);
//
//
////            String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));
////            File outputFile = new File(android.os.Environment.getExternalStorageDirectory(), tempFileName);
//
//            photoUri = Uri.fromFile(outputFile);
//
//            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//            startActivityForResult(intent, REQUEST_LAUNCH_CAMERA);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void showImageGallery(){

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SHOW_GALLERY);

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),
                REQUEST_SHOW_GALLERY);
    }

    private void viewImages(){

        Intent intent = new Intent(getActivity(), ActivityImageGallery.class);
        intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, images);
        intent.putExtra(ActivityImageGallery.ARG_TYPE, ImagePagerAdapter.TYPE_URI);
        startActivity(intent);

    }

    private void uploadPhoto(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload Pictures Option");

        builder.setPositiveButton("Show Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        showImageGallery();
                    }
                });

        builder.setNegativeButton("Launch Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        launchCamera();
                    }
                });

        builder.show();

    }

    private void submitReport(){

        rlProgress.setVisibility(View.VISIBLE);

//        File file = new File(photoUri.toString());
        String remarks = etRemarks.getText().toString();

        List<String> files = new ArrayList<>();
        Uri uri = null;
        imageFiles = new String[images.size()];
        int count = 0;
        for(String item : images){

            uri = Uri.parse(item);
            files.add(getRealPathFromURI(uri));
            imageFiles[count]=getRealPathFromURI(uri);
        }

        report = new ProblematicJobOrder();
        report.setProblemType(problemType);
        report.setNotes(remarks);
        report.setImages(files);
        report.setJobOrderNo(jobOrderNo);
        report.setProblemTypeId(type);

        Request request = JobOrderAPI.reportProblematicJO(ActivityProblematic.REQUEST_SUBMIT_REPORT, report, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);
    }

    private String getRealPathFromURI(Uri contentURI) {

        if(contentURI.toString().contains("file")) {

//            File file = new File(contentURI.toString());
            return ImageUtility.compressCameraFileBitmap(contentURI.getEncodedPath());

        }
        else{

            String path = null;
            String result = null;
            String[] projection = {MediaStore.Images.Media.DATA};

            try {
                Cursor cursor = getActivity().getContentResolver().query(contentURI, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                result = cursor.getString(columnIndex);

                path = ImageUtility.compressCameraFileBitmap(result);

                cursor.close();



            } catch (Exception e) {
                e.printStackTrace();
            }

            return path;
        }
    }

    private void goToHome(){

        Intent intent = new Intent(getActivity(), ActivityJobOrderList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void startProblematicService(){
        Intent i = new Intent(getActivity(), ServiceReportProblematic.class);
        i.putExtra(ActivityProblematic.ARG_PROBLEMATIC_IMAGES, imageFiles);
        i.putExtra(ActivityProblematic.ARG_WAYBILL_NUMBER, waybillNumber);

        getActivity().startService(i);
    }

}
