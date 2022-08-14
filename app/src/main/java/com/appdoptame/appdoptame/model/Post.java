package com.appdoptame.appdoptame.model;

import java.util.Date;
import java.util.List;

public class Post {
    private final Date date;
    private final String ID;
    private final List<String> likes;
    private final List<Comment> comments;
    private final User user;
    private final Pet pet;

    public Post(String ID, Date date, List<String> likes, List<Comment> comments, User user, Pet pet){
        this.date = date;
        this.likes = likes;
        this.user = user;
        this.pet = pet;
        this.ID = ID;
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getID() {
        return ID;
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
