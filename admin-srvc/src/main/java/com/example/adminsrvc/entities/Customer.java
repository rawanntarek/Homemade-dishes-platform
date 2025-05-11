package com.example.adminsrvc.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {
    @JsonProperty("username")
    String username;

    @JsonProperty("password")
    String password;

    @JsonProperty("balance")
    double balance;

    public Customer() {

    }

    public Customer(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
