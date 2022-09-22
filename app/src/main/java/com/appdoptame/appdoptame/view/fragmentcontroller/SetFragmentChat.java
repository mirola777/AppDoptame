package com.appdoptame.appdoptame.view.fragmentcontroller;

import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.view.fragment.FragmentChat;

public class SetFragmentChat {
    public static void set(Chat chat){
        FragmentChat fragment = new FragmentChat(chat);
        FragmentController.addFragmentFade(fragment);
    }
}
