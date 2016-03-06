package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.constants.ErrorMessages;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.Login;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.model.CashDetail;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

/**
 * Created by J.bautista
 */
public class FragmentDialogCashLimitWarning extends DialogFragment implements View.OnClickListener, ResponseHandler{

    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String ARG_JOB_ORDER_NO = "jobOrderNo";

    private static final int REQUEST_CASH_INFO = 1000;
    private static final int REQUEST_EXTEND_CASH_LIMIT = 1001;
    private static final int REQUEST_ACCEPT_JOB_ORDER = 1002;
    private static final int REQUEST_REFRESH_TOKEN = 1003;

    private RelativeLayout rlProgress;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSubmit;
    private ImageButton btnClose;
    private TextView tvCashOnHand;
    private TextView tvCashLimit;

    private RequestQueue requestQueue;

    private String jobOrderNo;
    private int requestCode;

    private int currentRequest;

    public static FragmentDialogCashLimitWarning createInstance(int requestCode, String jobOrderNo){

        FragmentDialogCashLimitWarning fragment = new FragmentDialogCashLimitWarning();

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putString(ARG_JOB_ORDER_NO, jobOrderNo);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        Bundle args = getArguments();

        jobOrderNo = args.getString(ARG_JOB_ORDER_NO);
        requestCode = args.getInt(ARG_REQUEST_CODE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_cashlimit_warning, container, false);

        Window window =  getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        requestCashInfo();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSubmit:

                if(allowSubmit()){

//                    requestExtendCashLimit();
                    requestAcceptJobOrder();

                }
                else {

                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.login_error_incomplete_fields), Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btnClose:

                dismiss();
                break;

        }

    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        switch (requestCode){

            case REQUEST_CASH_INFO:

                handleCashInfoRequest(new CashDetail((com.yilinker.core.model.express.internal.CashDetail) object));
                break;

            case REQUEST_EXTEND_CASH_LIMIT:

                requestAcceptJobOrder();
                break;

            case REQUEST_ACCEPT_JOB_ORDER:

                goToHome();
                break;

            case REQUEST_REFRESH_TOKEN:

                Login oAuth = (Login) object;

                BaseApplication appClass = ApplicationClass.getInstance();

                appClass.saveAccessToken(oAuth.getAccess_token());
                appClass.saveRefreshToken(oAuth.getRefresh_token());

                handleRefreshToken();

                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {

        if(message.equalsIgnoreCase(ErrorMessages.ERR_EXPIRED_TOKEN)){

            ApplicationClass.refreshToken(this);
        }

        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
    }

    private void initViews(View root){

        rlProgress = (RelativeLayout) root.findViewById(R.id.rlProgress);
        etUsername = (EditText) root.findViewById(R.id.etUsername);
        etPassword = (EditText) root.findViewById(R.id.etPassword);
        tvCashLimit = (TextView) root.findViewById(R.id.tvCashLimit);
        tvCashOnHand = (TextView) root.findViewById(R.id.tvCashOnHand);
        btnSubmit = (Button) root.findViewById(R.id.btnSubmit);
        btnClose = (ImageButton) root.findViewById(R.id.btnClose);

        btnSubmit.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        rlProgress.setVisibility(View.GONE);
    }

    private void requestCashInfo(){

        currentRequest = REQUEST_CASH_INFO;

        rlProgress.setVisibility(View.VISIBLE);

        Request request = RiderAPI.getCashDetails(REQUEST_CASH_INFO, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestExtendCashLimit(){

        currentRequest = REQUEST_EXTEND_CASH_LIMIT;

        rlProgress.setVisibility(View.VISIBLE);

        String userName = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        Request request = RiderAPI.extendCashLimit(REQUEST_EXTEND_CASH_LIMIT, password, userName, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void requestAcceptJobOrder(){

        currentRequest = REQUEST_ACCEPT_JOB_ORDER;

        rlProgress.setVisibility(View.VISIBLE);

        String userName = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        Request request = RiderAPI.acceptJobOrder(REQUEST_ACCEPT_JOB_ORDER, jobOrderNo, userName, password, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }


    private void handleCashInfoRequest(CashDetail cashDetail){

        tvCashOnHand.setText(PriceFormatHelper.formatPrice(cashDetail.getCashOnHand()));
        tvCashLimit.setText(PriceFormatHelper.formatPrice(cashDetail.getCashLimit()));

        rlProgress.setVisibility(View.GONE);
    }


    private boolean allowSubmit(){

        String strName = etUsername.getText().toString().trim();
        String strPassword = etPassword.getText().toString().trim();

        boolean allow = strName.length() > 0 & strPassword.length() > 0;

        return allow;
    }

    private void goToHome(){

        Intent intent = new Intent(getActivity(), ActivityJobOrderList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void handleRefreshToken(){

        switch (currentRequest){

            case REQUEST_ACCEPT_JOB_ORDER:

                requestAcceptJobOrder();
                break;

            case REQUEST_EXTEND_CASH_LIMIT:

                requestExtendCashLimit();
                break;

            case REQUEST_CASH_INFO:

                requestCashInfo();
                break;

        }

    }

}
