package com.appdoptame.appdoptame.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Comment {
    private final Date   date;
    private final User   user;
    private final String comment;
    private final String postID;

    public Comment(String postID, Date date, User user, String comment){
        this.comment  = comment;
        this.date     = date;
        this.postID   = postID;
        this.user     = user;
    }

    public Comment(String postID, User user, String comment){
        this(postID, Calendar.getInstance().getTime(), user, comment);
    }

    public String getPostID() {
        return postID;
    }

    public Date getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }
}
