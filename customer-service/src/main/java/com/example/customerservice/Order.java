package com.example.customerservice;

import java.util.List;

public class Order {
    private String customerName;
    private List<dish_order> dishes;
private String status;
public Order()
{}
public Order(String customerName, List<dish_order> dishes, String status) {
    this.customerName = customerName;
    this.dishes = dishes;
    this.status = "pending";
}
public String getCustomerName() {
    return customerName;
}
public void setCustomerName(String customerName) {
    this.customerName = customerName;
}
public List<dish_order> getDishes() {
    return dishes;
}
public void setDishes(List<dish_order> dishes) {
    this.dishes = dishes;
}
public String getStatus() {
    return status;
}
public void setStatus(String status) {
    this.status = status;
}
}
