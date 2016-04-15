package com.yilinker.expressinternal.mvp.view.bankinformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.yilinker.core.customview.CustomNetworkImageView;
import com.yilinker.core.imageloader.BitmapLruImageCache;
import com.yilinker.core.imageloader.VolleyImageLoader;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.customviews.CustomSlideDownAnimation;
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
//        ImageLoader imageLoader = VolleyImageLoader.getInstance(itemView.getContext()).getImageLoader();
        ImageLoader imageLoader1 = new ImageLoader(Volley.newRequestQueue(itemView.getContext()), new BitmapLruImageCache(1000));
        ivLogo.setImageUrl(logoURL, imageLoader1);

    }


    @Override
    public void setDropDownOpen(boolean isToOpen) {

        if (isToOpen){
            animateExpand();

        }else {
            animateCollapse();

        }
    }

    @Override
    public void resetDropDownState() {
        llBankContainer.setVisibility(View.GONE);
        ibDropDown.setBackgroundResource(R.drawable.ic_dropdown_arrow);
    }

    private void animateExpand(){
        ibDropDown.setBackgroundResource(R.drawable.ic_dropdown_arrow_down);
        CustomSlideDownAnimation animation = new CustomSlideDownAnimation(llBankContainer, 300, CustomSlideDownAnimation.EXPAND);
        animation.setHeight((int) itemView.getContext().getResources().getDimension(R.dimen.bank_information_expand_height));
        llBankContainer.startAnimation(animation);

    }

    private void animateCollapse(){
        ibDropDown.setBackgroundResource(R.drawable.ic_dropdown_arrow);
        CustomSlideDownAnimation animation = new CustomSlideDownAnimation(llBankContainer, 300, CustomSlideDownAnimation.COLLAPSE);
         int height = animation.getHeight();
        animation.setHeight(height);
        llBankContainer.startAnimation(animation);

    }

    @Override
    public void onClick(View v) {

        presenter.setDropDownVisibility();

    }

}
