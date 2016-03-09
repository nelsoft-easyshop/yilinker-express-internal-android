package com.yilinker.expressinternal.mvp.view.profile;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.model.Languages;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.profile.MainProfilePresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;

import java.util.List;

/**
 * Created by rlcoronado on 03/03/2016.
 */
public class FragmentProfile extends BaseFragment implements IMainProfileView, RecyclerViewClickListener {

    private ApplicationClass appClass;
    private MainProfilePresenter presenter;
    private LanguageAdapter adapter;

    private RecyclerView rvLanguages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        appClass = (ApplicationClass) getActivity().getApplicationContext();

        if (savedInstanceState == null) {

            presenter = new MainProfilePresenter();

        } else {

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        initializeViews(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.bindView(this);

        initializeLanguages();
    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    public void initializeViews(View parent) {

        rvLanguages = (RecyclerView) parent.findViewById(R.id.rvLanguages);

        rvLanguages.setHasFixedSize(true);
        rvLanguages.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new LanguageAdapter(this);

        rvLanguages.setAdapter(adapter);
    }

    @Override
    public void showLoader(boolean isShown) {


    }

    private void initializeLanguages() {

        Resources resources = getResources();
        String[] languages = resources.getStringArray(R.array.profile_languages);
        String[] locale = resources.getStringArray(R.array.profile_languages_locale);

        presenter.initializeLanguageModel(languages, locale, appClass.getLanguageId());
    }


    @Override
    public void onItemClick(int position, Object object) {

        presenter.onLanguageSelected(position);
        appClass.setLanguage(object);

    }

    @Override
    public void loadLanguages(List<Languages> langs) {
        adapter.addAll(langs);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void changeSelectedLanguage(Languages previousLang, Languages currentLang) {

        adapter.updateItem(previousLang);
        adapter.updateItem(currentLang);

    }
}
