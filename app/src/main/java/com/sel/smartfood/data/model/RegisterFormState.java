package com.sel.smartfood.data.model;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    Integer fullnameError;
    @Nullable
    Integer emailError;
    @Nullable
    Integer passwordError;
    @Nullable
    Integer repasswordError;
    @Nullable
    Integer phoneNumberError;
    boolean isDataValid;

    public RegisterFormState(@Nullable Integer fullnameError,
                             @Nullable Integer emailError,
                             @Nullable Integer phoneNumberError,
                             @Nullable Integer passwordError,
                             @Nullable Integer repasswordError){

        this.fullnameError = fullnameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.phoneNumberError = phoneNumberError;
        this.repasswordError = repasswordError;
        this.isDataValid = false;
    }

    public RegisterFormState(boolean isDataValid){
        this.fullnameError = this.emailError = this.passwordError = this.repasswordError = this.phoneNumberError = null;
        this.isDataValid = true;
    }

    @Nullable
    public Integer getFullnameError() {
        return fullnameError;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getRepasswordError() {
        return repasswordError;
    }

    @Nullable
    public Integer getPhoneNumberError() {
        return phoneNumberError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
