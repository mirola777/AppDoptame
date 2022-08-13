package com.appdoptame.appdoptame.view.fragmentcontroller;

import com.appdoptame.appdoptame.view.fragment.FragmentMain;

public class SetFragmentMain {
    public static void set(){
        FragmentMain fragment = new FragmentMain();
        FragmentController.addFragmentFade(fragment);
    }
}