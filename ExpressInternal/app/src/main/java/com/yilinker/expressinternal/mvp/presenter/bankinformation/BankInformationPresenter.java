package com.yilinker.expressinternal.mvp.presenter.bankinformation;

import com.android.volley.Request;
import com.yilinker.core.api.express.BankInformationApi;
import com.yilinker.core.model.express.internal.Bank;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.mvp.model.BankInformation;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
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
        //TODO add request here

//        Request request = BankInformationApi.getBankInformation(REQUEST_BANK_INFORMATIONS,this,new ExpressErrorHandler(this,REQUEST_BANK_INFORMATIONS));
//        request.setTag(REQUEST_BANK_INFORMATIONS_TAG);
//        view().addRequestToQueue(request);

        //TEMP TODO delete this
        addTempData();

    }

    //TODO TEMP
    private void addTempData(){

        List<BankInformation> banks = new ArrayList<>();
        for (int x =1 ; x<5; x++){
            BankInformation bank = new BankInformation();
            bank.setBankName("Banco De China 00" + String.valueOf(x));
            bank.setId(x);
            bank.setAccountName("Yilinker Express "+ String.valueOf(x));
            bank.setAccountNumber("12 2016 100490 "+String.valueOf(x));
            bank.setDropDownOpen(false);

            banks.add(bank);
        }

        view().addAllBanks(banks);
    }

    public void onPause(){
        //TODO uncomment this
//        view().cancelRequests(getRequestTags());
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
        for (Bank item: bankServer){
            BankInformation bank = new BankInformation(item);

            banksLocal.add(bank);
        }

        setModel(banksLocal);
        view().addAllBanks(model);

    }
}
