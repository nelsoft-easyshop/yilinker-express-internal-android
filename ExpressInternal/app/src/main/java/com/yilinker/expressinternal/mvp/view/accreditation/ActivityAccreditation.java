package com.yilinker.expressinternal.mvp.view.accreditation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.login.ActivityLogin;
import com.yilinker.expressinternal.customviews.CustomLinearLayoutManager;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.accreditation.AccreditationPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.JobsAdapter;

import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ActivityAccreditation extends BaseFragmentActivity implements IAccreditationView, OnDataUpdateListener, View.OnClickListener{

    private AccreditationPresenter presenter;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etBirthday;
    private EditText etGender;

    private AccreditationRequirementAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accreditation);

        if(savedInstanceState == null){

            presenter = new AccreditationPresenter();
            presenter.bindView(this);

            initializeViews(null);

            presenter.onCreate();

        }
        else {

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

    }


    @Override
    public void initializeViews(View parent) {

        etBirthday = (EditText) findViewById(R.id.etBithday);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etGender = (EditText) findViewById(R.id.etGender);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        RecyclerView rvRequirements = (RecyclerView) findViewById(R.id.rvRequirements);

        //For Requirements list
        adapter = new AccreditationRequirementAdapter(this);
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this, CustomLinearLayoutManager.VERTICAL, false);
//        layoutManager.
        rvRequirements.setLayoutManager(layoutManager);
        rvRequirements.setAdapter(adapter);

        btnSave.setFocusableInTouchMode(true);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void showLoader(boolean isShown) {

        //TODO Change the text of the upload button and show the transparent view to avoid editing of details while uploading data

    }

    @Override
    public void goToLoginScreen() {

        Intent intent = new Intent(ActivityAccreditation.this, ActivityLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showErrorMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessageByType(int type) {

        //TODO Add switch here to show error message based on validation type error

    }

    @Override
    public void loadRequirementsList(List<AccreditationRequirement> requirements) {

        adapter.clearAndAddAll(requirements);
    }

    @Override
    public void addRequest(Request request) {
        
        addRequestToQueue(request);
    }

    @Override
    public void onDataUpdate(Object item) {

        //TODO Call presenter method for updating selected value here
        //temp

        AccreditationRequirement requirement = (AccreditationRequirement)item;
        adapter.updateItem(requirement);

        Toast.makeText(getApplicationContext(), requirement.getInputValue(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnSave:

                v.requestFocus();
                presenter.onSaveButtonClick();
                break;
        }
    }
}
