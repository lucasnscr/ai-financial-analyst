package com.lucasnscr.langchain4jdemo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "Stocks")
public class Stock {

    @Id
    private String name;
    private long volume;
    private double volumeWeightedAveragePrice;
    private double priceOpenMarket;
    private double priceCloseMarket;
    private double priceHighest;
    private double priceLess;
    private LocalDate date;
    private long transactionsDay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getVolumeWeightedAveragePrice() {
        return volumeWeightedAveragePrice;
    }

    public void setVolumeWeightedAveragePrice(double volumeWeightedAveragePrice) {
        this.volumeWeightedAveragePrice = volumeWeightedAveragePrice;
    }

    public double getPriceOpenMarket() {
        return priceOpenMarket;
    }

    public void setPriceOpenMarket(double priceOpenMarket) {
        this.priceOpenMarket = priceOpenMarket;
    }

    public double getPriceCloseMarket() {
        return priceCloseMarket;
    }

    public void setPriceCloseMarket(double priceCloseMarket) {
        this.priceCloseMarket = priceCloseMarket;
    }

    public double getPriceHighest() {
        return priceHighest;
    }

    public void setPriceHighest(double priceHighest) {
        this.priceHighest = priceHighest;
    }

    public double getPriceLess() {
        return priceLess;
    }

    public void setPriceLess(double priceLess) {
        this.priceLess = priceLess;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getTransactionsDay() {
        return transactionsDay;
    }

    public void setTransactionsDay(long transactionsDay) {
        this.transactionsDay = transactionsDay;
    }
}
