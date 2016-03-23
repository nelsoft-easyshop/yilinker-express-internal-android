package com.yilinker.expressinternal.mvp.view.startpage;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yilinker.expressinternal.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Patrick on 3/9/2016.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private TypedArray images;
    private Context context;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Context context, TypedArray images){
        this.images = images;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final View itemView;
        itemView = inflater.inflate(R.layout.layout_viewpager_item, container, false);

        ImageViewHolder holder = new ImageViewHolder();
        holder.ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

        holder.ivImage.setBackgroundResource(images.getResourceId(position,0));


        container.addView(itemView);

        return itemView;
    }

    class ImageViewHolder{

        public ImageView ivImage;
    }

}
