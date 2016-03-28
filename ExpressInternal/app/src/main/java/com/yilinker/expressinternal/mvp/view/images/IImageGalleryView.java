package com.yilinker.expressinternal.mvp.view.images;

import java.io.File;

/**
 * Created by patrick-villanueva on 3/22/2016.
 */
public interface IImageGalleryView {

    public void reloadGallery();
    public void launchCamera(File outputFile);
}
