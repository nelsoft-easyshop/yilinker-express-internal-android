package com.yilinker.expressinternal.mvp.presenter.images;

import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.images.IImageGalleryView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick-villanueva on 3/22/2016.
 */
public class ImageGalleryPresenter extends BasePresenter<List<String>,IImageGalleryView> implements IImageGalleryPresenter {


    @Override
    protected void updateView() {

    }

    @Override
    public void reloadGallery(String photoFile) {
        model.clear();
        model.add(photoFile);
        view().reloadGallery();
    }

    @Override
    public void launchCamera() {
        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));
        File outputFile = new File(android.os.Environment.getExternalStorageDirectory(), tempFileName);

        view().launchCamera(outputFile);

    }

    @Override
    public void addAllImage(List<String> images) {
        model.addAll(images);
    }
}
