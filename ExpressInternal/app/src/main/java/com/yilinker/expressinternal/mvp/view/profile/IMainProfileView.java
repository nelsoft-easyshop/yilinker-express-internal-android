package com.yilinker.expressinternal.mvp.view.profile;

import com.yilinker.expressinternal.mvp.model.Languages;

import java.util.List;

/**
 * Created by rlcoronado on 03/03/2016.
 */
public interface IMainProfileView {

    void loadLanguages(List<Languages> langs);
    void changeSelectedLanguage(Languages previousLang, Languages currentLang);
}
