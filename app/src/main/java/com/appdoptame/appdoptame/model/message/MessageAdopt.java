package com.appdoptame.appdoptame.model.message;

import com.appdoptame.appdoptame.model.Message;

import java.util.Date;

public class MessageAdopt extends Message {
    public MessageAdopt(String ID, String chatID, String writerID, Date date, String message) {
        super(ID, chatID, writerID, date, message);
    }

    public MessageAdopt(String writerID, String message) {
        super(writerID, message);
    }

    public MessageAdopt(String chatID, String writerID, String message) {
        super(chatID, writerID, message);
    }
}
