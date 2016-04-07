package com.yilinker.expressinternal.mvp.view.bankinformation;

import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yilinker.core.imageloader.VolleyImageLoader;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.bankinformation.BankPresenter;

/**
 * Created by patrick-villanueva on 4/6/2016.
 */
public class BankViewHolder extends BaseViewHolder<BankPresenter> implements IBankView, View.OnClickListener {

    private TextView tvBankName;
    private TextView tvBankName2;
    private TextView tvAccountName;
    private TextView tvAccountNumber;
    private NetworkImageView ivLogo;
    private LinearLayout llBankContainer;
    private ImageButton ibDropDown;

    public BankViewHolder(View itemView) {
        super(itemView);

        tvBankName = (TextView) itemView.findViewById(R.id.tvBankName);
        tvBankName2 = (TextView) itemView.findViewById(R.id.tvBankName2);
        tvAccountName = (TextView) itemView.findViewById(R.id.tvAccountName);
        tvAccountNumber = (TextView) itemView.findViewById(R.id.tvAccountNumber);
        ivLogo = (NetworkImageView) itemView.findViewById(R.id.ivBankLogo);
        llBankContainer = (LinearLayout) itemView.findViewById(R.id.llBankInformationContainer);
        ibDropDown = (ImageButton) itemView.findViewById(R.id.ibDropDown);

        ibDropDown.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void setBankName(String bankName) {
        tvBankName.setText(bankName);
        tvBankName2.setText(bankName);
    }

    @Override
    public void setAccountName(String accountName) {

        tvAccountName.setText(accountName);
    }

    @Override
    public void setAccountNumber(String accountNumber) {

        tvAccountNumber.setText(accountNumber);
    }

    @Override
    public void setLogoURL(String logoURL) {
        ImageLoader imageLoader = VolleyImageLoader.getInstance(itemView.getContext()).getImageLoader();
        ivLogo.setImageUrl(logoURL, imageLoader);
    }

    @Override
    public void setDropDownOpen(boolean isToOpen) {

        if (isToOpen){
            ibDropDown.setBackgroundResource(R.drawable.ic_dropdown_arrow_down);

            /***Added simple animation*/
            Animation in = AnimationUtils.loadAnimation(itemView.getContext(), android.R.anim.fade_in);
            llBankContainer.startAnimation(in);

            llBankContainer.setVisibility(View.VISIBLE);

        }else {
            ibDropDown.setBackgroundResource(R.drawable.ic_dropdown_arrow);

            /***Added simple animation*/
            Animation out = AnimationUtils.loadAnimation(itemView.getContext(), android.R.anim.fade_out);
            llBankContainer.startAnimation(out);

            llBankContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        presenter.setDropDownVisibility();

    }
}
