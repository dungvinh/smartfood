package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sel.smartfood.data.model.OrderHistory;
import com.sel.smartfood.data.model.PaymentAccount;
import com.sel.smartfood.data.model.TransHistory;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface FirebasePaymentAccount {
    @Nullable
    Single<PaymentAccount> getBalance(@NonNull String key);

    void updateBalance(@NonNull String key, @NonNull Long balance);
    void saveTransHistory(String email, Long amountOfMoney, String service, String date, boolean isWithdraw);
    void saveOrderHistory(String email, String productName, int productTotalPrice, int productNumber,String date, String productImage);

    Single<List<TransHistory>> getTransHistories(String email);
    Single<List<TransHistory>> getAllTransHistories();
    Single<List<OrderHistory>> getOrderHistories(String email);
}
