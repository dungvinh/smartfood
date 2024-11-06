package com.sel.smartfood.ui.shop;

import com.sel.smartfood.data.model.Category;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface ICategoryCallbackListener {
    void OnCategoryLoadSuccess(List<Category> categories);
}
