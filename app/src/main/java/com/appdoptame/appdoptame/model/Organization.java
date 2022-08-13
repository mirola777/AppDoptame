package com.appdoptame.appdoptame.model;

public class Organization extends User {
    private final String address;
    private final String website;

    public Organization(String ID,
                  String identification,
                  String name,
                  String phone,
                  String address,
                  String city,
                  String department,
                  String image,
                  String website,
                        long age) {
        super(ID, identification, name, phone, city, department, image, age);
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
