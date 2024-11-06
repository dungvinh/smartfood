package com.sel.smartfood.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sel.smartfood.data.local.PreferenceManager;
import com.sel.smartfood.data.model.Emitter;
import com.sel.smartfood.data.model.PaymentAccount;
import com.sel.smartfood.data.remote.firebase.FirebasePaymentAccountImpl;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;
import com.sel.smartfood.ui.transaction.IBalanceCallbackListener;

import java.util.Calendar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PaymentShopcartViewModel extends AndroidViewModel implements IBalanceCallbackListener {
    private MutableLiveData<Boolean> isPaymentButtonEnabled = new MutableLiveData<>();
    private MutableLiveData<PaymentAccount> paymentAccount = new MutableLiveData<>();
    private MutableLiveData<Emitter<Boolean>> isUpdatedSuccessful = new MutableLiveData<>();

    private FirebaseService firebaseService;
    private PreferenceManager preferenceManager;
    private CompositeDisposable compositeDisposable;
    private boolean isWithdraw;
    private String service;

    private String productName;
    private int productTotalPrice;
    private int productNumber;
    private String productImage;

    public PaymentShopcartViewModel(@NonNull Application application) {
        super(application);
        firebaseService = new FirebaseServiceBuilder().addPaymentAccount(new FirebasePaymentAccountImpl(this)).build();
        compositeDisposable = new CompositeDisposable();
        preferenceManager = new PreferenceManager(application);
    }
    public void getBalance(){
        String key = preferenceManager.getEmail().split("@")[0];
        Disposable d = firebaseService.getBalance(key)
                .subscribeOn(Schedulers.io())
                .subscribe(account -> paymentAccount.postValue(account), e -> paymentAccount.postValue(null));
        compositeDisposable.add(d);
    }
    public boolean isEnoughBalance(long amount){
        return paymentAccount != null ? paymentAccount.getValue().getBalance() >= amount : false;
    }
    public void updateBalance(long amountOfMoney){
        String key = preferenceManager.getEmail().split("@")[0];
        amountOfMoney = paymentAccount.getValue().getBalance() - amountOfMoney;
        firebaseService.updateBalance(key, amountOfMoney);
    }
    public void saveTransHistories(long amountOfMoney){
        String date = Calendar.getInstance().getTime().toString();
        firebaseService.saveTransHistory(preferenceManager.getEmail(), amountOfMoney, service, date, isWithdraw);
    }

    public void saveOrderHistory(String productName, int productTotalPrice, int productNumber, String productImage){
        String date = Calendar.getInstance().getTime().toString();
        firebaseService.saveOrderHistory(preferenceManager.getEmail(), productName, productTotalPrice, productNumber, date, productImage);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    public boolean isWithdraw() {
        return isWithdraw;
    }

    public void setWithdraw(boolean withdraw) {
        isWithdraw = withdraw;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductTotalPrice(int productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public LiveData<Boolean> getIsPaymentButtonEnabled() {
        return isPaymentButtonEnabled;
    }

    public LiveData<PaymentAccount> getPaymentAccount() {
        return paymentAccount;
    }

    public LiveData<Emitter<Boolean>> getIsUpdatedSuccessful() {
        return isUpdatedSuccessful;
    }

    public void amountOfMoneyChange(String number){
        try{
            int amount = Integer.parseInt(number);
            if (amount > 0 && amount % 1000 == 0){
                isPaymentButtonEnabled.setValue(true);
            }
            else{
                isPaymentButtonEnabled.setValue(false);
            }
        }
        catch (NumberFormatException e){
            isPaymentButtonEnabled.setValue(false);
        }

    }


    @Override
    public void onSuccess(boolean isSuccess) {
        isUpdatedSuccessful.setValue(new Emitter<>(isSuccess));
    }

}
