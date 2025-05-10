package com.example.customerservice;

public class Customer {
    String username;
    String password;
    double Balance;
    public Customer() {

    }
    public Customer(String username, String password, double Balance) {
        this.username = username;
        this.password = password;
        this.Balance = 100;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public double getBalance() {return Balance;}
    public void setBalance(double Balance) {
        this.Balance = Balance;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
