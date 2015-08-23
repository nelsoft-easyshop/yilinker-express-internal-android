package com.yilinker.expressinternal.controllers.images;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.customviews.CarouselImageContainer;

import java.util.HashMap;

/**
 * Created by J.Bautista
 */
public class ImagePagerAdapter extends PagerAdapter implements
        ViewPager.OnPageChangeListener{

    private final static int PAGES = 5;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    private final static int LOOPS = 10;
    private final static int FIRST_PAGE = PAGES * LOOPS / 2;
    private final static float BIG_SCALE = 1.0f;
    private final static float SMALL_SCALE = 0.7f;
    private final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private Context context;
    private LayoutInflater inflater;

    private CarouselImageContainer current = null;
    private CarouselImageContainer next = null;

    private float scale;

    HashMap<Integer,CarouselImageContainer> list;

    public ImagePagerAdapter(Context context){

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        list = new HashMap<>();
    }


    @Override
    public int getCount() {
        return  PAGES;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.layout_image_pager, container, false);

        CarouselImageContainer root = (CarouselImageContainer) view.findViewById(R.id.llImage);

        if (position == FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;


        list.put(position, root);

        root.setScaleBoth(scale);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        list.remove(position);
        container.removeView((LinearLayout) object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset >= 0f && positionOffset <= 1f)
        {
            current = list.get(position);
            current.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);

            if (position < PAGES-1) {
                next = list.get(position + 1);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
