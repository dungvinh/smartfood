package com.sel.smartfood.data.model;



public class OrderHistory{
    private String  productName;
    private long productTotalPrice;
    private long  productNumber;
    private String date;
    private String  productImage;

    public OrderHistory(){}

    public OrderHistory(String productName, long productTotalPrice, long productNumber,String date, String productImage) {
        this.productName = productName;
        this.productTotalPrice = productTotalPrice;
        this.productNumber = productNumber;
        this.date = date;
        this.productImage = productImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(long productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public long getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
