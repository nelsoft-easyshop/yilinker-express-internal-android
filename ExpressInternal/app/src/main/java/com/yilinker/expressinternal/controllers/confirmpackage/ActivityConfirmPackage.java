package com.yilinker.expressinternal.controllers.confirmpackage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.express.internal.ShippingFee;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.model.PackageType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rlcoronado on 06/01/2016.
 */
public class ActivityConfirmPackage extends BaseActivity implements ResponseHandler, View.OnClickListener,
        AdapterView.OnItemClickListener, TextWatcher{

    private static final int REQUEST_CODE_GET_PACKAGE_TYPES = 1000;
    private static final int REQUEST_CODE_CALCULATE_SHIPPING_FEE = 1001;

    public static final String ARG_PACKAGE_FEE = "packageFee";

    private RequestQueue requestQueue;
    private AdapterPackageTypes adapterPackageTypes;
    private AdapterPackageSizes adapterPackageSizes;
    private List<PackageType> packageList = new ArrayList<>();
    private PackageType packageFee;

    private RelativeLayout rlLength, rlWidth, rlHeight, rlWeight, rlSize, rlType, rlProgressBar;
    private ListView lvType, lvSizes;
    private EditText etLength, etWidth, etHeight, etWeight, etType, etSize;
    private TextView tvShippingFee;
    private ImageView ivDropdown, ivDropdown2;
    private Button btnSave;

    private boolean isEdit = false;
    private String jobOrderNo;
    private int packagePosition, sizePosition;
    private int packageId, tempPackageId;
    private int sizeId, tempSizeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_package);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();
        packageFee = new PackageType();

        initViews();
        initListeners();
        getPackageTypes();

        getData();


    }

    @Override
    protected void onPause() {
        super.onPause();

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_CANCELED);
    }

    private void getData() {

        Intent intent = getIntent();

        jobOrderNo = intent.getStringExtra(ActivityChecklist.ARG_JOB_ORDER);

        if(intent.getParcelableExtra(ARG_PACKAGE_FEE) != null) {

            packageFee = intent.getParcelableExtra(ARG_PACKAGE_FEE);

            etLength.setText(packageFee.getLength());
            etWidth.setText(packageFee.getWidth());
            etHeight.setText(packageFee.getHeight());
            etWeight.setText(packageFee.getWeight());
            etType.setText(packageFee.getTypeName());
            rlProgressBar.setVisibility(View.GONE);
            getPackageTypes();

            tempPackageId = Integer.valueOf(packageFee.getTypeId());
            tempSizeId = Integer.valueOf(packageFee.getSizeId());

            isEdit = true;
        }

    }

    private void initViews() {

        etType = (EditText) findViewById(R.id.etPackageType);
        etSize = (EditText) findViewById(R.id.etPackageSize);

        rlLength = (RelativeLayout) findViewById(R.id.rlLength);
        rlWidth = (RelativeLayout) findViewById(R.id.rlWidth);
        rlHeight = (RelativeLayout) findViewById(R.id.rlHeight);
        rlWeight = (RelativeLayout) findViewById(R.id.rlWeight);
        rlType = (RelativeLayout) findViewById(R.id.rlPackageType);
        rlSize = (RelativeLayout) findViewById(R.id.rlPackageSize);
        rlProgressBar = (RelativeLayout) findViewById(R.id.rlProgress);

        lvType = (ListView) findViewById(R.id.lvTypes);
        lvSizes = (ListView) findViewById(R.id.lvSizes);
        ivDropdown = (ImageView) findViewById(R.id.ivDropdown);
        ivDropdown2 = (ImageView) findViewById(R.id.ivDropdown2);

        etLength = (EditText) findViewById(R.id.etLength);
        etWidth = (EditText) findViewById(R.id.etWidth);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        tvShippingFee = (TextView) findViewById(R.id.tvShippingFee);

        btnSave = (Button) findViewById(R.id.btnSave);

        setActionBar();

    }

    private void initListeners() {

        btnSave.setOnClickListener(this);
        rlSize.setOnClickListener(this);
        rlType.setOnClickListener(this);
        ivDropdown.setOnClickListener(this);
        ivDropdown2.setOnClickListener(this);
        etType.setOnClickListener(this);
        etSize.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        lvType.setOnItemClickListener(this);
        lvSizes.setOnItemClickListener(this);

        etType.addTextChangedListener(this);
        etSize.addTextChangedListener(this);
        etWeight.addTextChangedListener(this);
        etHeight.addTextChangedListener(this);
        etLength.addTextChangedListener(this);
        etWidth.addTextChangedListener(this);
    }

    private void setActionBar(){

        //For Action Bar

        setActionBarTitle(getString(R.string.checklist_delivery_package));

        int actionBarColor = R.color.marigold;

        setActionBarBackgroundColor(actionBarColor);

    }


    @Override
    protected void handleRefreshToken() {

    }

    private void getPackageTypes() {

        rlProgressBar.setVisibility(View.VISIBLE);

        Request request = JobOrderAPI.getPackages(REQUEST_CODE_GET_PACKAGE_TYPES, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);
    }

    private void checkConditions() {

        if(!etType.getText().toString().isEmpty()){
            if(etType.getText().toString().equals("Custom")) {
                if(etHeight.getText().toString().isEmpty()
                        || etLength.getText().toString().isEmpty()
                        || etWidth.getText().toString().isEmpty()
                        || etWeight.getText().toString().isEmpty()) {

                } else {
                    calculateShippingFee();
                }
            } else {
                if(etSize.getText().toString().isEmpty() || etWeight.getText().toString().isEmpty()) {

//                    onBackPressed();

                } else {
                    calculateShippingFee();
                }
            }
        } else {
//            onBackPressed();
        }

    }

    private void calculateShippingFee() {

//        rlProgressBar.setVisibility(View.VISIBLE);

        tvShippingFee.setText("Calculating...");

        String length, height, width, weight;

        length = etLength.getText().toString();
        height = etHeight.getText().toString();
        width = etWidth.getText().toString();
        weight = etWeight.getText().toString();

        Request request = JobOrderAPI.calculateShippingFee(REQUEST_CODE_CALCULATE_SHIPPING_FEE,
                sizeId, length, width, height, weight, jobOrderNo, this);

        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }


    private void handleCalculateShippingFee(Object object) {

        ShippingFee shippingFee = (ShippingFee) object;

        //temp To format the shipping fee to 2 decimal places
        float totalFee = Float.parseFloat(shippingFee.getTotalShippingFee());
        String fee  = String.format("%.2f", totalFee);

//        tvShippingFee.setText(shippingFee.getTotalShippingFee());
        tvShippingFee.setText(String.format("P%s", fee));


        btnSave.setEnabled(totalFee > 0);

    }

    private void handlePackageTypesData(Object object) {

        List<com.yilinker.core.model.express.internal.PackageType> packageTypeServer = (ArrayList<com.yilinker.core.model.express.internal.PackageType>) object;

        for(int i=0;i<packageTypeServer.size();i++) {

            com.yilinker.expressinternal.model.PackageType packageTypeLocal = new com.yilinker.expressinternal.model.PackageType(packageTypeServer.get(i));

            packageList.add(packageTypeLocal);
        }

        setPackageList(packageList);


    }

    private void setSizeList() {

        adapterPackageSizes = new AdapterPackageSizes(this, packageList.get(packageId).getSize());
        lvSizes.setAdapter(adapterPackageSizes);
        adapterPackageSizes.notifyDataSetChanged();

        if(isEdit){

            lvSizes.performItemClick(lvSizes, tempSizeId, lvSizes.getItemIdAtPosition(tempSizeId));
            isEdit = false;
        }
    }

    private void setPackageList(List<com.yilinker.expressinternal.model.PackageType> packageList) {

        adapterPackageTypes = new AdapterPackageTypes(this, packageList);
        lvType.setAdapter(adapterPackageTypes);
        adapterPackageTypes.notifyDataSetChanged();

        if(isEdit){
            lvType.performItemClick(lvType, tempPackageId, lvType.getItemIdAtPosition(tempPackageId));
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch(v.getId()) {

            case R.id.btnSave:

                setResult(RESULT_OK);
                Intent data = new Intent();

                packageFee.setTypeName(etType.getText().toString());
                packageFee.setTypeId(String.valueOf(packagePosition));
                packageFee.setSizeId(String.valueOf(sizePosition));
                packageFee.setHeight(etHeight.getText().toString());
                packageFee.setLength(etLength.getText().toString());
                packageFee.setWeight(etWeight.getText().toString());
                packageFee.setWidth(etWidth.getText().toString());
                packageFee.setShippingFee(tvShippingFee.getText().toString());

                data.putExtra(ARG_PACKAGE_FEE, packageFee);

                setResult(RESULT_OK, data);

                finish();

                break;

            case R.id.etPackageType:
                lvType.setVisibility(lvType.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;

            case R.id.ivDropdown:
                lvType.setVisibility(lvType.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;

            case R.id.etPackageSize:

                if(adapterPackageSizes != null) {
                    lvSizes.setVisibility(lvSizes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
                break;

            case R.id.ivDropdown2:

                if(adapterPackageSizes != null) {
                    lvSizes.setVisibility(lvSizes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getAdapter() == adapterPackageTypes) {

            lvType.setVisibility(View.GONE);
            packagePosition = position;
            packageId = packageList.get(position).getId();
            etType.setText(packageList.get(position).getName());

            if(!etType.getText().toString().equals("Custom")){

                rlSize.setVisibility(View.VISIBLE);
                rlHeight.setVisibility(View.GONE);
                rlLength.setVisibility(View.GONE);
                rlWidth.setVisibility(View.GONE);

                setSizeList();
                lvSizes.performItemClick(lvSizes, sizePosition, lvSizes.getItemIdAtPosition(sizePosition));

            } else {

                rlSize.setVisibility(View.GONE);
                rlHeight.setVisibility(View.VISIBLE);
                rlLength.setVisibility(View.VISIBLE);
                rlWidth.setVisibility(View.VISIBLE);

                sizeId = packageId;
                lvSizes.setVisibility(View.GONE);
                lvType.setVisibility(View.GONE);

            }


        }

        if (parent.getAdapter() == adapterPackageSizes) {

            lvSizes.setVisibility(View.GONE);
            sizeId = packageList.get(packagePosition).getSize().get(position).getId();
            etSize.setText(packageList.get(packagePosition).getSize().get(position).getName());
            sizePosition = position;

        }

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch(requestCode) {

            case REQUEST_CODE_GET_PACKAGE_TYPES:

                handlePackageTypesData(object);

                break;

            case REQUEST_CODE_CALCULATE_SHIPPING_FEE:

                handleCalculateShippingFee(object);

                break;

        }

        rlProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode) {

            case REQUEST_CODE_CALCULATE_SHIPPING_FEE:
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                tvShippingFee.setText("-");
                btnSave.setEnabled(false);
                break;

        }

        rlProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
        checkConditions();
    }
}