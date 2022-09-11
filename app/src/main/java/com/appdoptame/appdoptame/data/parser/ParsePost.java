package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.Comment;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsePost {
    private static final String LIKES    = "LIKES";
    private static final String DATE     = "DATE";
    private static final String POST_ID  = "ID";
    private static final String PET      = "PET";
    private static final String USER     = "PERSON";
    private static final String COMMENTS = "COMMENTS";

    public static Post parse(Map<String, Object> doc){

        List<String> likes     = (List<String>) doc.get(LIKES);
        List<Comment> comments = new ArrayList<>();
        Date         date      = ((Timestamp) doc.get(DATE)).toDate();
        String       ID        = (String) doc.get(POST_ID);
        Pet          pet       = ParsePet.parse((Map<String, Object>) doc.get(PET));
        User         user      = ParseUser.parse((Map<String, Object>) doc.get(USER));

        return new Post(ID, date, likes, comments, user, pet);
    }

    public static Map<String, Object> parse(Post post){
        Map<String, Object> doc = new HashMap<>();
        doc.put(POST_ID,      post.getID());
        doc.put(DATE,         post.getDate());
        doc.put(LIKES,        post.getLikes());
        doc.put(COMMENTS,     post.getComments());
        doc.put(PET,          ParsePet.parse(post.getPet()));
        doc.put(USER,         ParseUser.parse(post.getUser()));

        return doc;
    }
}
