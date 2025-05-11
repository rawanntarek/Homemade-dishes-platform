package com.example.sellerservice;

import java.util.List;

public class Order {
    private String customerName;
    private List<dish_order> dishes;
private String status;
private String orderId;

    public double getTotalPrice() {
        double totalPrice=0;
        for (dish_order dish : dishes) {
            totalPrice+=dish.getPrice()* dish.getAmount();
        }
        return totalPrice;
    }
public Order()
{}
public Order(String customerName, List<dish_order> dishes, String status, String orderId) {
    this.customerName = customerName;
    this.dishes = dishes;
    this.status = "pending";
    this.orderId = orderId;
}
public String getOrderId()
{
    return orderId;
}
public void setOrderId(String orderId)
{
    this.orderId = orderId;
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
