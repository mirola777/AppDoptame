package com.appdoptame.appdoptame.model;

public class Person extends User {
    private final String lastName;

    public Person(String ID,
                  String identification,
                  String name,
                  String lastName,
                  String phone,
                  String city,
                  String department,
                  int age) {
        super(ID, identification, name, phone, city, department, age);
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}
