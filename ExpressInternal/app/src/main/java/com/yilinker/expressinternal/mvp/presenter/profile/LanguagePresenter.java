package com.yilinker.expressinternal.mvp.presenter.profile;

import com.yilinker.expressinternal.mvp.model.Languages;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.profile.LanguageViewHolder;

/**
 * Created by rlcoronado on 01/03/2016.
 */
public class LanguagePresenter extends BasePresenter<Languages, LanguageViewHolder> {

    @Override
    protected void updateView() {

        view().setLanguageName(model.getTitle());
        view().setSelected(model.isSelected());
    }

    public void onClick(){

        view().showSelected(model);

    }


}
