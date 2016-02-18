package com.yilinker.expressinternal.controllers.images;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class ActivityImageGallery extends BaseActivity {

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
    private ViewPager pager;
    private ImageButton btnRetake;

    private List<String> images;
    private String type;

    private boolean retake;
    private boolean hasNewPhoto;

    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        getData();
        initViews();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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
    protected void handleRefreshToken() {

    }

    private void initViews(){

        setPager();

        //For Action Bar
        setActionBarTitle("Images");
        setActionBarBackgroundColor(R.color.marigold);

    }

    private void setPager(){

        pager = (ViewPager) findViewById(R.id.vpImage);

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

    private void getData(){

        Intent intent = getIntent();

        images = intent.getStringArrayListExtra(ARG_IMAGES);
        type = intent.getStringExtra(ARG_TYPE);
        retake = intent.getBooleanExtra(ARG_RETAKE, false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnCamera:

                launchCamera(REQUEST_CAMERA);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK){

            reloadGallery();

            Intent intent = new Intent();
            intent.putExtra(ARG_NEW_PHOTO, photoUri.toString());

            setResult(RESULT_OK, intent);

        }
        else{

            setResult(RESULT_CANCELED);
        }

    }

    private void launchCamera(int requestCode){

        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));
        File outputFile = new File(android.os.Environment.getExternalStorageDirectory(), tempFileName);

        photoUri = Uri.fromFile(outputFile);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, requestCode);


    }

    private void reloadGallery(){

        String photoFile = photoUri.toString();

        images.clear();
        images.add(photoFile);
        adapter.notifyDataSetChanged();


    }
}
