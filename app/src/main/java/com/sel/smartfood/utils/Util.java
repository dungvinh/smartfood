package com.sel.smartfood.utils;

import android.util.Patterns;

public class Util {
    public static boolean isEmailValid(String email){
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6 && password.length() <= 30;
    }
    public static boolean isPhoneNumberValid(String phoneNumber){
        return phoneNumber != null && Patterns.PHONE.matcher(phoneNumber).matches();
    }
    public static boolean isFullnameValid(String fullname){
        return fullname != null && fullname.length() > 2;
    }
}
