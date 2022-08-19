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
                  String image,
                  long age) {
        super(ID, identification, name, phone, city, department, image, age);
        this.lastName = lastName;
    }

    public Person(String identification,
                  String name,
                  String lastName,
                  String phone,
                  String city,
                  String department,
                  String image,
                  long age) {
        super(null, identification, name, phone, city, department, image, age);
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}
