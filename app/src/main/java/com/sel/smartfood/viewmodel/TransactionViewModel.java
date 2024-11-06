package com.sel.smartfood.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sel.smartfood.R;
import com.sel.smartfood.data.local.PreferenceManager;
import com.sel.smartfood.data.model.Emitter;
import com.sel.smartfood.data.model.OrderHistory;
import com.sel.smartfood.data.model.PaymentAccount;
import com.sel.smartfood.data.model.PaymentService;
import com.sel.smartfood.data.model.TransHistory;
import com.sel.smartfood.data.remote.firebase.FirebasePaymentAccountImpl;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;
import com.sel.smartfood.ui.transaction.IBalanceCallbackListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TransactionViewModel extends AndroidViewModel implements IBalanceCallbackListener {
    private MutableLiveData<List<PaymentService>> paymentService = new MutableLiveData<>();
    private List<PaymentService> paymentServiceList;
    private MutableLiveData<Boolean> isNextButtonEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTransactionButtonEnabled = new MutableLiveData<>();
    private MutableLiveData<PaymentAccount> paymentAccount = new MutableLiveData<>();
    private MutableLiveData<Emitter<Boolean>> isUpdatedSuccessful = new MutableLiveData<>();
    private MutableLiveData<List<TransHistory>> transHistoryList = new MutableLiveData<>();
    private MutableLiveData<List<OrderHistory>> orderHistoryList = new MutableLiveData<>();

    private FirebaseService firebaseService;
    private PreferenceManager preferenceManager;
    private CompositeDisposable compositeDisposable;
    private boolean isWithdraw;
    private String service;

    public TransactionViewModel(Application application){
        super(application);
        paymentServiceList = getPaymentServiceList();
        this.paymentService.setValue(paymentServiceList);
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
    public void updateBalance(long amountOfMoney){
        String key = preferenceManager.getEmail().split("@")[0];
        amountOfMoney = paymentAccount.getValue().getBalance() + (isWithdraw ? - amountOfMoney : amountOfMoney);
        firebaseService.updateBalance(key, amountOfMoney);
    }

    public boolean isEnoughBalance(long amountOfMoney){
        if (paymentAccount.getValue() != null){
            return amountOfMoney <= paymentAccount.getValue().getBalance();
        }
        return false;
    }

    public boolean isEmptyBalance(){
        if (paymentAccount.getValue() != null){
            return paymentAccount.getValue().getBalance() == 0;
        }
        return false;
    }

    public void saveTransHistories(long amountOfMoney){
        String date = Calendar.getInstance().getTime().toString();
        firebaseService.saveTransHistory(preferenceManager.getEmail(), amountOfMoney, service, date, isWithdraw);
    }
    public void saveOrderHistory(String productName, int productTotalPrice, int productNumber, String productImage){
        String date = Calendar.getInstance().getTime().toString();
        firebaseService.saveOrderHistory(preferenceManager.getEmail(), productName, productTotalPrice, productNumber, date, productImage);
    }

    public void getTransHistories(){
        String email = preferenceManager.getEmail();
        Disposable d = firebaseService.getTransHistories(email)
                .subscribeOn(Schedulers.io())
                .subscribe(list -> transHistoryList.postValue(list), e -> transHistoryList.postValue(null));
        compositeDisposable.add(d);
    }

    public void getOrderHistories(){
        String email = preferenceManager.getEmail();
        Disposable d = firebaseService.getOrderHistories(email)
                .subscribeOn(Schedulers.io())
                .subscribe(list -> orderHistoryList.postValue(list), e -> orderHistoryList.postValue(null));
        compositeDisposable.add(d);
    }

    public void setPaymentService(int position){
        this.service = paymentServiceList.get(position).getName();
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

    public void setTransactionButtonEnabled(boolean flag){
        isTransactionButtonEnabled.setValue(flag);
    }
    public void setWithdraw(boolean withdraw) {
        isWithdraw = withdraw;
    }

    public LiveData<List<PaymentService>> getPaymentService() {
        return paymentService;
    }

    public LiveData<Boolean> getIsNextButtonEnabled() {
        return isNextButtonEnabled;
    }

    public LiveData<Boolean> getIsTransactionButtonEnabled() {
        return isTransactionButtonEnabled;
    }

    public LiveData<PaymentAccount> getPaymentAccount() {
        return paymentAccount;
    }

    public LiveData<Emitter<Boolean>> getIsUpdatedSuccessful() {
        return isUpdatedSuccessful;
    }

    public LiveData<List<TransHistory>> getTransHistoryList() {
        return transHistoryList;
    }

    public LiveData<List<OrderHistory>> getOrderHistoryList() {
        return orderHistoryList;
    }

    public void amountOfMoneyChange(String number){
        try{
            int amount = Integer.parseInt(number);
            if (amount > 0 && amount % 1000 == 0){
                isTransactionButtonEnabled.setValue(true);
            }
            else{
                isTransactionButtonEnabled.setValue(false);
            }
        }
        catch (NumberFormatException e){
            isTransactionButtonEnabled.setValue(false);
        }

    }

    public void chooseOnePaymentService(int position){
        boolean flag = false;
        for (int i = 0; i < paymentServiceList.size(); i++){
            if (i == position){
                PaymentService paymentChoosed = paymentServiceList.get(i);
                paymentChoosed.setChoosed(!paymentChoosed.isChoosed());
                flag = paymentChoosed.isChoosed();
            }
            else{
                paymentServiceList.get(i).setChoosed(false);
            }
        }
        this.paymentService.setValue(paymentServiceList);
        this.isNextButtonEnabled.setValue(flag);
    }
    private List<PaymentService> getPaymentServiceList(){
        int[] paymentImageSources = getPaymentImageSources();
        String[] paymentServiceNames = getPaymentServiceNames();
        List<PaymentService> list = new ArrayList<>();
        int size = paymentImageSources.length;
        for (int i = 0; i < size; i++){
            list.add(new PaymentService(paymentImageSources[i], paymentServiceNames[i]));
        }
        return list;
    }
    private int[] getPaymentImageSources(){
        return new int[]{
                R.drawable.momo_pay,
                R.drawable.viettel_pay,
                R.drawable.samsung_pay,
                R.drawable.apple_pay
        };
    }
    private String[] getPaymentServiceNames(){
        return new String[]{
          "Ví Momo",
          "ViettelPay",
          "SamsungPay",
          "ApplePay"
        };
    }

    @Override
    public void onSuccess(boolean isSuccess) {
        isUpdatedSuccessful.setValue(new Emitter<>(isSuccess));
    }
}
