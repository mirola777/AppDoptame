package com.appdoptame.appdoptame.util;

import com.google.android.gms.common.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLToByteArray {
    public static byte[] getByteImageFromURL(String url) {
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}