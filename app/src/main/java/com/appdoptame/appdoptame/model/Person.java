package com.appdoptame.appdoptame.model;

public class Person extends User {
    public Person(String ID,
                  String identification,
                  String name,
                  String lastName,
                  String phone,
                  String city,
                  String department,
                  int age) {
        super(ID, identification, name, lastName, phone, city, department, age);
    }
}
