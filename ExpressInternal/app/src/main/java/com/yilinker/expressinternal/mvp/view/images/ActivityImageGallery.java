package com.yilinker.expressinternal.mvp.view.images;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.images.ImageGalleryPresenter;
import com.yilinker.expressinternal.mvp.view.BaseActivity;
import com.yilinker.expressinternal.mvp.view.images.IImageGalleryView;

import java.io.File;
import java.util.List;

/**
 * Created by patrick-villanueva on 3/22/2016.
 */
public class ActivityImageGallery extends BaseActivity implements IImageGalleryView {

    private final static String KEY_PHOTO_URI = "photoUri";

    public static final String ARG_RETAKE = "retake";
    public static final String ARG_IMAGES = "images";
    public static final String ARG_TYPE = "type";
    public static final String ARG_NEW_PHOTO = "newPhoto";

    private static final int REQUEST_CAMERA = 1000;

    private final static int PAGES = 5;
    private final static int LOOPS = 10;
    private final static int FIRST_PAGE = PAGES * LOOPS / 2;

    private ImagePagerAdapter adapter;
    private ImageButton btnRetake;

    private List<String> images;
    private String type;

    private boolean retake;
    private boolean hasNewPhoto;

    private Uri photoUri;

    private ImageGalleryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setActionBarLayout(R.layout.layout_toolbar_registration);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        if (savedInstanceState == null){
            presenter = new ImageGalleryPresenter();
            Log.i("RESULT","new");

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

            Log.i("RESULT","restore");
        }
        getData();
        initializeViews(null);

        presenter.bindView(this);
        presenter.addAllImage(images);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);

        if (photoUri!=null){
            outState.putString(KEY_PHOTO_URI, photoUri.toString());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            photoUri = Uri.parse(savedInstanceState.getString(KEY_PHOTO_URI));

        }

    }

    @Override
    public void initializeViews(View parent) {
        super.initializeViews(parent);

        setPager();
    }

    private void getData(){
        Intent intent = getIntent();

        images = intent.getStringArrayListExtra(ARG_IMAGES);
        type = intent.getStringExtra(ARG_TYPE);
        retake = intent.getBooleanExtra(ARG_RETAKE, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    private void setPager(){

        ViewPager pager = (ViewPager) findViewById(R.id.vpImage);

        adapter = new ImagePagerAdapter(getApplicationContext(), images, type);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(adapter);
        btnRetake = (ImageButton) findViewById(R.id.btnCamera);


//        // Set current item to the middle page so we can fling to both
//        // directions left and right
//        pager.setCurrentItem(FIRST_PAGE);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        pager.setPageMargin(-300);

        if(retake){
            btnRetake.setVisibility(View.VISIBLE);
            btnRetake.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnCamera:
                presenter.launchCamera();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK){

            String photoFile = photoUri.toString();

            Log.i("RESULT","reload gallery");
            presenter.reloadGallery(photoFile);

            Intent intent = new Intent();
            intent.putExtra(ARG_NEW_PHOTO, photoUri.toString());

            setResult(RESULT_OK, intent);

        }
        else{

            setResult(RESULT_CANCELED);
        }

    }

    @Override
    public void reloadGallery() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void launchCamera(File outputFile) {

        photoUri = Uri.fromFile(outputFile);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
}
