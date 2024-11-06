package com.sel.smartfood.data.remote.firebase;

public class FirebaseServiceBuilder implements FirebaseService.Builder{
    private FirebaseAuthentication firebaseAuth;
    private FirebasePaymentAccount firebasePaymentAccount;
    private FirebaseRegistration firebaseRegistration;
    private FirebaseProducts firebaseProducts;
    private FirebaseInfo firebaseInfo;

    @Override
    public FirebaseService.Builder addAuth(FirebaseAuthentication auth) {
        this.firebaseAuth = auth;
        return this;
    }

    @Override
    public FirebaseService.Builder addPaymentAccount(FirebasePaymentAccount paymentAccount) {
        this.firebasePaymentAccount = paymentAccount;
        return this;
    }

    @Override
    public FirebaseService.Builder addRegistration(FirebaseRegistration firebaseRegistration) {
        this.firebaseRegistration = firebaseRegistration;
        return this;
    }

    @Override
    public FirebaseService.Builder addProducts(FirebaseProducts firebaseProducts) {
        this.firebaseProducts = firebaseProducts;
        return this;
    }

    @Override
    public FirebaseService.Builder addInfo(FirebaseInfo firebaseInfo) {
        this.firebaseInfo = firebaseInfo;
        return this;
    }

    @Override
    public FirebaseService build() {
        return new FirebaseService(firebaseAuth,
                                   firebasePaymentAccount,
                                   firebaseRegistration,
                                   firebaseProducts,
                                   firebaseInfo);
    }
}
