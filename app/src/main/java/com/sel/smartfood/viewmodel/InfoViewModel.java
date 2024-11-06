package com.sel.smartfood.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sel.smartfood.data.local.PreferenceManager;
import com.sel.smartfood.data.model.User;
import com.sel.smartfood.data.remote.firebase.FirebaseInfo;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InfoViewModel extends AndroidViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();
    private FirebaseService firebaseService;
    private CompositeDisposable compositeDisposable;
    private PreferenceManager preferenceManager;

    public InfoViewModel(@NonNull Application application){
        super(application);
        firebaseService = new FirebaseServiceBuilder().addInfo(new FirebaseInfo()).build();
        preferenceManager = new PreferenceManager(application);
        compositeDisposable = new CompositeDisposable();
    }

    public void getUserInfo(){
        String email = getEmailSaved();
        Disposable d = firebaseService.getUser(email.split("@")[0])
                .subscribeOn(Schedulers.io())
                .subscribe(u -> user.postValue(u), e -> user.postValue(null));
        compositeDisposable.add(d);
    }

    public String getEmailSaved(){
        return preferenceManager.getEmail();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void logOut(){
        preferenceManager.deleteLogInState();
        preferenceManager.clearEmail();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }
}
