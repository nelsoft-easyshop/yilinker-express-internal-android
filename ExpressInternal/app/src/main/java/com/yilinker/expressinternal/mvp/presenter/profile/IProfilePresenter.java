package com.yilinker.expressinternal.mvp.presenter.profile;

/**
 * Created by rlcoronado on 03/03/2016.
 */
public interface IProfilePresenter{


    void initializeLanguageModel(String[] languages, String[] locales, int currentLang);
    void onLanguageSelected(int position);

}
