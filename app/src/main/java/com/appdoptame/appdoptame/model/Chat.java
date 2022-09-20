package com.appdoptame.appdoptame.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String        ID;
    private User          owner;
    private User          adopter;
    private Pet           pet;
    private List<Message> messages;

    public Chat(String ID, User owner, User adopter, Pet pet, List<Message> messages){
        this.ID       = ID;
        this.owner    = owner;
        this.adopter  = adopter;
        this.pet      = pet;
        this.messages = messages;
    }

    public Chat(User owner, User adopter, Pet pet){
        this(null, owner, adopter, pet, new ArrayList<>());
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getID() {
        return ID;
    }

    public Pet getPet() {
        return pet;
    }

    public User getAdopter() {
        return adopter;
    }

    public User getOwner() {
        return owner;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAdopter(User adopter) {
        this.adopter = adopter;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
