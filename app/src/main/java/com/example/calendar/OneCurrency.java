package com.example.calendar;

public class OneCurrency {

    public OneCurrency(String buy, String sale, String name) {
        super();

        this.buy = buy;
        this.sale = sale;
        this.name = name;
    }

    String buy;
    String sale;

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
