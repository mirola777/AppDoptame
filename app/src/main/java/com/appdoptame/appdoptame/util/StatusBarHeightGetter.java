package com.appdoptame.appdoptame.util;

import com.appdoptame.appdoptame.AppDoptameApp;

/**
 * Esta clase se encarga de obtener la altura de la barra de notificaiones del dispositivo
 *
 * @author: Juan Manuel Muñoz
 */
public class StatusBarHeightGetter {
    /**
     * Método que se encarga del objetivo de la clase.
     * @return altura de la barra de notificaciones
     */
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
