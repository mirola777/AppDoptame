package com.appdoptame.appdoptame.model;

import java.util.Date;
import java.util.List;

public class Comment {
    private final String ID;
    private final Date date;
    private final String userName;
    private final String userID;
    private final String comment;

    public Comment(String ID, Date date, String userName, String userID, String comment){
        this.comment  = comment;
        this.date     = date;
        this.ID       = ID;
        this.userID   = userID;
        this.userName = userName;
    }

    public String getID() {
        return ID;
    }

    public Date getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }
}
