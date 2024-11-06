package com.sel.smartfood.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sel.smartfood.data.model.Emitter;
import com.sel.smartfood.data.remote.firebase.FirebaseAuthenticationImpl;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;
import com.sel.smartfood.utils.Util;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPasswordViewModel extends ViewModel {
    private MutableLiveData<Boolean> isEmailValid = new MutableLiveData<>();
    private MutableLiveData<Emitter<Boolean>> isSentMail = new MutableLiveData<>();
    private FirebaseService firebaseService = new FirebaseServiceBuilder()
                                                .addAuth(new FirebaseAuthenticationImpl())
                                                .build();
    public static int TIME_OUT_SECONDS = 10;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void resetPassword(String email){
        Disposable d = firebaseService.resetPassword(email)
                        .timeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .subscribe(()-> isSentMail.postValue(new Emitter<>(true)),
                                e-> {isSentMail.postValue(new Emitter<>(false));});
        compositeDisposable.add(d);
    }

    public void emailChanged(String email){
        if (Util.isEmailValid(email)){
            isEmailValid.setValue(true);
        }
        else{
            isEmailValid.setValue(false);
        }
    }

    public LiveData<Boolean> isEmailValid() {
        return isEmailValid;
    }

    public LiveData<Emitter<Boolean>> isSentMail() {
        return isSentMail;
    }
}
