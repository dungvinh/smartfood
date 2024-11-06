package com.sel.smartfood.data.model;

import androidx.lifecycle.MutableLiveData;

import com.sel.smartfood.data.remote.firebase.FirebaseProducts;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;
import com.sel.smartfood.ui.shop.ICategoryCallbackListener;
import com.sel.smartfood.ui.shop.IProductCallbackListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class ShopRepo implements ICategoryCallbackListener, IProductCallbackListener {
    private FirebaseService firebaseService;
    private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private ICategoryCallbackListener categoryCallbackListener;
    private IProductCallbackListener productCallbackListener;
    public ShopRepo(ICategoryCallbackListener categoryCallbackListener, IProductCallbackListener productCallbackListener){
        this.categoryCallbackListener = categoryCallbackListener;
        this.productCallbackListener = productCallbackListener;

        firebaseService = new FirebaseServiceBuilder().addProducts(new FirebaseProducts(this, this)).build();
    }

    public void getCategoryList(){
        firebaseService.getCategories();
    }

    public void getProductList(){
        firebaseService.getProducts();
    }


    public Single<List<Product>> fetchNewProducts(int position){
        List<Product> productList = new ArrayList<>();
//        int categoryId = findCategoryId(position);
        // find the products which have the same category_id with category_id of current page
        // data sample

         //add description so comment this
        productList.add(new Product(6, 4, "Com ca", 3000, 10, 3,null, null));
        productList.add(new Product(7, 2, "Sinh to", 6000, 8, 4, null, null));
        productList.add(new Product(8, 4, "Cam vat", 10000, 7, 5, null, null));
        return Single.just(productList);
    }

    @Override
    public void OnCategoryLoadSuccess(List<Category> categories) {
        this.categoryCallbackListener.OnCategoryLoadSuccess(categories);
    }

    @Override
    public void OnProductLoadSuccess(List<Product> products) {
        this.productCallbackListener.OnProductLoadSuccess(products);
    }

}
