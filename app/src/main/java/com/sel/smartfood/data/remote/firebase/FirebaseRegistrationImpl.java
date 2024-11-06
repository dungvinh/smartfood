package com.sel.smartfood.data.remote.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sel.smartfood.data.model.PaymentAccount;
import com.sel.smartfood.data.model.User;

import io.reactivex.rxjava3.core.Completable;

public class FirebaseRegistrationImpl implements FirebaseRegistration{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
    private DatabaseReference paymentRef;
    public FirebaseRegistrationImpl(){
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("UsersInfo");
        paymentRef = FirebaseDatabase.getInstance().getReference().child("PaymentAccounts");
    }
    public Completable register(String email, String password, String fullname, String phone){
        Exception arr[] = {null};
        return Completable.create(emitter -> {
           firebaseAuth.createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener(task -> {
                           if (!emitter.isDisposed() && task.isSuccessful()){
//                               uid[0] = Objects.requireNonNull(task.getResult().getUser()).getUid();
                           }
                           else{
                               arr[0] = task.getException();
                           }
                       });
           if (arr[0] != null){
               emitter.onError(arr[0]);
               return;
           }
            paymentRef.child(email.split("@")[0]).setValue(new PaymentAccount(), (databaseError, dataReference) -> {
               if (databaseError != null){
                   arr[0] = databaseError.toException();
               }
            });
            if (arr[0] != null){
                emitter.onError(arr[0]);
                return;
            }
           userRef.child(email.split("@")[0]).setValue(new User(fullname, phone, email), (databaseError, databaseReference) -> {
                if (databaseError == null){
                    emitter.onComplete();
                }
                else{
                    emitter.onError(new Exception(databaseError.getMessage()));
                }
           });
        });
    }
}
