package com.sel.smartfood.data.model;

public class TransHistory {
    private String email;
    private Long amountOfMoney;
    private boolean isWithdraw;
    private String service;
    private String date;

    public TransHistory(){}

    public TransHistory(String email, Long amountOfMoney, String service, String date, boolean isWithdraw) {
        this.email = email;
        this.amountOfMoney = amountOfMoney;
        this.isWithdraw = isWithdraw;
        this.service = service;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Long amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isWithdraw() {
        return isWithdraw;
    }

    public void setWithdraw(boolean withdraw) {
        isWithdraw = withdraw;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
