package com.appdoptame.appdoptame.model;

import java.util.Date;
import java.util.List;

public class Post {
    private final Date date;
    private final List<String> likes;
    private final User user;
    private final Pet pet;

    public Post(Date date, List<String> likes, User user, Pet pet){
        this.date = date;
        this.likes = likes;
        this.user = user;
        this.pet = pet;
    }

    public Date getDate() {
        return date;
    }

    public List<String> getLikes() {
        return likes;
    }

    public Pet getPet() {
        return pet;
    }

    public User getUser() {
        return user;
    }
}
