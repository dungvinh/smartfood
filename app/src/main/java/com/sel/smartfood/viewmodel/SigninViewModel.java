package com.sel.smartfood.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.sel.smartfood.R;
import com.sel.smartfood.data.local.PreferenceManager;
import com.sel.smartfood.data.model.Emitter;
import com.sel.smartfood.data.model.Result;
import com.sel.smartfood.data.model.SigninFormState;
import com.sel.smartfood.data.remote.firebase.FirebaseAuthenticationImpl;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;
import com.sel.smartfood.utils.Util;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SigninViewModel extends AndroidViewModel {
    public final static int TIME_OUT_SEC = 10;

    public final static String UNAVAILABLE_SERVICE_MESSAGE = "Dịch vụ không có sẵn. Vui lòng cài Google Play";
    public final static String LOGIN_ERROR_MESSAGE = "Đã có lỗi xảy ra! Vui lòng thử lại";
    public final static String WRONG_USER = "Tài khoản không tồn tại";
    public final static String WRONG_PASSWORD = "Mật khẩu không đúng";

    private MutableLiveData<SigninFormState> signinFormState = new MutableLiveData<>();
    private MutableLiveData<Emitter<Result<Boolean>>> signinResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    private FirebaseService firebaseService = new FirebaseServiceBuilder()
                                                    .addAuth(new FirebaseAuthenticationImpl())
                                                    .build();
    private PreferenceManager preferenceManager;
    private CompositeDisposable compositeDisposable;


    public SigninViewModel(@NonNull Application application) {
        super(application);
        preferenceManager = new PreferenceManager(application);
    }

    public void login(String username, String password){
        // dang nhap voi firebase
        Disposable d = firebaseService.login(username, password)
                .timeout(TIME_OUT_SEC, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(()-> {
                    signinResult.postValue(new Emitter<>(new Result.Success<>(true)));
                    preferenceManager.saveLogInState();
                    preferenceManager.setEmail(username);
                    if (username.contains("admin")){
                        preferenceManager.setAdminState();
                    }
                }, this::handleLoginWithEmailError);
        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }
    private void handleLoginWithEmailError(Throwable e){
        // thong bao khi dang nhap loi
        if (e instanceof FirebaseApiNotAvailableException){
            signinResult.postValue(new Emitter<>(new Result.Error(new Exception(UNAVAILABLE_SERVICE_MESSAGE))));
        }
        else if (e instanceof FirebaseAuthInvalidUserException){
            signinResult.postValue(new Emitter<>(new Result.Error(new Exception(WRONG_USER))));
        }
        else if (e instanceof FirebaseAuthInvalidCredentialsException){
            signinResult.postValue(new Emitter<>(new Result.Error(new Exception(WRONG_PASSWORD))));
        }
        else{
            signinResult.postValue(new Emitter<>(new Result.Error(new Exception(LOGIN_ERROR_MESSAGE))));
        }
    }

    public void loginDataChanged(String username, String password){
        if (!Util.isEmailValid(username)){
            signinFormState.setValue(new SigninFormState(R.string.invalid_username, null));
        }
        else if (!Util.isPasswordValid(password)){
            signinFormState.setValue(new SigninFormState(null, R.string.invalid_password));
        }
        else{
            signinFormState.setValue(new SigninFormState(true));
        }
    }

    public void checkLoggedInState(){
        // kiem tra co dang nhap chua
        isLoggedIn.setValue(preferenceManager.getLogInState());
    }

    public boolean isAdmin(){
        return preferenceManager.isAdmin();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()){
            compositeDisposable.clear();
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public LiveData<Emitter<Result<Boolean>>> getSigninResult() {
        return signinResult;
    }
    public LiveData<SigninFormState> getSigninFormState() {
        return signinFormState;
    }
    // livedata - observer
    public LiveData<Boolean> IsLoggedIn() {
        return isLoggedIn;
    }



}
