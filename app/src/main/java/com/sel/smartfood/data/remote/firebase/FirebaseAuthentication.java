package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;

public interface FirebaseAuthentication {
    @NonNull
    Completable loginWithEmail(String username, String password);
    @Nullable
    Completable resetPassword(String email);
    @Nullable
    FirebaseUser getCurrentUser();
}
