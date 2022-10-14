package com.appdoptame.appdoptame.model.notification;

import java.util.Date;

public abstract class Notification {
    public abstract void   onClicked();
    public abstract String getBody();
    public abstract String getImage();
    public abstract Date   getDate();
}
