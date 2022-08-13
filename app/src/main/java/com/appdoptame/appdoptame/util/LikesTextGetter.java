package com.appdoptame.appdoptame.util;

import android.annotation.SuppressLint;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;

public class LikesTextGetter {
    @SuppressLint("StringFormatMatches")
    public static String getDateText(int likes){
        if(likes != 1){
            return String.format(
                    AppDoptameApp.getContext().getString(R.string.likes),
                    likes);
        } else {
            return likes + " " + AppDoptameApp.getContext().getString(R.string.like);
        }
    }
}
