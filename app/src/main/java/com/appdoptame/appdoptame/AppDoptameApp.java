package com.appdoptame.appdoptame;

import android.app.Application;
import android.content.Context;

public class AppDoptameApp  extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getContext(){
        return appContext;
    }

    public static int getColorById(int id){
        return appContext.getResources().getColor(id);
    }

    public static String getStringById(int id){
        return appContext.getString(id);
    }
}