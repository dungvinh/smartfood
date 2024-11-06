package com.sel.smartfood.data.model;

public class Product {
    private int id;
    private int categoryId;
    private String name;
    private int price;
    private float preparationTime;
    private int ratingScore;
    private String description;
    private String url;
    public Product(){

    }


    public Product(int id, int categoryId, String name, int price, float preparationTime, int ratingScore, String url, String description){
        this.id = id;
        this.categoryId = categoryId;
        this.description = description;
        this.name = name;
        this.price = price;
        this.preparationTime = preparationTime;
        this.ratingScore = ratingScore;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getPrice() {
        return price;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPreparationTime(float preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
