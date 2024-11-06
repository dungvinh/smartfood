package com.sel.smartfood.data.model;

public class PaymentAccount {
    long balance;

    public PaymentAccount(){
        this.balance = 0;
    }

    public PaymentAccount(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
