package com.appdoptame.appdoptame.util;

import com.appdoptame.appdoptame.model.Pet;

public class PetAgeGetter {
    public static String get(Pet pet){
        if(pet != null){
            if(pet.getAge() == 1){
                return pet.getAge() + " year old";
            }

            return pet.getAge() + " years old";
        }

        return "";
    }
}
