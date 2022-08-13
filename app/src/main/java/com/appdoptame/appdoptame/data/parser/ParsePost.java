package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ParsePost {
    public static Post parse(Map<String, Object> doc){
        List<String> likes  = (List<String>) doc.get("LIKES");
        Date         date   = ((Timestamp) doc.get("DATE")).toDate();
        String       ID     = (String) doc.get("ID");
        Pet          pet    = ParsePet.parse((Map<String, Object>) doc.get("PET"));
        User         person = ParsePerson.parse((Map<String, Object>) doc.get("PERSON"));

        return new Post(ID, date, likes, person, pet);
    }
}
