package com.example.calendar;

public class OneStock {

    public String symbol;
    public String name;
    public String ask;
    public String volume;
    public String change_percent;
    public String currency;
    private String book_value;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public OneStock(String symbol, String name, String ask, String volume,
                    String change_percent, String currency, String book_value) {
        super();
        this.symbol = symbol;
        this.name = name;
        this.ask = ask;
        this.volume = volume;
        this.change_percent = change_percent;
        this.currency = currency;

        setBook_value(book_value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getChange_percent() {
        return change_percent;
    }

    public void setChange_percent(String change_percent) {
        this.change_percent = change_percent;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBook_value() {
        return book_value;
    }

    public void setBook_value(String book_value) {
        this.book_value = book_value;
    }

}
