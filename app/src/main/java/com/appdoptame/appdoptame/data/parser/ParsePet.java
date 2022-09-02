package com.appdoptame.appdoptame.data.parser;

import com.appdoptame.appdoptame.model.Person;
import com.appdoptame.appdoptame.model.Pet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsePet {
    public static Pet parse(Map<String, Object> doc){
        long         age            = (long) doc.get("AGE");
        String       department     = (String) doc.get("DEPARTMENT");
        String       description    = (String) doc.get("DESCRIPTION");
        String       city           = (String) doc.get("CITY");
        String       name           = (String) doc.get("NAME");
        String       type           = (String) doc.get("TYPE");
        String       breed          = (String) doc.get("BREED");
        String       sex            = (String) doc.get("SEX");
        String       ID             = (String) doc.get("ID");
        boolean      stray          = (boolean) doc.get("STRAY");
        boolean      adopted        = (boolean) doc.get("ADOPTED");
        boolean      sterilized     = (boolean) doc.get("STERILIZED");
        long         size           = (long) doc.get("SIZE");
        long         weight         = (long) doc.get("WEIGHT");
        List<String> images         = (List<String>) doc.get("IMAGES");

        return new Pet(ID, name, type, sex, description, city, department, breed, stray, sterilized, adopted, age, size, weight, images);
    }

    public static Map<String, Object> parse(Pet pet) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("AGE",          pet.getAge());
        doc.put("DEPARTMENT",   pet.getDepartment());
        doc.put("DESCRIPTION",  pet.getDescription());
        doc.put("CITY",         pet.getCity());
        doc.put("NAME",         pet.getName());
        doc.put("TYPE",         pet.getType());
        doc.put("BREED",        pet.getBreed());
        doc.put("SEX",          pet.getSex());
        doc.put("ID",           pet.getID());
        doc.put("STRAY",        pet.isStray());
        doc.put("ADOPTED",      pet.isAdopted());
        doc.put("STERILIZED",   pet.isSterilized());
        doc.put("SIZE",         pet.getSize());
        doc.put("WEIGHT",       pet.getWeight());
        doc.put("IMAGES",       pet.getImages());

        return doc;
    }
}
