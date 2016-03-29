package com.yilinker.expressinternal.mvp.presenter.images;

import android.net.Uri;

import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.images.IImageGalleryView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick-villanueva on 3/22/2016.
 */
public class ImageGalleryPresenter extends BasePresenter<List<String>,IImageGalleryView> implements IImageGalleryPresenter {

    private Uri photoUri;

    @Override
    protected void updateView() {

    }

//    @Override
//    public void reloadGallery(String photoFile) {
//        model.clear();
//        model.add(photoFile);
//        view().reloadGallery();
//    }

    @Override
    public void reloadGallery() {

        String photoFile = photoUri.toString();

        model.clear();
        model.add(photoFile);
        view().reloadGallery(photoFile);
    }


    @Override
    public void launchCamera() {
        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));
        File outputFile = new File(android.os.Environment.getExternalStorageDirectory(), tempFileName);

        photoUri = Uri.fromFile(outputFile);
//        view().launchCamera(outputFile);
        view().launchCamera(photoUri);

    }

}
