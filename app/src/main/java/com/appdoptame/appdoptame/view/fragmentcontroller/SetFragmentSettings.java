package com.appdoptame.appdoptame.view.fragmentcontroller;

import com.appdoptame.appdoptame.view.fragment.FragmentSettings;

public class SetFragmentSettings {
    public static void set(){
        FragmentSettings fragment = new FragmentSettings();
        FragmentController.addFragmentFade(fragment);
    }
}
