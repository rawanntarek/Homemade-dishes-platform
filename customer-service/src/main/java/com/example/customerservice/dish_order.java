package com.example.customerservice;

public class dish_order {
    private String dishName;
    private int amount;
    private double price;
    private String companyName;
    dish_order()
    {

    }
    dish_order(String dishName, int amount, double price, String companyName)
    {
        this.dishName = dishName;
        this.amount = amount;
        this.price = price;
        this.companyName = companyName;
    }
    public String getDishName() {
        return dishName;
    }
    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

