package com.appdoptame.appdoptame.model.notification;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.model.Post;

import java.util.Date;

public class NotificationNewPost extends Notification {
    private final Post post;

    public NotificationNewPost(Post post){
        this.post = post;
    }

    @Override
    public void onClicked() {

    }

    @Override
    public String getBody() {
        return
                post.getUser().getName() + " " +
                AppDoptameApp.getStringById(R.string.notification_new_post_1) + " " +
                post.getPet().getName() + AppDoptameApp.getStringById(R.string.notification_new_post_2);
    }

    @Override
    public String getImage() {
        return post.getPet().getImages().get(0);
    }

    @Override
    public Date getDate() {
        return post.getDate();
    }
}
