package com.appdoptame.appdoptame.model;

import java.util.Calendar;
import java.util.Date;

public class Message {
    private String ID;
    private Date   date;
    private String chatID;
    private String writerID;
    private String message;

    public Message(String ID, String chatID, String writerID, Date date, String message){
        this.chatID   = chatID;
        this.date     = date;
        this.writerID = writerID;
        this.message  = message;
        this.ID       = ID;
    }

    public Message(String writerID, String message){
        this(null, null, writerID, Calendar.getInstance().getTime(), message);
    }

    public Message(String chatID, String writerID, String message){
        this(null, chatID, writerID, Calendar.getInstance().getTime(), message);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getWriterID() {
        return writerID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWriterID(String writerID) {
        this.writerID = writerID;
    }
}
