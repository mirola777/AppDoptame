package com.appdoptame.appdoptame.model;

public class Chat {
    private String        ID;
    private User          owner;
    private User          adopter;
    private Pet           pet;
    private Message       lastMessage;

    public Chat(String ID, User owner, User adopter, Pet pet, Message lastMessage){
        this.ID       = ID;
        this.owner    = owner;
        this.adopter  = adopter;
        this.pet      = pet;
        this.lastMessage = lastMessage;
    }

    public Chat(User owner, User adopter, Pet pet){
        this(null, owner, adopter, pet, null);
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
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
