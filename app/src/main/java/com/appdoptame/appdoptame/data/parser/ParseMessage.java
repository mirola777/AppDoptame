package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.util.MessageConstants;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseMessage {
    private static final String CHAT_ID   = "CHAT_ID";
    private static final String WRITER_ID = "WRITER_ID";
    private static final String DATE      = "DATE";
    private static final String MESSAGE   = "MESSAGE";
    private static final String TYPE      = "TYPE";

    public static List<Message> parseDocList(List<Map<String, Object>> docs){
        List<Message> messages = new ArrayList<>();

        for(Map<String, Object> doc: docs){
            if(doc.get(TYPE) != null){
                if(doc.get(TYPE).equals(MessageConstants.NORMAL)) messages.add(parse(doc));
            } else {
                messages.add(parse(doc));
            }
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
        String message  = (String) doc.get(MESSAGE);
        String writerID = (String) doc.get(WRITER_ID);

        return new Message(chatID, writerID, date, message);
    }

    public static Map<String, Object> parse(Message message){
        Map<String, Object> doc  = new HashMap<>();
        doc.put(CHAT_ID,    message.getChatID());
        doc.put(WRITER_ID,  message.getWriterID());
        doc.put(DATE,       message.getDate());
        doc.put(MESSAGE,    message.getMessage());
        doc.put(TYPE,       MessageConstants.NORMAL);

        return doc;
    }

    public static Map<String, Object> parseAdopt(Message message){
        Map<String, Object> doc  = new HashMap<>();
        doc.put(CHAT_ID,    message.getChatID());
        doc.put(WRITER_ID,  message.getWriterID());
        doc.put(DATE,       message.getDate());
        doc.put(MESSAGE,    message.getMessage());
        doc.put(TYPE,       MessageConstants.ADOPT);

        return doc;
    }
}
