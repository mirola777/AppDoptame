package com.appdoptame.appdoptame.util;

import com.appdoptame.appdoptame.AppDoptameApp;

public class StatusBarHeightGetter {
    public static int get(){
        int statusBarHeight = 0;
        int resourceId = AppDoptameApp.getContext().getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight =
                    AppDoptameApp.getContext().getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }
}
