package com.sel.smartfood.data.model;

public class PaymentService {
    private int imageSource;
    private String name;
    private boolean isChoosed;

    public PaymentService(int imageSource, String name){
        this.imageSource = imageSource;
        this.name = name;
        this.isChoosed = false;
    }

    public int getImageSource() {
        return imageSource;
    }

    public String getName() {
        return name;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}
