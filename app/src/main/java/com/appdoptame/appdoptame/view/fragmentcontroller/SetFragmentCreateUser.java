package com.appdoptame.appdoptame.view.fragmentcontroller;

import com.appdoptame.appdoptame.view.fragment.FragmentCreateUser;

public class SetFragmentCreateUser {
    public static void set(){
        FragmentCreateUser fragment = new FragmentCreateUser();
        FragmentController.addFragmentFade(fragment);
    }
}
