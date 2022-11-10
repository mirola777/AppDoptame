package com.appdoptame.appdoptame.model.message;

import com.appdoptame.appdoptame.model.Message;

import java.util.Date;

public class MessageImage extends Message {
    private String image;

    public MessageImage(String ID,String chatID, String writerID, Date date, String message, String image) {
        super(ID, chatID, writerID, date, message);
        this.image = image;
    }

    public MessageImage(String writerID, String message, String image) {
        super(writerID, message);
        this.image = image;
    }

    public MessageImage(String chatID, String writerID, String message, String image) {
        super(chatID, writerID, message);
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
