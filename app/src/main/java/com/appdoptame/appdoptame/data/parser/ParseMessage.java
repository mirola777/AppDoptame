package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.message.MessageAdopt;
import com.appdoptame.appdoptame.model.message.MessageFile;
import com.appdoptame.appdoptame.model.message.MessageImage;
import com.appdoptame.appdoptame.util.MessageConstants;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ParseMessage {
    private static final String CHAT_ID    = "CHAT_ID";
    private static final String MESSAGE_ID = "ID";
    private static final String WRITER_ID  = "WRITER_ID";
    private static final String DATE       = "DATE";
    private static final String MESSAGE    = "MESSAGE";
    private static final String TYPE       = "TYPE";
    private static final String IMAGE      = "IMAGE";
    private static final String FILENAME   = "FILENAME";
    private static final String FILEURL    = "FILEURL";

    public static List<Message> parseDocList(List<Map<String, Object>> docs){
        List<Message> messages = new ArrayList<>();

        for(Map<String, Object> doc: docs){
            messages.add(parse(doc));
        }

        return messages;
    }

    public static List<Map<String, Object>> parseMessageList(List<Message> messages){
        List<Map<String, Object>> docs = new ArrayList<>();

        for(Message message: messages){
            docs.add(parse(message));
        }

        return docs;
    }


    public static Message parse(Map<String, Object> doc){
        Date   date     = ((Timestamp) doc.get(DATE)).toDate();
        String chatID   = (String) doc.get(CHAT_ID);
        String ID       = (String) doc.get(MESSAGE_ID);
        String message  = (String) doc.get(MESSAGE);
        String writerID = (String) doc.get(WRITER_ID);
        String filename = (String) doc.get(FILENAME);
        String fileURL  = (String) doc.get(FILEURL);

        if(ID == null) ID = UUID.randomUUID().toString();

        if(doc.get(TYPE) != null){
            switch ((String) doc.get(TYPE)){
                case MessageConstants.IMAGE:
                    String image = (String) doc.get(IMAGE);
                    return new MessageImage(ID, chatID, writerID, date, message, image);

                case MessageConstants.ADOPT:
                    return new MessageAdopt(ID, chatID, writerID, date, message);

                case MessageConstants.FILE:
                    return new MessageFile(ID, chatID, writerID, date, message, filename, fileURL);

                default:
                    return new Message(ID, chatID, writerID, date, message);

            }
        } else {
            return new Message(ID, chatID, writerID, date, message);
        }
    }

    public static Map<String, Object> parse(Message message){
        Map<String, Object> doc  = new HashMap<>();
        doc.put(CHAT_ID,    message.getChatID());
        doc.put(WRITER_ID,  message.getWriterID());
        doc.put(DATE,       message.getDate());
        doc.put(MESSAGE,    message.getMessage());
        doc.put(MESSAGE_ID, message.getID());

        if(message instanceof MessageFile){
            doc.put(TYPE, MessageConstants.FILE);
            doc.put(FILENAME, ((MessageFile) message).getFilename());
            doc.put(FILEURL, ((MessageFile) message).getFileURL());

        } else if(message instanceof MessageImage){
            doc.put(TYPE,  MessageConstants.IMAGE);
            doc.put(IMAGE, ((MessageImage) message).getImage());

        } else if(message instanceof MessageAdopt){
            doc.put(TYPE, MessageConstants.ADOPT);

        } else {
            doc.put(TYPE, MessageConstants.NORMAL);

        }

        return doc;
    }

    public static Map<String, Object> parseAdopt(Message message){
        Map<String, Object> doc  = new HashMap<>();
        doc.put(CHAT_ID,    message.getChatID());
        doc.put(WRITER_ID,  message.getWriterID());
        doc.put(DATE,       message.getDate());
        doc.put(MESSAGE,    message.getMessage());
        doc.put(MESSAGE_ID, message.getID());
        doc.put(TYPE,       MessageConstants.ADOPT);

        return doc;
    }
}
