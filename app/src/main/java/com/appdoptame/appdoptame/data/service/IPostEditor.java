package com.appdoptame.appdoptame.data.service;

import android.net.Uri;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Post;

import java.util.List;

public interface IPostEditor {
    void updatePost(Post post, List<byte[]> postImages, CompleteListener listener);
}
