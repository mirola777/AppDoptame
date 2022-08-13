package com.appdoptame.appdoptame.model;

public class User {
    private final String ID;
    private final String identification;
    private final String name;
    private final String lastName;
    private final String phone;
    private final String city;
    private final String department;
    private final int age;

    public User(String ID,
               String identification,
               String name,
               String lastName,
               String phone,
               String city,
               String department,
               int age){
        this.ID = ID;
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.city = city;
        this.department = department;
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getIdentification() {
        return identification;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }
}
