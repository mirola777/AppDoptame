package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.Comment;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.MessageConstants;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseComment {
    private static final String COMMENT  = "COMMENT";
    private static final String DATE     = "DATE";
    private static final String USER     = "USER";

    public static Comment parse(Map<String, Object> doc){

        Date   date    = ((Timestamp) doc.get(DATE)).toDate();
        String comment = (String) doc.get(COMMENT);
        User   user    = ParseUser.parse((Map<String, Object>) doc.get(USER));

        return new Comment(null, date, user, comment);
    }

    public static Map<String, Object> parse(Comment comment){
        Map<String, Object> doc = new HashMap<>();
        doc.put(DATE,         comment.getDate());
        doc.put(COMMENT,      comment.getComment());
        doc.put(USER,         ParseUser.parse(comment.getUser()));

        return doc;
    }

    public static List<Comment> parseDocList(List<Map<String, Object>> docs){
        List<Comment> comments = new ArrayList<>();

        for(Map<String, Object> doc: docs){
            comments.add(parse(doc));
        }

        return comments;
    }
}
