package com.yilinker.expressinternal.mvp.view.reportproblematic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
//import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.reportproblematic.ReportProblematicFormPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;
import com.yilinker.expressinternal.mvp.view.images.ActivityImageGallery;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick-villanueva on 3/29/2016.
 */
public class FragmentReportProblematicForm extends BaseFragment implements View.OnClickListener,IReportProblematicFormView{

    private final static String ARG_TYPE = "problematic-type";
    private final static String ARG_JO_NUMBER = "job-order-number";
    private static final int REQUEST_LAUNCH_CAMERA = 2000;
    private static final int REQUEST_SHOW_GALLERY = 2001;

    private ReportProblematicFormPresenter presenter;

    private TextView tvViewImages;
    private Button btnConfirmProblematic;
    private EditText etRemark;

    private Bundle savedInstance;

    public static FragmentReportProblematicForm createInstance(ProblematicType problematicType, String jobOrderNo){
        FragmentReportProblematicForm fragment = new FragmentReportProblematicForm();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_TYPE, problematicType);
        bundle.putString(ARG_JO_NUMBER, jobOrderNo);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_problematic_form, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null){

            presenter = new ReportProblematicFormPresenter();

        }else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

            if (presenter == null) {
                presenter = PresenterManager.getInstance().restorePresenter(savedInstance);
            }
        }

        presenter.onCreate();
        initializeViews(view);
        presenter.bindView(this);
        getData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    @Override
    public void initializeViews(View parent) {

        tvViewImages = (TextView) parent.findViewById(R.id.tvViewImages);
        tvViewImages.setOnClickListener(this);

        btnConfirmProblematic = (Button) parent.findViewById(R.id.btnConfirmProblematic);
        btnConfirmProblematic.setOnClickListener(this);

        etRemark = (EditText) parent.findViewById(R.id.etRemarks);

        TextView tvAttachPhoto = (TextView) parent.findViewById(R.id.tvAttachPhoto);
        tvAttachPhoto.setOnClickListener(this);
    }

    private void getData(){

        Bundle bundle = getArguments();
        ProblematicType type = bundle.getParcelable(ARG_TYPE);
        String jobOrderNo = bundle.getString(ARG_JO_NUMBER);

        presenter.setSelectedProblematicType(type);
        presenter.setJobOrderNo(jobOrderNo);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        presenter.unbindView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tvViewImages:
                presenter.goToImages();
                break;

            case R.id.tvAttachPhoto:
                attachPhoto();
                break;

            case R.id.btnConfirmProblematic:
                String remark = etRemark.getText().toString();
                presenter.submitReport(remark);

            default:
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        savedInstance = new Bundle();
        PresenterManager.getInstance().savePresenter(presenter, savedInstance);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case REQUEST_LAUNCH_CAMERA:

                if (resultCode == Activity.RESULT_OK){

                    presenter.addImageGallery(null);
                }
                break;

            case REQUEST_SHOW_GALLERY:
                if (resultCode == Activity.RESULT_OK){

                    presenter.addImageGallery(data.getData());
                }

                break;

            default:
                break;

        }

        /***show view images label if images already have content*/
        if(tvViewImages.getVisibility() == View.GONE){

            tvViewImages.setVisibility(View.VISIBLE);
        }
    }

    private void attachPhoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.image_upload_options));

        builder.setPositiveButton(getString(R.string.image_show_gallery),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        showImageGallery();
                    }
                });

        builder.setNegativeButton(getString(R.string.image_launch_camera),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        presenter.launchCamera();
                    }
                });

        builder.show();
    }

    private void showImageGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.image_select_picture)),
                REQUEST_SHOW_GALLERY);
    }

    @Override
    public void goToImages(ArrayList<String> images){
        Intent intent = new Intent(getActivity(), ActivityImageGallery.class);
        intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, images);
        intent.putExtra(ActivityImageGallery.ARG_TYPE, ImagePagerAdapter.TYPE_URI);
        startActivity(intent);
    }

    @Override
    public void launchCamera(Uri photoUri) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_LAUNCH_CAMERA);

    }

    @Override
    public Cursor getCursor(Uri contentURI, String[] projection) {
        return getActivity().getContentResolver().query(contentURI, projection, null, null, null);
    }

    private void goToMainScreen(){
        Intent intent = new Intent(getActivity(), ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        goToMainScreen();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSelectedProblematicType(String problematicType) {
        TextView tvProblematicType = (TextView) getActivity().findViewById(R.id.tvProblemType);
        tvProblematicType.setText(problematicType);

    }


    @Override
    public void showLoader(boolean isShown) {

        View viewLoader = getActivity().findViewById(R.id.viewLoader);

        if (isShown){
            viewLoader.setVisibility(View.VISIBLE);
            btnConfirmProblematic.setText(getString(R.string.confirm_problematic_submitting));
        }else{
            viewLoader.setVisibility(View.GONE);
            btnConfirmProblematic.setText(getString(R.string.confirm_problematic_2));
        }

    }
}
