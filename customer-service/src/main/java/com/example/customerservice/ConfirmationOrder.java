package com.example.customerservice;

public class ConfirmationOrder {
    static String orderId;
    static String CustomerName;
    String Status;
    String message;
    double totalOrderAmount;
    public double getTotalOrderAmount() {
        return totalOrderAmount;
    }
    public void setTotalOrderAmount(double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }
    ConfirmationOrder() {

    }
    ConfirmationOrder(String CustomerName, String Status, String message, String orderId,double totalOrderAmount) {
        this.CustomerName = CustomerName;
        this.Status = Status;
        this.message = message;
        this.orderId = orderId;
        this.totalOrderAmount = totalOrderAmount;
    }
    public static String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public static String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }
    public String getStatus() {
        return Status;
    }
    public void setStatus(String Status) {
        this.Status = Status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
