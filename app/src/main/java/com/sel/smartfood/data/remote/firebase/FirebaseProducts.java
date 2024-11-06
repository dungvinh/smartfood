package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sel.smartfood.data.model.Category;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.ui.shop.ICategoryCallbackListener;
import com.sel.smartfood.ui.shop.IProductCallbackListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class FirebaseProducts {
    private DatabaseReference productsRef;
    private DatabaseReference categoriesRef;
    private ICategoryCallbackListener categoryCallbackListener;
    private IProductCallbackListener productCallbackListener;


    public FirebaseProducts(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.setPersistenceEnabled(true);
        productsRef = firebaseDatabase.getReference().child("Products");
        categoriesRef = firebaseDatabase.getReference().child("Categories");
    }

    public FirebaseProducts(ICategoryCallbackListener categoryCallbackListener, IProductCallbackListener productCallbackListener){
        this.categoryCallbackListener = categoryCallbackListener;
        this.productCallbackListener = productCallbackListener;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.setPersistenceEnabled(true);
        productsRef = firebaseDatabase.getReference().child("Products");
        categoriesRef = firebaseDatabase.getReference().child("Categories");
    }
    // lay cac loai mon an
    public void getCategories(){
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> categories = new ArrayList<>();
                for (DataSnapshot node: snapshot.getChildren()){
                    categories.add(node.getValue(Category.class));
                }
                categoryCallbackListener.OnCategoryLoadSuccess(categories);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryCallbackListener.OnCategoryLoadSuccess(new ArrayList<>());
            }
        });
    }
    // lay het cac mon an
    public void getProducts() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot node : snapshot.getChildren()) {
                    products.add(node.getValue(Product.class));
                }
                productCallbackListener.OnProductLoadSuccess(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                productCallbackListener.OnProductLoadSuccess(new ArrayList<>());
            }
        });
    }
    public Single<Boolean> updateProduct(Product product){
        return Single.create(emitter -> {
                DatabaseReference reference = productsRef.child(String.valueOf(product.getId()));
                reference.child("name").setValue(product.getName()).addOnCompleteListener(task -> {
                    if (emitter.isDisposed() || !task.isSuccessful()){
                        emitter.onError(task.getException());
                    }
                });
                reference.child("price").setValue(product.getPrice()).addOnCompleteListener(task -> {
                    if (emitter.isDisposed() || !task.isSuccessful()){
                        emitter.onError(task.getException());
                    }
                });
                reference.child("description").setValue(product.getDescription()).addOnCompleteListener(task -> {
                    if (emitter.isDisposed() || !task.isSuccessful()){
                        emitter.onError(task.getException());
                    }
                    else{
                        emitter.onSuccess(true);
                    }
                });

            }
        );
    }
}
