package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sel.smartfood.data.model.OrderHistory;
import com.sel.smartfood.data.model.PaymentAccount;
import com.sel.smartfood.data.model.TransHistory;
import com.sel.smartfood.ui.transaction.IBalanceCallbackListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;

public class FirebasePaymentAccountImpl implements FirebasePaymentAccount {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private DatabaseReference historiesRef;
    private DatabaseReference historiesOrder;
    private IBalanceCallbackListener balanceCallbackListener;

    public FirebasePaymentAccountImpl(IBalanceCallbackListener balanceCallbackListener){
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference().child("PaymentAccounts");
        historiesRef = firebaseDatabase.getReference().child("TransHistories");
        historiesOrder = firebaseDatabase.getReference().child("OrderHistories");
        this.balanceCallbackListener = balanceCallbackListener;
    }
    public FirebasePaymentAccountImpl(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference().child("PaymentAccounts");
        historiesRef = firebaseDatabase.getReference().child("TransHistories");
    }

    @Nullable
    @Override
    public Single<PaymentAccount> getBalance(@NonNull String key) {
        return Single.create(emitter -> {
            ref.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() != 1){
                        emitter.onError(new Exception("Error"));
                        return;
                    }
                    for (DataSnapshot node : snapshot.getChildren()){
                        emitter.onSuccess(new PaymentAccount(node.getValue(Long.class)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        });
    }
    @Override
    public void updateBalance(@NonNull String key, @NonNull Long balance) {
            ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() != 1){
                       balanceCallbackListener.onSuccess(false);
                        return;
                    }
                    ref.child(key).child("balance").setValue(balance);
                    balanceCallbackListener.onSuccess(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    balanceCallbackListener.onSuccess(false);
                }
            });
    }
    @Override
    public void saveTransHistory(String email, Long amountOfMoney, String service, String date, boolean isWithdraw){
        String key = email.split("@")[0];
        historiesRef.child(key).push().setValue(new TransHistory(email, amountOfMoney, service, date, isWithdraw));
    }
    public void saveOrderHistory(String email, String productName, int productTotalPrice, int productNumber, String date ,String productImage){
        String key = email.split("@")[0];
        historiesOrder.child(key).push().setValue(new OrderHistory(productName, productTotalPrice,productNumber, date,productImage));
    }

    @Override
    public Single<List<TransHistory>> getTransHistories(String email) {
        String key = email.split("@")[0];
        return Single.create(emitter -> {
            historiesRef.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<TransHistory> transHistories = new ArrayList<>();
                    Map<String, Object> td = (HashMap<String, Object>)snapshot.getValue();
                    for (Object obj : td.values()){
                        Map<String, Object> map = (Map<String, Object>)obj;

                        transHistories.add(new TransHistory((String)map.get("email"), (Long)map.get("amountOfMoney"), (String) map.get("service"), (String)map.get("date"), (Boolean)map.get("withdraw")));
                    }
                    emitter.onSuccess(transHistories);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        });
    }

    @Override
    public Single<List<TransHistory>> getAllTransHistories() {
        return Single.create(emitter -> {
            historiesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<TransHistory> transHistories = new ArrayList<>();
                    Map<String, Object> td = (HashMap<String, Object>) snapshot.getValue();
                    for (Object obj : td.values()) {
                        Map<String, Object> m = (Map<String, Object>) obj;
                        for (Object b : m.values()) {
                            Map<String, Object> map = (Map<String, Object>) b;
                            transHistories.add(new TransHistory((String) map.get("email"), (Long) map.get("amountOfMoney"), (String) map.get("service"), (String) map.get("date"), (Boolean) map.get("withdraw")));
                        }
                    }
                    emitter.onSuccess(transHistories);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        });
    }

    public Single<List<OrderHistory>> getOrderHistories(String email) {
        String key = email.split("@")[0];
        return Single.create(emitter -> {
            historiesOrder.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<OrderHistory> orderHistories = new ArrayList<>();
                    Map<String, Object> td = (HashMap<String, Object>)snapshot.getValue();
                    for(Object obj : td.values()){
                        Map<String, Object>map = (Map<String, Object>)obj;

                        orderHistories.add(new OrderHistory((String)map.get("productName"), (long)map.get("productTotalPrice"),
                                (long)map.get("productNumber"),(String)map.get("date"), (String)map.get("productImage")));
                    }
                    emitter.onSuccess(orderHistories);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());

                }
            });
        });
    }
}
