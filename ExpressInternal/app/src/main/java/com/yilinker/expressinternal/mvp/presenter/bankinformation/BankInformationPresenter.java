package com.yilinker.expressinternal.mvp.presenter.bankinformation;

import com.android.volley.Request;
import com.yilinker.core.api.express.BankInformationApi;
import com.yilinker.core.model.express.internal.Bank;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.mvp.model.BankInformation;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.bankinformation.IBankInformationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick-villanueva on 4/6/2016.
 */
public class BankInformationPresenter extends RequestPresenter<List<BankInformation>, IBankInformationView> {

    private final static int REQUEST_BANK_INFORMATIONS = 2000;
    private final static String REQUEST_BANK_INFORMATIONS_TAG = "bank-informations";

    @Override
    protected void updateView() {

    }

    public void requestBankInformation(){

//        view().showLoader(true);
        Request request = BankInformationApi.getBankInformation(REQUEST_BANK_INFORMATIONS,this,new ExpressErrorHandler(this,REQUEST_BANK_INFORMATIONS));
        request.setTag(REQUEST_BANK_INFORMATIONS_TAG);
        view().addRequestToQueue(request);


    }

    public void onPause(){
        view().cancelRequests(getRequestTags());
    }

    private List<String> getRequestTags(){
        List<String> lists = new ArrayList<>();
        lists.add(REQUEST_BANK_INFORMATIONS_TAG);

        return lists;
    }


    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_BANK_INFORMATIONS:

                handleBankInformations(object);
                view().showLoader(false);
                break;

            default:
                break;
        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case REQUEST_BANK_INFORMATIONS:
                view().showErrorMessage(message);
                view().showLoader(false);
                break;

            default:
                break;
        }
    }

    private void handleBankInformations(Object object){

        List<Bank> bankServer = (List<Bank>) object;

        List<BankInformation> banksLocal = new ArrayList<>();
        int id = 1;
        for (Bank item: bankServer){
            BankInformation bank = new BankInformation(item);
            bank.setId(id);

            banksLocal.add(bank);
            id++;
        }

        setModel(banksLocal);
        view().addAllBanks(model);

    }
}
