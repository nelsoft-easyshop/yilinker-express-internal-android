
package com.yilinker.expressinternal.mvp.view.checklist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.checklist.FragmentDialogUpdateStatus2;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.controllers.signature.ActivitySignature;
import com.yilinker.expressinternal.interfaces.RequestOngoingListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.Rider;
import com.yilinker.expressinternal.mvp.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.model.DeliveryPackage;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.checklist.ChecklistDeliveryPresenter;
import com.yilinker.expressinternal.mvp.presenter.checklist.ChecklistPickupPresenter;
import com.yilinker.expressinternal.mvp.view.confirmpackage.ActivityConfirmPackage;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ActivityCompleteJODetails;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;
import com.yilinker.expressinternal.service.ServiceDeliveryChecklist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class FragmentChecklistDelivery extends ChecklistBaseFragment<ChecklistDeliveryPresenter> implements IChecklistDeliveryView, View.OnClickListener{

    private static final int REQUEST_VALID_ID = 100;
    private static final int REQUEST_RECIPIENT_PICTURE = 102;
    private static final int REQUEST_SIGNATURE = 103;
    private static final int REQUEST_IMAGE_GALLERY = 1000;

    private ChecklistDeliveryPresenter presenter;

    private ChecklistItemAdapter adapter;

    private Button btnComplete;


    private RequestOngoingListener reqeuestListener;

    public static FragmentChecklistDelivery createInstance(JobOrder jobOrder){

        FragmentChecklistDelivery fragment = new FragmentChecklistDelivery();

        Bundle args = new Bundle();
        args.putParcelable(ARG_JOB_ORDER, jobOrder);

        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_joborder_checklist, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new ChecklistDeliveryPresenter();

            initializeViews(view);

            presenter.bindView(this);

            //For creating the checklist
            presenter.onViewCreated(getData(), getTitles(R.array.checklist_delivery), getTitleWithData(R.array.checklist_delivery_with_data), getString(R.string.checklist_delivery_payment));

        }

        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        reqeuestListener = (RequestOngoingListener) activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void onResume() {

        presenter.bindView(this);
        super.onResume();
    }

    @Override
    public void onPause() {

        presenter.onPause();
        presenter.unbindView();

        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        presenter.bindView(this);

        if(resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case REQUEST_RECIPIENT_PICTURE:

                    presenter.onRecipientPictureResult();
                    break;

                case REQUEST_SIGNATURE:

                    String signature = data.getStringExtra(ActivitySignature.ARG_IMAGE_FILE);
                    presenter.onSignatureResult(signature);
                    break;

                case REQUEST_VALID_ID:

                    presenter.onValidIdResult();
                    break;

                case (REQUEST_IMAGE_GALLERY + REQUEST_VALID_ID):

                    String idUri = data.getStringExtra(ActivityImageGallery.ARG_NEW_PHOTO);
                    presenter.onValidIdRetake(idUri);
                    break;

                case (REQUEST_IMAGE_GALLERY + REQUEST_RECIPIENT_PICTURE):

                    String pictureUri = data.getStringExtra(ActivityImageGallery.ARG_NEW_PHOTO);
                    presenter.onRecipientPictureRetake(pictureUri);
                    break;
            }
        }

    }

    @Override
    public void initializeViews(View parent) {

        btnComplete = (Button) parent.findViewById(R.id.btnConfirm);

        RecyclerView rvChecklist = (RecyclerView) parent.findViewById(R.id.rvChecklist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvChecklist.setLayoutManager(layoutManager);

        adapter = new ChecklistItemAdapter(this);
        rvChecklist.setAdapter(adapter);

        btnComplete.setText(getString(R.string.checklist_delivery_complete));
        btnComplete.setOnClickListener(this);
    }

    @Override
    public void showLoader(boolean isShown) {

    }


    @Override
    public void goToMainScreen() {

        Intent intent = new Intent(getActivity(), ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    public void startDeliveryService(DeliveryPackage deliveryPackage) {

        Intent service = new Intent(getActivity(), ServiceDeliveryChecklist.class);
        service.putExtra(ServiceDeliveryChecklist.ARG_DELIVERY_JO, deliveryPackage);

        getActivity().startService(service);

    }

    @Override
    public String compressImage(String path) {

        String compressedImage = ImageUtility.compressCameraFileBitmap(path, getActivity());

        return compressedImage;
    }

    @Override
    public void showCaptureImageScreen(List<String> uri, int type) {


        Intent intent = new Intent(getActivity(), ActivityImageGallery.class);
        intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, (ArrayList<String>) uri);
        intent.putExtra(ActivityImageGallery.ARG_TYPE, ImagePagerAdapter.TYPE_URI);
        intent.putExtra(ActivityImageGallery.ARG_RETAKE, true);

        int requestCode = REQUEST_IMAGE_GALLERY + getRequestCode(type);
        startActivityForResult(intent, requestCode);

    }

    @Override
    public void launchCamera(Uri uri, int type) {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        int requestCode = getRequestCode(type);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void showSignatureScreen(String uri) {

        Intent intent = new Intent(getActivity(), ActivitySignature.class);
        intent.putExtra(ActivitySignature.ARG_IMAGE_FILE, uri);

        startActivityForResult(intent, REQUEST_SIGNATURE);
    }

    @Override
    public void addRequest(Request request) {
        addRequestToQueue(request);
    }

    @Override
    public void cancelRequest(List<String> requests) {

        cancelRequests(requests);
    }

    @Override
    public void goToCompleteScreen(JobOrder joborder) {

        Intent intent = new Intent(getActivity(), ActivityCompleteJODetails.class);
        intent.putExtra(ActivityCompleteJODetails.KEY_JOB_ORDER, joborder);

        startActivity(intent);
    }

    @Override
    public void loadChecklistItems(List<ChecklistItem> items) {

        adapter.clearAndAddAll(items);
    }

    @Override
    public void updateItem(ChecklistItem item) {

        adapter.updateItem(item);
        presenter.onUpdateChecklistItem(item);
    }

    @Override
    public void enableCompleteButton(boolean isEnabled) {

        btnComplete.setEnabled(isEnabled);
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showScreenLoader(boolean showLoader) {

        String text = null;

        if(showLoader){

            text = getString(R.string.completing_delivery);

        }
        else{

            text = getString(R.string.checklist_delivery_complete);
        }

        reqeuestListener.onRequestOngoing(showLoader);
        btnComplete.setText(text);
    }

    @Override
    public void onItemClick(int position, ChecklistItem object) {

        String validId = getString(R.string.checklist_delivery_valid_id);
        String recipientPicture = getString(R.string.checklist_delivery_picture);
        String signature = getString(R.string.checklist_delivery_signature);

        if(object.getTitle().equalsIgnoreCase(validId)){

            presenter.onValidIdClick(object);

        }
        else if(object.getTitle().equalsIgnoreCase(recipientPicture)){

            presenter.onRecipientPictureClick(object);

        }
        else if(object.getTitle().equalsIgnoreCase(signature)){

            presenter.onSignatureImageClick(object);

        }
        else {

            updateItem(object);

        }

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnConfirm:

                presenter.onCompleteButtonClick();
                break;
        }
    }

    private int getRequestCode(int type){

        int requestCode = 0;

        switch (type){

            case ChecklistDeliveryPresenter.TYPE_RECIPIENT_PIC:

                requestCode = REQUEST_RECIPIENT_PICTURE;
                break;

            case ChecklistDeliveryPresenter.TYPE_VALID_ID:

                requestCode = REQUEST_VALID_ID;
                break;

        }

        return requestCode;

    }
}
