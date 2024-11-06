package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sel.smartfood.data.model.User;

import io.reactivex.rxjava3.core.Single;

public class FirebaseInfo {
    private DatabaseReference ref;
    public FirebaseInfo(){
        ref = FirebaseDatabase.getInstance().getReference("UsersInfo");
    }
    public Single<User> getUser(String key){
        return Single.create((emitter)->{
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot node : snapshot.getChildren()){
                        if (node.getKey().equals(key)){
                            emitter.onSuccess(node.getValue(User.class));
                            return;
                        }
                    }
                    emitter.onError(new Exception("Error"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });

        });
    }
}
