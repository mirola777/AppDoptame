package com.appdoptame.appdoptame.model;

public class Organization extends User {
    private final String address;
    private final String website;

    public Organization(String ID,
                  String identification,
                  String name,
                  String lastName,
                  String phone,
                  String address,
                  String city,
                  String department,
                  String website,
                  int age) {
        super(ID, identification, name, lastName, phone, city, department, age);
        this.address = address;
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public String getAddress() {
        return address;
    }
}
