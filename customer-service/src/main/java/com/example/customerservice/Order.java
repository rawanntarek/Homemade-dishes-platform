package com.example.customerservice;

public class Order {
private String companyName;
private String dishName;
private int amount;
private double price;
public Order()
{}
public Order(String companyName, String dishName, int amount, double price) {
    this.dishName = dishName;
    this.amount = amount;
    this.price = price;
    this.companyName = companyName;
}
public String getCompanyName() {
    return companyName;
}
public double getPrice() {
    return price;
}
public void setPrice(double price) {
    this.price = price;
}
public void setCompanyName(String companyName) {
    this.companyName = companyName;
}
public String getDishName() {
    return dishName;
}
public int getAmount() {
    return amount;
}
}
