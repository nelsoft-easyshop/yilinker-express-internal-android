package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.MapLegendItemPresenter;

/**
 * Created by J.Bautista on 4/7/16.
 */
public class MapLegendItemViewHolder extends BaseViewHolder<MapLegendItemPresenter> {

    private TextView tvLabel;
    private ImageView ivImage;

    public MapLegendItemViewHolder(View itemView) {
        super(itemView);

        tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

    }

    public void setLabelText(String text){

        tvLabel.setText(text);
    }

    public void setImage(int resourceId){

        ivImage.setImageResource(resourceId);
    }
}
