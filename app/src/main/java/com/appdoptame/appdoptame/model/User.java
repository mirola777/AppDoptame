package com.appdoptame.appdoptame.model;

public class User {
    private String ID;
    private String identification;
    private String name;
    private String phone;
    private String city;
    private String department;
    private String image;
    private String lastName;
    private long age;

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

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(String image) {
        this.image = image;
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
