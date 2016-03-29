package com.yilinker.expressinternal.mvp.view.images;

import android.net.Uri;

import java.io.File;

/**
 * Created by patrick-villanueva on 3/22/2016.
 */
public interface IImageGalleryView {

    public void reloadGallery(String photoFile);
//    public void reloadGallery();
//    public void launchCamera(File outputFile);
    public void launchCamera(Uri photoUri);
}
