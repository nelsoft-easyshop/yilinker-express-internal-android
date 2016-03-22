package com.yilinker.expressinternal.mvp.view.profile;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class FragmentProfile extends BaseFragment implements IMainProfileView, RecyclerViewClickListener, View.OnClickListener{

    private ApplicationClass appClass;
    private MainProfilePresenter presenter;
    private LanguageAdapter adapter;

    private RecyclerView rvLanguages;

    private TextView tvFilter;

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
            initializeViews(view);
            presenter.bindView(this);

            initializeLanguages();

        } else {

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

            presenter.bindView(this);

        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    public void initializeViews(View parent) {

        LinearLayout llFilter = (LinearLayout) parent.findViewById(R.id.llFilter);
        tvFilter = (TextView)parent.findViewById(R.id.tvFilterType);
        rvLanguages = (RecyclerView) parent.findViewById(R.id.rvLanguages);

        rvLanguages.setHasFixedSize(true);
        rvLanguages.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new LanguageAdapter(this);

        rvLanguages.setAdapter(adapter);

        llFilter.setOnClickListener(this);
        tvFilter.setText(getFilterLabel(((ApplicationClass)ApplicationClass.getInstance()).isFilterByArea()));

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

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.llFilter){

            toggleFilterBy();
        }

    }

    private void toggleFilterBy(){

        ApplicationClass appClass = (ApplicationClass)ApplicationClass.getInstance();

        appClass.resetFilterType();

        tvFilter.setText(getFilterLabel(appClass.isFilterByArea()));
    }

    private String getFilterLabel(boolean isFilterByArea){

        ApplicationClass appClass = (ApplicationClass)ApplicationClass.getInstance();

        String label = null;
        if(appClass.isFilterByArea()){

            label = getString(R.string.filter_area);
        }
        else{

            label = getString(R.string.filter_branch);
        }

        return label;
    }

}
