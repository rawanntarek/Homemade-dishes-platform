package com.example.sellerservice;

public class Dish {
    public String name;
    public int amount;
    public double price;
    public Dish()
    {

    }
    public Dish(String name, int amount, double price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
