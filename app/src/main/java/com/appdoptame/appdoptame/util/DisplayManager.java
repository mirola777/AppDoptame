package com.appdoptame.appdoptame.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;

import com.appdoptame.appdoptame.AppDoptameApp;

public class DisplayManager {
    public static int getScreenHeight(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        int height;

        if (!hasNavBar(activity.getResources())) {
            height = display.getHeight();
        } else {
            try {
                display.getRealSize(point);
                height = point.y;
            } catch (Exception e) {
                height = display.getHeight();
            }
        }

        return height - getStatusBarHeight();
    }

    public static int getStatusBarHeight(){
        int statusBarHeight = 0;
        int resourceId = AppDoptameApp.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = AppDoptameApp.getContext().getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

    private static boolean hasNavBar (Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }
}