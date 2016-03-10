package com.yilinker.expressinternal.mvp.view.profile;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.Languages;
import com.yilinker.expressinternal.mvp.presenter.profile.LanguagePresenter;

/**
 * Created by rlcoronado on 03/03/2016.
 */
public class LanguageViewHolder extends BaseViewHolder<LanguagePresenter> implements ILanguageView, View.OnClickListener {

    private RecyclerViewClickListener listener;
    private RelativeLayout rlLanguage;
//    private CheckBox cbLanguage;
    private TextView tvLanguage;
    private ImageView cbLanguage;

    public LanguageViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        this.listener = listener;

        rlLanguage = (RelativeLayout) itemView.findViewById(R.id.rlLanguage);
        tvLanguage = (TextView) itemView.findViewById(R.id.tvLanguage);
//        cbLanguage = (CheckBox) itemView.findViewById(R.id.cbLanguage);
        cbLanguage = (ImageView) itemView.findViewById(R.id.cbLanguage);

        rlLanguage.setOnClickListener(this);
    }

    @Override
    public void setLanguageName(String languageName) {
        tvLanguage.setText(languageName);
    }


    @Override
    public void onClick(View v) {
        presenter.onClick();
    }

    @Override
    public void showSelected(Languages lang) {
        listener.onItemClick(lang.getId(), lang);
    }

//    @Override
//    public void setSelected(boolean isSelected) {
//        cbLanguage.setChecked(isSelected);
//    }

    @Override
    public void setSelected(boolean isSelected) {

        int resource = 0;

        if(isSelected){

            resource = R.drawable.ic_checked;
        }

        else{

            resource = R.drawable.ic_unchecked;
        }

        cbLanguage.setImageResource(resource);
    }

}
