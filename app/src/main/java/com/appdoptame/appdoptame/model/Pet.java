package com.appdoptame.appdoptame.model;

import java.util.List;

public class Pet {
    private String ID;
    private String name;
    private String type;
    private String sex;
    private String description;
    private String city;
    private String department;
    private String breed;
    private boolean stray;
    private boolean sterilized;
    private boolean adopted;
    private long age;
    private long size;
    private long weight;
    private List<String> images;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setSterilized(boolean sterilized) {
        this.sterilized = sterilized;
    }

    public void setStray(boolean stray) {
        this.stray = stray;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(long weight) {
        this.weight = weight;
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
