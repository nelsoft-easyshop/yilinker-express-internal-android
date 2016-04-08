package com.yilinker.expressinternal.mvp.view.accreditation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.AccreditationConstant;
import com.yilinker.expressinternal.controllers.login.ActivityLogin;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.model.ButtonRequirementImage;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.accreditation.AccreditationPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragmentActivity;
import com.yilinker.expressinternal.mvp.view.accreditation.button.DialogImagePreview;
import com.yilinker.expressinternal.mvp.view.accreditation.button.IButtonRequirementListener;
import com.yilinker.expressinternal.mvp.view.login.ActivityOngoingAccreditation;

import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ActivityAccreditation extends BaseFragmentActivity implements IAccreditationView, OnDataUpdateListener, View.OnClickListener, RecyclerViewClickListener<AccreditationRequirement>, IButtonRequirementListener, DatePickerDialog.OnDateSetListener{

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_DATE = 101;

    private AccreditationPresenter presenter;

    private EditText etFirstName;
    private EditText etLastName;
    private Button etBirthday;
    private EditText etGender;
    private Button btnSave;
    private View invisibleLoader;

    private View currentFocus;

    private AccreditationRequirementAdapter adapter;


    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if(hasFocus){

                currentFocus = v;

                if(v.getId() == R.id.etBithday){
                    showDatePicker();
                    hideKeyboard(v);
                }

            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String value = s.toString();

            int id = currentFocus.getId();
            switch (id){

                case R.id.etFirstName:

                    presenter.onFirstNameTextChanged(value);
                    break;

                case R.id.etLastName:

                    presenter.onLastNameTextChanged(value);
                    break;

                case R.id.etBithday:

                    presenter.onBirthdayTextChanged(value);
                    break;

                case R.id.etGender:

                    presenter.onGenderChanged(value);
                    break;
            }
        }
    };


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

//    @Override
//    protected void onPause() {
//
//        presenter.unbindView();
//        super.onPause();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        presenter.bindView(this);

        if(resultCode == RESULT_OK){

            switch (requestCode){

                case REQUEST_CAMERA:

                    presenter.onCameraResult();
                    break;

            }

        }

    }

    @Override
    public void initializeViews(View parent) {

        etBirthday = (Button) findViewById(R.id.etBithday);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etGender = (EditText) findViewById(R.id.etGender);
        btnSave = (Button) findViewById(R.id.btnSave);
        invisibleLoader = findViewById(R.id.invisibleLoader);

        RecyclerView rvRequirements = (RecyclerView) findViewById(R.id.rvRequirements);

        //For Requirements list
        adapter = new AccreditationRequirementAdapter(this, this);
        adapter.setInnerButtonListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        rvRequirements.setHasFixedSize(false);
        rvRequirements.setNestedScrollingEnabled(false);
        rvRequirements.setLayoutManager(layoutManager);
        rvRequirements.setAdapter(adapter);

        btnSave.setFocusableInTouchMode(true);
        btnSave.setOnClickListener(this);

        etBirthday.addTextChangedListener(textWatcher);
        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etGender.addTextChangedListener(textWatcher);

        etBirthday.setOnFocusChangeListener(focusChangeListener);
        etFirstName.setOnFocusChangeListener(focusChangeListener);
        etLastName.setOnFocusChangeListener(focusChangeListener);
        etGender.setOnFocusChangeListener(focusChangeListener);

        etBirthday.setOnClickListener(this);
    }

    @Override
    public void showLoader(boolean isShown) {

        //TODO Change the text of the upload button and show the transparent view to avoid editing of details while uploading data

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //temp Clear tokens
        ApplicationClass.getInstance().deleteTokens();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //temp Clear tokens
        ApplicationClass.getInstance().deleteTokens();
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

        switch (type){

            case AccreditationConstant.ACCREDITATION_ERROR_INCOMPLETE:

                showErrorMessage(getString(R.string.error_fill_up_fields));
                break;

        }
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
    public void launchCamera(Uri uri) {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, REQUEST_CAMERA);

    }

    @Override
    public void launchGallery() {



    }

    @Override
    public void resetItem(AccreditationRequirement item) {

        adapter.updateItem(item);
    }

    @Override
    public void onDataUpdate(Object item) {

        AccreditationRequirement requirement = (AccreditationRequirement)item;
        adapter.updateItem(requirement);

        presenter.onDataUpdate(requirement);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnSave:

                v.requestFocus();
                presenter.onSaveButtonClick();
                break;

            case R.id.etBithday:

                showDatePicker();
                break;
        }
    }

    @Override
    public void onItemClick(int position, AccreditationRequirement object) {

        presenter.onItemClick(object);
    }

    @Override
    public void onView(String filePath) {

        DialogImagePreview dialog = DialogImagePreview.createInstance(filePath);

        dialog.show(getFragmentManager(), "dialog");

    }

    @Override
    public void onDelete(ButtonRequirementImage item) {


    }

    @Override
    public String compressImage(String path) {

        String compressedImage = ImageUtility.compressCameraFileBitmap(path, getApplicationContext());

        return compressedImage;
    }

    @Override
    public void enableSaveButton(boolean isEnabled) {

        btnSave.setEnabled(isEnabled);
    }

    @Override
    public void showScreenLoader(boolean isShown) {

        String text = null;
        int visibility = 0;

        if(isShown){

            visibility = View.VISIBLE;
            text = getString(R.string.accreditation_saving);
        }
        else {

            visibility = View.GONE;
            text = getString(R.string.accreditation_save);
        }

        btnSave.setText(text);
        invisibleLoader.setVisibility(visibility);

    }

    @Override
    public void goToConfirmation() {

        Intent goToOngoing = new Intent(ActivityAccreditation.this, ActivityOngoingAccreditation.class);
        startActivity(goToOngoing);

    }

    private void showDatePicker(){

        FragmentDialogDatePicker dialog = FragmentDialogDatePicker.createInstance(REQUEST_DATE);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String date = String.format("%02d/%02d/%04d", monthOfYear, dayOfMonth, year);
        etBirthday.setText(date);
    }

    private void hideKeyboard(View view){

        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
