package com.example.customerservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dish {
    @JsonProperty("name")
    private String name;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("price")
    private double price;
    @JsonProperty("amount")
    private int amount;

}
