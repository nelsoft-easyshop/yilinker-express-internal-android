package com.yilinker.expressinternal.mvp.view.images;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.images.ImageGalleryPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragmentActivity;

import java.util.List;

/**
 * Created by patrick-villanueva on 3/22/2016.
 */
public class ActivityImageGallery extends BaseFragmentActivity implements IImageGalleryView , View.OnClickListener{

    private final static String KEY_PHOTO_URI = "photoUri";

    public static final String ARG_RETAKE = "retake";
    public static final String ARG_IMAGES = "images";
    public static final String ARG_TYPE = "type";
    public static final String ARG_NEW_PHOTO = "newPhoto";
    public static final String ARG_PAGE_NAME = "pageName";

    private static final int REQUEST_CAMERA = 1000;

    private final static int PAGES = 5;
    private final static int LOOPS = 10;
    private final static int FIRST_PAGE = PAGES * LOOPS / 2;

    private ImagePagerAdapter adapter;
    private ImageButton btnRetake;

    private List<String> images;
    private String type;
    private String pageName;
    private boolean retake;

    private ImageGalleryPresenter presenter;

    private static Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_2);

        if (savedInstanceState == null){

            Log.i("RESULT","Create presenter");
            presenter = new ImageGalleryPresenter();
            getData();
            initializeViews(null);
            presenter.bindView(this);
            presenter.setModel(images);

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
            Log.i("RESULT","restore presenter");

            if (presenter==null){
                Log.i("RESULT","restoring null presenter");
                presenter = PresenterManager.getInstance().restorePresenter(savedInstance);

            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (presenter==null){
            Log.i("RESULT","saving null presenter");

        }

        savedInstance = new Bundle();
        PresenterManager.getInstance().savePresenter(presenter, outState);
        Log.i("RESULT","presenter saved");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        savedInstance = new Bundle();
        PresenterManager.getInstance().savePresenter(presenter, savedInstance);
    }

    @Override
    public void initializeViews(View parent) {

        ImageView ivClose = (ImageView) findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);

        setPageName(pageName);
        setPager();

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    private void getData(){
        Intent intent = getIntent();

        pageName = getString(R.string.images);

        images = intent.getStringArrayListExtra(ARG_IMAGES);
        type = intent.getStringExtra(ARG_TYPE);
        retake = intent.getBooleanExtra(ARG_RETAKE, false);
        if (intent.hasExtra(ARG_PAGE_NAME)) {
            pageName = intent.getStringExtra(ARG_PAGE_NAME);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("RESULT","resume");
        presenter.bindView(this);
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

    private void setPageName(String pageName){
        TextView tvPageName = (TextView) findViewById(R.id.tvPageName);
        tvPageName.setText(pageName);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnCamera:
                presenter.launchCamera();
                break;

            case R.id.ivClose:
                onBackPressed();
                break;

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //bind the presenter
        Log.i("RESULT","reload gallery");
        presenter.bindView(this);

        if(resultCode == RESULT_OK){

            presenter.reloadGallery();

        }
        else{

            setResult(RESULT_CANCELED);
        }

    }
    @Override
    public void reloadGallery(String photoFile) {
        adapter.notifyDataSetChanged();

        Intent intent = new Intent();
        intent.putExtra(ARG_NEW_PHOTO, photoFile);

        setResult(RESULT_OK, intent);
    }

    @Override
    public void launchCamera(Uri photoUri) {


        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
}
