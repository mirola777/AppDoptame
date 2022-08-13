package com.appdoptame.appdoptame.util;

import android.annotation.SuppressLint;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;

/**
 * Esta clase se encarga de convertir la cantidad de likes (muy util para los Posts)
 * al siguiente formato de texto:
 *      - 0 me gusta (0 likes)
 *      - 1 me gusta (1 like)
 *      - 2 me gusta (2 likes)
 *
 * @author: Juan Manuel Muñoz
 */
public class LikesTextGetter {
    /**
     * Método que se encarga del objetivo de la clase.
     * @param likes cantidad de likes
     * @return conversión de like a texto
     */
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
