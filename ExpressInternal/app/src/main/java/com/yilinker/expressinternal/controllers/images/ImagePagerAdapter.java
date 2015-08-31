package com.yilinker.expressinternal.controllers.images;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yilinker.core.imageloader.VolleyImageLoader;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.customviews.CarouselImageContainer;

import java.util.HashMap;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class ImagePagerAdapter extends PagerAdapter implements
        ViewPager.OnPageChangeListener{

    private final static float BIG_SCALE = 1.0f;
    private final static float SMALL_SCALE = 0.7f;
    private final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private Context context;
    private LayoutInflater inflater;

    private CarouselImageContainer current = null;
    private CarouselImageContainer next = null;

    private float scale;

    private List<String> objects;

    private HashMap<Integer,CarouselImageContainer> list;

    private ImageLoader imageLoader;
    private int firstPage;

    public ImagePagerAdapter(Context context, List<String> objects){

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objects = objects;

        this.imageLoader = VolleyImageLoader.getInstance(context).getImageLoader();

        firstPage = objects.size() / 2;

        list = new HashMap<>();
    }


    @Override
    public int getCount() {
        return  objects.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.layout_image_pager, container, false);

        CarouselImageContainer root = (CarouselImageContainer) view.findViewById(R.id.llImage);
        NetworkImageView ivProduct = (NetworkImageView) view.findViewById(R.id.ivProduct);

        if (position == firstPage)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;

        ivProduct.setImageUrl(objects.get(position), imageLoader);

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

            if (position < objects.size()-1) {
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
