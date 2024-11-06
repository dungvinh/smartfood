package com.sel.smartfood.data.model;

public class ShopCartModel {
    public int productId;
    public String productName;
    public int productPrice;
    public String productImage;
    public int productNumbers;
    public String productDescriptions;

    public ShopCartModel(int productId, String productName, int productPrice, String productImage, int productNumbers, String productDescriptions) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productNumbers = productNumbers;
        this.productDescriptions = productDescriptions;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductDescriptions() {
        return productDescriptions;
    }

    public void setProductDescriptions(String productDescriptions) {
        this.productDescriptions = productDescriptions;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductNumbers() {
        return productNumbers;
    }

    public void setProductNumbers(int productNumbers) {
        this.productNumbers = productNumbers;
    }
}
