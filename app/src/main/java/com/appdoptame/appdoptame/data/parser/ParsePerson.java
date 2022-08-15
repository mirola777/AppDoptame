package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.Person;
import com.appdoptame.appdoptame.model.User;

import java.util.HashMap;
import java.util.Map;

public class ParsePerson {
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

        return new Person(ID, identification, name, lastName, phone, city, department, image, age);
    }

    public static Map<String, Object> parse(Person person){
        Map<String, Object> doc  = new HashMap<>();
        doc.put("AGE",        person.getAge());
        doc.put("DEPARTMENT", person.getDepartment());
        doc.put("PHONE",      person.getPhone());
        doc.put("CITY",       person.getCity());
        doc.put("NAME",       person.getName());
        doc.put("IMAGE",      person.getImage());
        doc.put("LAST_NAME",  person.getLastName());
        doc.put("CC",         person.getIdentification());
        doc.put("ID",         person.getID());

        return doc;
    }
}
