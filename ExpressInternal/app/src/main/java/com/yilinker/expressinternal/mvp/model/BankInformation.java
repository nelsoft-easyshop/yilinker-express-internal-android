package com.yilinker.expressinternal.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yilinker.core.model.express.internal.Bank;

/**
 * Created by patrick-villanueva on 4/6/2016.
 */
public class BankInformation implements Parcelable {

    private int id;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String bankLogoURL;
    private boolean isDropDownOpen;


    public BankInformation(Bank bankServer){

        this.id = bankServer.getId();
        this.bankName = bankServer.getBankName();
        this.bankLogoURL = bankServer.getLogoURL();
        this.accountName = bankServer.getAcountName();
        this.accountNumber = bankServer.getAccountNumber();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankLogoURL() {
        return bankLogoURL;
    }

    public void setBankLogoURL(String bankLogoURL) {
        this.bankLogoURL = bankLogoURL;
    }

    public boolean isDropDownOpen() {
        return isDropDownOpen;
    }

    public void setDropDownOpen(boolean dropDownOpen) {
        isDropDownOpen = dropDownOpen;
    }

    public BankInformation() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.bankName);
        dest.writeString(this.accountName);
        dest.writeString(this.accountNumber);
        dest.writeString(this.bankLogoURL);
        dest.writeByte(isDropDownOpen ? (byte) 1 : (byte) 0);
    }

    protected BankInformation(Parcel in) {
        this.id = in.readInt();
        this.bankName = in.readString();
        this.accountName = in.readString();
        this.accountNumber = in.readString();
        this.bankLogoURL = in.readString();
        this.isDropDownOpen = in.readByte() != 0;
    }

    public static final Creator<BankInformation> CREATOR = new Creator<BankInformation>() {
        public BankInformation createFromParcel(Parcel source) {
            return new BankInformation(source);
        }

        public BankInformation[] newArray(int size) {
            return new BankInformation[size];
        }
    };
}
