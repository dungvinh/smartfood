package com.sel.smartfood.data.model;

public class Category {
    private int id;
    private String name;
    private String url;

    public Category(){}
    public Category(int id, String name, String url){
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
