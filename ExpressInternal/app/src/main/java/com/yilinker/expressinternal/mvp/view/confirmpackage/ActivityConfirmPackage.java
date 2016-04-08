package com.yilinker.expressinternal.mvp.view.confirmpackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.customviews.WeightTextInputFilter;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.mvp.model.*;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.presenter.confirmpackage.ConfirmPackagePresenter;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.view.base.BaseFragmentActivity;
import com.yilinker.expressinternal.utilities.ExpressFileHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/18/16.
 */
public class ActivityConfirmPackage extends BaseFragmentActivity implements IConfirmPackageView, TextWatcher, View.OnClickListener, View.OnFocusChangeListener{

    public static final String ARG_SELECTED_PACKAGE = "package";

    private Button btnSave;
    private Button btnPackageType;
    private Button btnPackageSize;
    private EditText etWidth;
    private EditText etLength;
    private EditText etHeight;
    private EditText etWeight;
    private LinearLayout llCustomFields;
    private TextView tvShippingFee;
    private RecyclerView rvTypes;
    private RecyclerView rvSizes;

    private PackageTypeAdapter typeAdapter;
    private PackageSizeAdapter sizeAdapter;

    private ConfirmPackagePresenter presenter;

    private int viewWithFocus;

    private RecyclerViewClickListener<PackageType> typeListener = new RecyclerViewClickListener<PackageType>() {
        @Override
        public void onItemClick(int position, PackageType object) {

            btnPackageSize.setText(getString(R.string.package_select));

            presenter.onPackageTypeChanged(object);
            showPackageTypeList(false);
        }
    };

