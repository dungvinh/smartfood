package com.sel.smartfood.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sel.smartfood.data.local.PreferenceManager;
import com.sel.smartfood.data.model.Emitter;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.data.model.TransHistory;
import com.sel.smartfood.data.model.User;
import com.sel.smartfood.data.remote.firebase.FirebaseInfo;
import com.sel.smartfood.data.remote.firebase.FirebasePaymentAccountImpl;
import com.sel.smartfood.data.remote.firebase.FirebaseProducts;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminViewModel extends AndroidViewModel {
    private PreferenceManager preferenceManager;
    private FirebaseService firebaseService;
    private MutableLiveData<User> userInfo = new MutableLiveData<>();
    private MutableLiveData<List<TransHistory>> tranHistories = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<Boolean> isUpdateButtonEnabled = new MutableLiveData<>();
    private MutableLiveData<Emitter<Boolean> >isUpdateSucess = new MutableLiveData<>();
    private MutableLiveData<Integer[]> numTypeTrans = new MutableLiveData<>();
    public AdminViewModel(@NonNull Application application) {
        super(application);
        preferenceManager = new PreferenceManager(application);
        firebaseService = new FirebaseServiceBuilder().addInfo(new FirebaseInfo())
                                                      .addPaymentAccount(new FirebasePaymentAccountImpl())
                                                      .addProducts(new FirebaseProducts())
                                                      .build();
        compositeDisposable = new CompositeDisposable();
    }

    public void getUser(){
        String key = preferenceManager.getEmail().split("@")[0];
        Disposable d = firebaseService.getUser(key)
                        .subscribeOn(Schedulers.io())
                        .subscribe(u -> userInfo.postValue(u), e -> userInfo.postValue(null));
        compositeDisposable.add(d);
    }
    public void getAllTransHistories(){
        Disposable d = firebaseService.getAllTransHistories()
                    .subscribeOn(Schedulers.io())
                    .subscribe(histories -> {
                            int withdraw = 0, deposit = 0;
                            for (TransHistory history : histories){
                                if (history.isWithdraw()){
                                    ++withdraw;
                                }
                                else{
                                    ++deposit;
                                }
                            }
                            numTypeTrans.postValue(new Integer[]{withdraw, deposit});
                            tranHistories.postValue(histories);
                        }, e -> tranHistories.postValue(null));
        compositeDisposable.add(d);
    }

    public void logout(){
        preferenceManager.deleteLogInState();
        preferenceManager.clearEmail();
    }

    public void updateProduct(Product product){
        Disposable d = firebaseService.updateProduct(product)
                        .subscribeOn(Schedulers.io())
                        .subscribe(rs -> isUpdateSucess.postValue(new Emitter<>(rs)), e -> isUpdateSucess.postValue(new Emitter<>(false)));
        compositeDisposable.add(d);
    }

    public void changeData(){
        isUpdateButtonEnabled.setValue(true);
    }

    public LiveData<List<TransHistory>> getTranHistories() {
        return tranHistories;
    }

    public LiveData<User> getUserInfo() {
        return userInfo;
    }

    public LiveData<Boolean> isUpdateButtonEnabled() {
        return isUpdateButtonEnabled;
    }

    public LiveData<Emitter<Boolean>> isUpdateSucess() {
        return isUpdateSucess;
    }

    public MutableLiveData<Integer[]> getNumTypeTrans() {
        return numTypeTrans;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

}
