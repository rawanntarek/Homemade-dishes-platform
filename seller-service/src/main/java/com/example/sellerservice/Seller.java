package com.example.sellerservice;

public class Seller {
    private String companyName;
    private String password;

    public Seller() {}

    public Seller(String companyName, String password) {
        this.companyName = companyName;
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
