package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.User;

import java.util.HashMap;
import java.util.Map;

public class ParseUser {
    public static User parse(Map<String, Object> doc){
        long   age            = (long) doc.get("AGE");
        String department     = (String) doc.get("DEPARTMENT");
        String phone          = (String) doc.get("PHONE");
        String city           = (String) doc.get("CITY");
        String name           = (String) doc.get("NAME");
        String image          = (String) doc.get("IMAGE");
        String lastName       = (String) doc.get("LAST_NAME");
        String identification = (String) doc.get("CC");
        String ID             = (String) doc.get("ID");

        return new User(ID, identification, name, lastName, phone, city, department, image, age);
    }

    public static Map<String, Object> parse(User user){
        Map<String, Object> doc  = new HashMap<>();
        doc.put("AGE",        user.getAge());
        doc.put("DEPARTMENT", user.getDepartment());
        doc.put("PHONE",      user.getPhone());
        doc.put("CITY",       user.getCity());
        doc.put("NAME",       user.getName());
        doc.put("IMAGE",      user.getImage());
        doc.put("LAST_NAME",  user.getLastName());
        doc.put("CC",         user.getIdentification());
        doc.put("ID",         user.getID());

        return doc;
    }
}
