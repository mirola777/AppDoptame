package com.appdoptame.appdoptame.model;

public class User {
    private String ID;
    private final String identification;
    private final String name;
    private final String phone;
    private final String city;
    private final String department;
    private final String image;
    private final String lastName;
    private final long age;

    public User(String ID,
               String identification,
               String name,
               String lastName,
               String phone,
               String city,
               String department,
               String image,
               long age){
        this.ID = ID;
        this.lastName = lastName;
        this.identification = identification;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.department = department;
        this.age = age;
        this.image = image;
    }

    public User(
                String identification,
                String name,
                String lastName,
                String phone,
                String city,
                String department,
                String image,
                long age){

        this(null, identification, name, lastName, phone, city, department, image, age);
    }


    public String getLastName() {
        return lastName;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImage() {
        return image;
    }

    public String getDepartment() {
        return department;
    }

    public String getCity() {
        return city;
    }

    public long getAge() {
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

    public String getPhone() {
        return phone;
    }
}
