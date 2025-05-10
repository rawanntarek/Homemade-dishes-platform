package com.example.sellerservice;

public class ConfirmationOrder {
    String orderId;
    String CustomerName;
    String Status;
    String message;
    ConfirmationOrder() {

    }
    ConfirmationOrder(String CustomerName, String Status, String message, String orderId) {
        this.CustomerName = CustomerName;
        this.Status = Status;
        this.message = message;
        this.orderId = orderId;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getCustomerName() {
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