    private RecyclerViewClickListener<PackageSize> sizeListener = new RecyclerViewClickListener<PackageSize>() {
        @Override
        public void onItemClick(int position, PackageSize object) {

            presenter.onPackageSizeChanged(object);
            showPackageSizeList(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_package2);

        if(savedInstanceState == null){

            presenter = new ConfirmPackagePresenter();

            initializeViews(null);

            presenter.bindView(this);

            presenter.onCreate(getData());
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
            presenter.bindView(this);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_CANCELED);
    }

    @Override
    public void loadPackageType(List<PackageType> list) {

        typeAdapter.clearAndAddAll(list);

        btnPackageType.setText(getString(R.string.package_select));
        btnPackageSize.setText(getString(R.string.package_select));

        btnPackageType.setEnabled(true);

    }

    @Override
    public void loadPackageSize(List<PackageSize> list) {

        sizeAdapter.clearAndAddAll(list);

        btnPackageSize.setEnabled(true);
    }

    @Override
    public void resetShippingFee(String fee) {

        tvShippingFee.setText(fee);
    }

    @Override
    public void goBackToChecklist(Package selectedPackage) {

        Intent intent = new Intent();
        intent.putExtra(ARG_SELECTED_PACKAGE, selectedPackage);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showCustomPackageView(boolean isShown) {

        int visibility = 0;

        if(isShown){

            visibility = View.VISIBLE;
        }
        else {

            visibility = View.GONE;
        }

        llCustomFields.setVisibility(visibility);

    }

    @Override
    public void showPackageSizeButton(boolean isShown) {

        int visibility = 0;

        if(isShown){

            visibility = View.VISIBLE;
        }
        else {

            visibility = View.GONE;
        }

        RelativeLayout rlPackageSize = (RelativeLayout) findViewById(R.id.rlPackageSize);
        rlPackageSize.setVisibility(visibility);
    }

    @Override
    public void addRequest(Request request) {

        addRequestToQueue(request);
    }

    @Override
    public void getLocalList(String fileName) {

        String localList = ExpressFileHelper.readFile(getApplicationContext(), fileName);
        presenter.onGetLocalList(localList);
    }

    @Override
    public void saveListToLocal(String list, String fileName) {

        ExpressFileHelper.saveToLocal(getApplicationContext(), list, fileName);
    }

    @Override
    public void setSelectedPackageType(String packageType) {

        btnPackageType.setText(packageType);
    }

    @Override
    public void setSelectedPackageSize(String packageSize) {

        btnPackageSize.setText(packageSize);
    }

    @Override
    public void setWidthText(String width) {

        etWidth.setText(width);
    }

    @Override
    public void setHeightText(String height) {

        etHeight.setText(height);
    }

    @Override
    public void setWeightText(String weight) {

        etWeight.setText(weight);
    }

    @Override
    public void setLengthText(String length) {

        etLength.setText(length);
    }

    @Override
    public void showErrorMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoPackageTypeError() {


    }

    @Override
    public void initializeViews(View parent) {

        ImageView ivClose = (ImageView) findViewById(R.id.ivClose);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnPackageType = (Button) findViewById(R.id.btnPackageType);
        btnPackageSize = (Button) findViewById(R.id.btnPackageSize);
        tvShippingFee = (TextView) findViewById(R.id.tvShippingFee);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etLength = (EditText) findViewById(R.id.etLength);
        etWidth = (EditText) findViewById(R.id.etWidth);
        etHeight = (EditText) findViewById(R.id.etHeight);
        llCustomFields = (LinearLayout) findViewById(R.id.llCustomFields);
        rvSizes = (RecyclerView) findViewById(R.id.rvSizes);
        rvTypes = (RecyclerView) findViewById(R.id.rvTypes);

        LinearLayoutManager sizeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSizes.setLayoutManager(sizeLayoutManager);

        LinearLayoutManager typeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTypes.setLayoutManager(typeLayoutManager);

        setPackageTypeAdapter();
        setPackageSizeAdapter();


        btnPackageSize.setOnClickListener(this);
        btnPackageType.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        etWeight.setFilters(new InputFilter[]{new WeightTextInputFilter(1000000, new WeightTextInputFilter.OnLimitReachedListener() {
            @Override
            public void onLimitReachedListener() {

                showMaximumWeightError();
            }
        })});
    }

    @Override
    public void showLoader(boolean isShown) {

        //TODO Reset text of Save button and show the transparent view to avoid editing of details
    }

    @Override
    public void cancelRequest(String tag) {

        List<String> tags = new ArrayList<>();
        tags.add(tag);

        cancelRequests(tags);
    }

    @Override
    public void showCalculatingFeeStatus() {

        tvShippingFee.setText(getString(R.string.package_calculating));
    }

    @Override
    public void enabledSaveButton(boolean isEnabled) {

        btnSave.setEnabled(isEnabled);
    }

    @Override
    public void enabledPackageTypeButton(boolean isEnabled) {

        btnPackageType.setEnabled(isEnabled);
    }

    @Override
    public void enabledPackageSizeButton(boolean isEnabled) {

        btnPackageSize.setEnabled(isEnabled);
    }

    @Override
    public void addEditTextListeners() {

        setTextChangedListener();
        setFocusChangedListener();
    }


    private void showMaximumWeightError() {

        showErrorMessage(getString(R.string.package_maximum_weight_error));
    }

    private void setTextChangedListener(){

        etHeight.addTextChangedListener(this);
        etWidth.addTextChangedListener(this);
        etWeight.addTextChangedListener(this);
        etLength.addTextChangedListener(this);
    }

    private void setFocusChangedListener(){

        etHeight.setOnFocusChangeListener(this);
        etWeight.setOnFocusChangeListener(this);
        etWidth.setOnFocusChangeListener(this);
        etLength.setOnFocusChangeListener(this);
    }

    private Package getData(){

        Package selectedPackage = null;

        Intent intent = getIntent();

        if(intent.hasExtra(ARG_SELECTED_PACKAGE)){

            selectedPackage = intent.getParcelableExtra(ARG_SELECTED_PACKAGE);
        }

        return selectedPackage;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {



    }

    @Override
    public void afterTextChanged(Editable s) {

        String text = s.toString();

        if(text.isEmpty()) {

            presenter.onEmptyField();
            enabledSaveButton(false);
        }
        else {

            switch (viewWithFocus) {

                case R.id.etWidth:

                    presenter.onWidthTextChanged(text);
                    break;

                case R.id.etWeight:

                    presenter.onWeightTextChanged(text);
                    break;

                case R.id.etLength:

                    presenter.onLengthTextChanged(text);
                    break;

                case R.id.etHeight:

                    presenter.onHeightTextChanged(text);
                    break;
            }
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnSave:

                presenter.onSaveButtonClick();
                break;

            case R.id.btnPackageType:

                showPackageTypeList(!(rvTypes.getVisibility() == View.VISIBLE));
                showPackageSizeList(false);
                break;

            case R.id.btnPackageSize:

                showPackageTypeList(false);
                showPackageSizeList(!(rvSizes.getVisibility() == View.VISIBLE));
                break;

            case R.id.ivClose:

                onBackPressed();
                break;

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus){

            viewWithFocus = v.getId();
        }
    }

    private void showPackageTypeList(boolean isShown){

        int visibility = 0;
        if(isShown){

            visibility = View.VISIBLE;
        }
        else {

            visibility = View.GONE;
        }

        rvTypes.setVisibility(visibility);
    }

    private void showPackageSizeList(boolean isShown){

        int visibility = 0;
        if(isShown){

            visibility = View.VISIBLE;
        }
        else {

            visibility = View.GONE;
        }

        rvSizes.setVisibility(visibility);
    }

    private void setPackageTypeAdapter(){

        typeAdapter = new PackageTypeAdapter(typeListener);
        rvTypes.setAdapter(typeAdapter);
    }

    private void setPackageSizeAdapter(){

        sizeAdapter = new PackageSizeAdapter(sizeListener);
        rvSizes.setAdapter(sizeAdapter);
    }
}
