package com.yilinker.expressinternal.mvp.view.profile;

import com.yilinker.expressinternal.mvp.model.Languages;

/**
 * Created by rlcoronado on 04/03/2016.
 */
public interface ILanguageView {

    void setLanguageName(String languageName);
    void showSelected(Languages lang);
    void setSelected(boolean isSelected);
}
