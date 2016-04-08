package com.yilinker.expressinternal.mvp.presenter.profile;

import com.yilinker.expressinternal.mvp.model.Languages;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.profile.FragmentProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rlcoronado on 03/03/2016.
 */
public class MainProfilePresenter extends BasePresenter<List<Languages>, FragmentProfile> implements IProfilePresenter {

    private List<Languages> langs;
    private int previousLang = 0;

    public MainProfilePresenter() {
    }

    @Override
    protected void updateView() {
        view().onItemClick(previousLang, langs.get(previousLang));
    }

    @Override
    public void initializeLanguageModel(String[] languages, String[] locales, int currentLang) {

        langs = new ArrayList<>();
        Languages lang = null;

        for (int i = 0; i < languages.length; i++) {

            lang = new Languages();
            lang.setId(i);
            lang.setTitle(languages[i]);
            lang.setLocale(locales[i]);

            if(i == currentLang){
                previousLang = i;
                lang.setSelected(true);
            }

            langs.add(lang);
        }

        view().loadLanguages(langs);
        updateView();

    }

    @Override
    public void onLanguageSelected(int position) {

        //Unselect previous language
        Languages previous = langs.get(previousLang);
        previous.setSelected(false);

        //Select current language
        Languages current = langs.get(position);
        current.setSelected(true);

        previousLang = position;

        view().changeSelectedLanguage(previous, current);

    }
}
