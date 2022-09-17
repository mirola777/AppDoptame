package com.appdoptame.appdoptame.util;

public abstract class Selectable {
    private final int drawableResourceID;
    private final int stringResourceID;

    public Selectable(int drawableResourceID, int stringResourceID){
        this.drawableResourceID = drawableResourceID;
        this.stringResourceID   = stringResourceID;
    }

    public int getDrawableID() {
        return drawableResourceID;
    }

    public int getStringID() {
        return stringResourceID;
    }

    public abstract void onClick();
}
