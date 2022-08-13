package com.appdoptame.appdoptame.model;

public class Pet {
    private final String ID;
    private final String name;
    private final String type;
    private final String sex;
    private final String description;
    private final String city;
    private final String department;
    private final String breed;
    private final boolean stray;
    private final boolean sterilized;
    private final boolean adopted;
    private final int age;
    private final int size;
    private final int weight;

    public Pet(String ID,
               String name,
               String type,
               String sex,
               String description,
               String city,
               String department,
               String breed,
               boolean stray,
               boolean sterilized,
               boolean adopted,
               int age,
               int size,
               int weight){
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.sex = sex;
        this.description = description;
        this.city = city;
        this.department = department;
        this.breed = breed;
        this.stray = stray;
        this.sterilized = sterilized;
        this.adopted = adopted;
        this.age = age;
        this.size = size;
        this.weight = weight;
    }

    public boolean isAdopted() {
        return adopted;
    }

    public boolean isSterilized() {
        return sterilized;
    }

    public boolean isStray() {
        return stray;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getID() {
        return ID;
    }

    public int getAge() {
        return age;
    }

    public int getSize() {
        return size;
    }

    public int getWeight() {
        return weight;
    }

    public String getBreed() {
        return breed;
    }

    public String getCity() {
        return city;
    }

    public String getDepartment() {
        return department;
    }

    public String getSex() {
        return sex;
    }

    public String getType() {
        return type;
    }
}
