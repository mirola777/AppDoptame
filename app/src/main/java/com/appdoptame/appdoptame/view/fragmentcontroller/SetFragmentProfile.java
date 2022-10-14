package com.appdoptame.appdoptame.view.fragmentcontroller;

import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.view.fragment.FragmentProfile;

public class SetFragmentProfile {
    public static void set(User user){
        FragmentProfile fragment = new FragmentProfile(user);
        FragmentController.addFragmentFade(fragment);
    }
}
