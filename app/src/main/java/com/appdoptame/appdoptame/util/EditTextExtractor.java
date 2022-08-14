package com.appdoptame.appdoptame.util;

import android.widget.EditText;

public class EditTextExtractor {
    public static String get(EditText editText){
        if(editText != null){
            String text = editText.getText().toString();
            if(text.length() == 0)
                return "";
            else
                return text.trim();
        }
        return "";
    }
}
