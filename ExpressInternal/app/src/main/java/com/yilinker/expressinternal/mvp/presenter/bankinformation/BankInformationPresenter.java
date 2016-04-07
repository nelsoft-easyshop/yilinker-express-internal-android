package com.yilinker.expressinternal.mvp.presenter.bankinformation;

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
            bank.setAccountNumber("12 23231 23 12 2");
            bank.setDropDownOpen(false);

            banks.add(bank);
        }

        view().addAllBanks(banks);
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

                break;

            default:
                break;
        }
    }

    private void handleBankInformations(Object object){

    }
}
