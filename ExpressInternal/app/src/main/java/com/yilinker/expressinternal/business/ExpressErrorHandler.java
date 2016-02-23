package com.yilinker.expressinternal.business;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yilinker.core.constants.APIConstants;
import com.yilinker.core.constants.ErrorMessages;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.APIResponse;
import com.yilinker.core.utility.GsonUtility;

import org.apache.http.HttpStatus;

/**
 * Created by J.Bautista on 2/18/16.
 */
public class ExpressErrorHandler implements Response.ErrorListener{

    private int requestCode;
    private ResponseHandler responseHandler;

    public ExpressErrorHandler(ResponseHandler responseHandler, int requestCode){

        this.responseHandler = responseHandler;
        this.requestCode = requestCode;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        if (error.networkResponse == null) {

            responseHandler.onFailed(requestCode, APIConstants.API_CONNECTION_PROBLEM);
            return;
        }

        int statusCode = error.networkResponse.statusCode;
        String message = null;

        if (statusCode == HttpStatus.SC_UNAUTHORIZED) {

            responseHandler.onFailed(requestCode, ErrorMessages.ERR_EXPIRED_TOKEN);

        } else if (statusCode == HttpStatus.SC_BAD_REQUEST) {

            try {

                String responseBody = new String(error.networkResponse.data, "utf-8");
                Gson gson = GsonUtility.createGsonBuilder(APIResponse.class, new APIResponse.APIResponseInstance()).create();
                APIResponse apiResponse = gson.fromJson(responseBody, APIResponse.class);

                if (apiResponse != null) {
                    message = apiResponse.getMessage();
                }


            } catch (Exception e) {

                message = APIConstants.API_CONNECTION_PROBLEM;
            } finally {

                responseHandler.onFailed(requestCode, message);
            }

        } else {

            responseHandler.onFailed(requestCode, APIConstants.API_CONNECTION_PROBLEM);
        }

    }
}
