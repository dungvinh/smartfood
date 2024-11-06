package com.sel.smartfood.ui.shop;

import com.sel.smartfood.data.model.Product;

import java.util.List;

public interface IProductCallbackListener {
    void OnProductLoadSuccess(List<Product> products);
}
