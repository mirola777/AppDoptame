package com.appdoptame.appdoptame.model;

import java.util.List;

public class Pet {
    private String ID;
    private final String name;
    private final String type;
    private final String sex;
    private final String description;
    private final String city;
    private final String department;
    private final String breed;
    private final boolean stray;
    private final boolean sterilized;
    private boolean adopted;
    private final long age;
    private final long size;
    private final long weight;
    private final List<String> images;

    public Pet(
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
               long age,
               long size,
               long weight,
               List<String> images){

        this(null, name, type, sex, description, city, department, breed, stray, sterilized, adopted, age, size, weight, images);
    }

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
               long age,
               long size,
               long weight,
               List<String> images){
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
        this.age    = age;
        this.size   = size;
        this.weight = weight;
        this.images = images;
    }

    public void setAdopted(boolean adopted) {
        this.adopted = adopted;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<String> getImages() {
        return images;
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

    public long getAge() {
        return age;
    }

    public long getSize() {
        return size;
    }

    public long getWeight() {
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
