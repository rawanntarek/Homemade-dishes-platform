package com.example.adminsrvc.entities;

public class Seller {
    String companyName;
    String companyPassword;

    public Seller()
    {

    }
    public Seller(String companyName, String companyPassword) {
        this.companyName = companyName;
        this.companyPassword = companyPassword;
    }
    public String getCompanyName() {
        return companyName;
    }
    public String getCompanyPassword() {
        return companyPassword;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public void setCompanyPassword(String companyPassword) {
        this.companyPassword = companyPassword;
    }
}
