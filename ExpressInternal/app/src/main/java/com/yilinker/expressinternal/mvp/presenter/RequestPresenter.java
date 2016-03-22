package com.yilinker.expressinternal.mvp.presenter;

import android.util.SparseArray;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.api.express.RiderApi;
import com.yilinker.core.constants.ErrorMessages;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.OAuthentication;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.APIConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 2/22/16.
 *
 * Use this as the base class of presenters which require method for refreshing token
 */
public abstract class RequestPresenter <M, V>  extends BasePresenter <M, V>  implements ResponseHandler{

    private final static int REQUEST_REFRESH_TOKEN = 9000;

    private RequestQueue requestQueue;
    private ApplicationClass appClass;

    private SparseArray<Boolean> requestList;

    private boolean isRetrievingToken;

    public RequestPresenter(){

        appClass = (ApplicationClass)ApplicationClass.getInstance();
        requestQueue = appClass.getRequestQueue();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {


    }

    @Override
    public void onFailed(int requestCode, String message) {

        if(message.equalsIgnoreCase(ErrorMessages.ERR_EXPIRED_TOKEN)){

            if(!isRetrievingToken) {

                if(requestList == null){
                    requestList = new SparseArray<>();
                }

                requestList.append(requestCode, false);
                isRetrievingToken = true;
                ApplicationClass.refreshToken(this);
                return;

            }
        }
    }

    /***
     * Updates the status of a request
     * @param requestCode
     */
    public void updateRequestStatus(int requestCode){

        synchronized (requestList) {

            if (requestList != null) {

                int index = requestList.indexOfKey(requestCode);

                if (index != -1) {
                    requestList.setValueAt(index, true);
                }
            }

        }
    }

    /***
     * Get the current list of request
     */
    public SparseArray<Boolean> getRequestList(){

        return this.requestList;
    }


}
