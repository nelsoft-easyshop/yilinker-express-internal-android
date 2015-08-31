package com.yilinker.expressinternal.controllers.images;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;

import java.util.List;

/**
 * Created by J.Bautista
 */
public class ActivityImageGallery extends BaseActivity {

    public static final String ARG_IMAGES = "images";

    private final static int PAGES = 5;
    private final static int LOOPS = 10;
    private final static int FIRST_PAGE = PAGES * LOOPS / 2;

    private ImagePagerAdapter adapter;
    private ViewPager pager;

    private List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        getData();
        initViews();


    }

    private void initViews(){

        setPager();

        //For Action Bar
        setActionBarTitle("Images");
        setActionBarBackgroundColor(R.color.marigold);

    }

    private void setPager(){

        pager = (ViewPager) findViewById(R.id.vpImage);

        adapter = new ImagePagerAdapter(getApplicationContext(), images);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(adapter);


//        // Set current item to the middle page so we can fling to both
//        // directions left and right
//        pager.setCurrentItem(FIRST_PAGE);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        pager.setPageMargin(-300);

    }

    private void getData(){

        images = getIntent().getStringArrayListExtra(ARG_IMAGES);
    }
}
