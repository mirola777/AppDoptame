package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseChat {
    private static final String CHAT_ID  = "ID";
    private static final String OWNER    = "OWNER";
    private static final String ADOPTER  = "ADOPTER";
    private static final String PET      = "PET";
    private static final String MESSAGES = "MESSAGES";

    public static Chat parse(Map<String, Object> doc){
        User   owner           = ParseUser.parse((Map<String, Object>) doc.get(OWNER));
        User   adopter         = ParseUser.parse((Map<String, Object>) doc.get(ADOPTER));
        Pet    pet             = ParsePet.parse((Map<String, Object>) doc.get(PET));
        String ID              = (String) doc.get(CHAT_ID);
        List<Message> messages = ParseMessage.parseDocList((List<Map<String, Object>>) doc.get(MESSAGES));

        return new Chat(ID, owner, adopter, pet, messages);
    }

    public static Map<String, Object> parse(Chat chat){
        Map<String, Object> doc  = new HashMap<>();
        doc.put(CHAT_ID,       chat.getID());
        doc.put(PET,           ParsePet.parse(chat.getPet()));
        doc.put(OWNER,         ParseUser.parse(chat.getOwner()));
        doc.put(ADOPTER,       ParseUser.parse(chat.getAdopter()));
        doc.put(MESSAGES,      ParseMessage.parseMessageList(chat.getMessages()));

        return doc;
    }
}
