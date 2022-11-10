package com.appdoptame.appdoptame.model.message;

import com.appdoptame.appdoptame.model.Message;

import java.util.Date;

public class MessageFile extends Message {
    private String filename;
    private String fileURL;

    public MessageFile(String ID, String chatID, String writerID, Date date, String message, String filename, String fileURL) {
        super(ID, chatID, writerID, date, message);

        this.filename = filename;
        this.fileURL  = fileURL;
    }

    public MessageFile(String writerID, String message) {
        super(writerID, message);

        this.filename = filename;
        this.fileURL  = fileURL;
    }

    public MessageFile(String chatID, String writerID, String message) {
        super(chatID, writerID, message);

        this.filename = filename;
        this.fileURL  = fileURL;
    }

    public String getFilename() {
        return filename;
    }

    public String getFileURL() {
        return fileURL;
    }
}
