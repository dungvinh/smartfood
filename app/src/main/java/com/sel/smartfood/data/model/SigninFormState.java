package com.sel.smartfood.data.model;

import androidx.annotation.Nullable;

public class SigninFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;
    public SigninFormState(@Nullable Integer usernameError, @Nullable Integer passwordError){
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }
    public SigninFormState(boolean isDataValid){
        this.usernameError = this.passwordError = null;
        this.isDataValid = true;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
