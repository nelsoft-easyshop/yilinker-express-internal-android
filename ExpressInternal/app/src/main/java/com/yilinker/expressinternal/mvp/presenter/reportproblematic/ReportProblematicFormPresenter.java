package com.yilinker.expressinternal.mvp.presenter.reportproblematic;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.model.express.internal.ProblematicJobOrder;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.reportproblematic.IReportProblematicFormView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick-villanueva on 3/29/2016.
 */
public class ReportProblematicFormPresenter extends RequestPresenter<ProblematicJobOrder,IReportProblematicFormView> implements
        IReportProblematicFormPresenter {

    private static final int REQUEST_SUBMIT_REPORT = 2000;
    private static final String TAG_SUBMIT_REPORT = "submit-report";
    private ArrayList<String> images;
    private Uri photoUri;

    private String jobOrderNo;
    private ProblematicType problematicType;

    @Override
    protected void updateView() {

    }


    @Override
    public void onCreate() {
        images = new ArrayList<>();
    }

    @Override
    public void goToImages() {
        view().goToImages(images);
    }

    @Override
    public void addImageGallery(Uri image) {
        if (image==null)
        {
            images.add(photoUri.toString());

        }else {
            images.add(image.toString());

        }
    }

    @Override
    public void setJobOrderNo(String jobOrderNo) {
        this.jobOrderNo = jobOrderNo;
    }

    @Override
    public void setSelectedProblematicType(ProblematicType type) {
        this.problematicType = type;
        view().setSelectedProblematicType(problematicType.getType());
    }

    @Override
    public void launchCamera() {
        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));
        File outputFile = new File(android.os.Environment.getExternalStorageDirectory(), tempFileName);

        photoUri = Uri.fromFile(outputFile);

        view().launchCamera(photoUri);

    }

    @Override
    public void submitReport(String remarks) {

        view().showLoader(true);

        List<String> files = new ArrayList<>();
        Uri uri = null;
        for(String item : images){

            uri = Uri.parse(item);
            files.add(getRealPathFromURI(uri));

        }

        ProblematicJobOrder report = new ProblematicJobOrder();
        report.setProblemType(problematicType.getType());
        report.setNotes(remarks);
        report.setImages(files);
        report.setJobOrderNo(jobOrderNo);

        Request request = JobOrderAPI.reportProblematic(REQUEST_SUBMIT_REPORT, report, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        view().addRequestToQueue(request);
    }

    @Override
    public void onPause() {
        view().cancelRequests(getRequestTags());
    }


    private List<String> getRequestTags(){
        List<String> lists = new ArrayList<>();
        lists.add(TAG_SUBMIT_REPORT);

        return lists;
    }

    public String getRealPathFromURI(Uri contentURI) {

        if(contentURI.toString().contains("file")) {

//            File file = new File(contentURI.toString());
            return ImageUtility.compressCameraFileBitmap(contentURI.getEncodedPath());

        }
        else{

            String path = null;
            String result = null;
            String[] projection = {MediaStore.Images.Media.DATA};

            try {
                Cursor cursor = view().getCursor(contentURI, projection);
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

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_SUBMIT_REPORT:
                ImageUtility.clearCache();
                view().onSuccess((String) object);
                view().showLoader(false);
                break;

            default:
                break;

        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case REQUEST_SUBMIT_REPORT:
                ImageUtility.clearCache();
                view().showErrorMessage(message);
                view().showLoader(false);
                break;

            default:
                break;

        }
    }

}
