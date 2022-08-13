package com.appdoptame.appdoptame.view.fragmentcontroller;

import com.appdoptame.appdoptame.view.fragment.FragmentLogin;

public class SetFragmentLogin {
    public static void set(){
        FragmentLogin fragment = new FragmentLogin();
        FragmentController.addFragmentFade(fragment);
    }
}
