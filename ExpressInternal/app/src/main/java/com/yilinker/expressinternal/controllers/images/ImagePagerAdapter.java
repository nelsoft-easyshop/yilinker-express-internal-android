package com.yilinker.expressinternal.controllers.images;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yilinker.core.imageloader.VolleyImageLoader;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.customviews.CarouselImageContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by J.Bautista
 */
public class ImagePagerAdapter extends PagerAdapter implements
        ViewPager.OnPageChangeListener{

    public static final String TYPE_URL = "url";
    public static final String TYPE_URI = "uri";
    public static final String TYPE_FILE = "file";

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
    private String type = TYPE_URL;   //Default

    private int resId;

    public ImagePagerAdapter(Context context, List<String> objects, String type){

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objects = objects;
        this.type = type;

        this.imageLoader = VolleyImageLoader.getInstance(context).getImageLoader();

        firstPage = objects.size() / 2;

        list = new HashMap<>();

        if(type.equalsIgnoreCase(TYPE_URL)){

            resId = R.layout.layout_image_pager;
        }
        else{

            resId = R.layout.layout_image_pager_local;
        }
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

        View view = inflater.inflate(resId, container, false);
        CarouselImageContainer root = (CarouselImageContainer) view.findViewById(R.id.llImage);

        ImageView ivProduct = (ImageView) view.findViewById(R.id.ivProduct);

        if (position == firstPage)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;


        if(type.equalsIgnoreCase(TYPE_URL)) {

            ((NetworkImageView)ivProduct).setImageUrl(objects.get(position), imageLoader);
        }
        else if (type.equalsIgnoreCase(TYPE_FILE)){

            String path = objects.get(position);

            Uri uri = Uri.fromFile(new File(path));

            ivProduct.setImageBitmap(decodeFromUri(uri.toString()));

        }
        else{

            String path = objects.get(position);

            ivProduct.setImageBitmap(decodeFromUri(path));

        }


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

    private Bitmap decodeFromUri(String path){

        Uri uri = Uri.parse(path);

        Bitmap actuallyUsableBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        AssetFileDescriptor fileDescriptor =null;
        try {

            fileDescriptor = context.getContentResolver().openAssetFileDescriptor( uri, "r");

            actuallyUsableBitmap
                    = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor.getFileDescriptor(), null, options);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return  actuallyUsableBitmap;
    }

}
