package com.appdoptame.appdoptame.view.fragmentcontroller;

import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.view.fragment.FragmentComment;

public class SetFragmentComment {
    public static void set(Post post){
        FragmentComment fragment = new FragmentComment(post);
        FragmentController.addFragmentFade(fragment);
    }
}
