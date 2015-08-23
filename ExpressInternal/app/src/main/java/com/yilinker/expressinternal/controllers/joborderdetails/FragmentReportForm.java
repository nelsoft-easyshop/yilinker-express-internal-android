package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.yilinker.core.base.BaseFragment;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;

import java.io.File;
import java.io.IOException;

/**
 * Created by J.Bautista
 */
public class FragmentReportForm extends BaseFragment implements View.OnClickListener, ResponseHandler{

    public static final String ARG_TYPE = "type";

    private static final int REQUEST_LAUNCH_CAMERA = 1000;
    private static final int REQUEST_SHOW_GALLERY  = 1001;

    private TextView tvTitle;
    private ImageButton btnCamera;
    private Button btnSubmit;
    private EditText etRemarks;

    private int type;
    private Uri photoUri;

    public static FragmentReportForm createInstance(Bundle bundle){

        FragmentReportForm fragment = new FragmentReportForm();

        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_problematic_reportform, null);

        return view;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {

        btnCamera = (ImageButton) view.findViewById(R.id.btnCamera);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        etRemarks = (EditText) view.findViewById(R.id.etRemarks);

        String title = null;

        switch (type){

            case JobOrderConstant.PROBLEMATIC_DAMAGED:

                title = getString(R.string.damaged_upon_delivery);
                break;

            case JobOrderConstant.PROBLEMATIC_RECIPIENT_NOT_FOUND:

                title = getString(R.string.recipient_not_found);
                break;

            case JobOrderConstant.PROBLEMATIC_REJECTED:

                title = getString(R.string.delivery_rejected);
                break;

            case JobOrderConstant.PROBLEMATIC_UNABLE_TO_PAY:

                title = getString(R.string.unable_to_pay);
                break;

        }

        tvTitle.setText(title);

        btnCamera.setOnClickListener(this);

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

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){

            switch (requestCode){

                case REQUEST_SHOW_GALLERY:

                    photoUri = data.getData();

                    break;
            }

        }
        else{

            //TODO Show error message
        }


    }


    @Override
    public void onSuccess(int requestCode, Object object) {

    }

    @Override
    public void onFailed(int requestCode, String message) {

    }

    private void getData(){

        Bundle bundle = getArguments();
        type = bundle.getInt(ARG_TYPE);

    }

    private void launchCamera(){

        try {

            File outputDir = getActivity().getExternalCacheDir();
            File outputFile = File.createTempFile("image", ".jpg", outputDir);

//            photoPath = outputFile.getPath();

            photoUri = Uri.fromFile(outputFile);

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_LAUNCH_CAMERA);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showImageGallery(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SHOW_GALLERY);
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

        File file = new File(photoUri.toString());
        String remarks = etRemarks.getText().toString();

        //TODO Submit report
    }

}
