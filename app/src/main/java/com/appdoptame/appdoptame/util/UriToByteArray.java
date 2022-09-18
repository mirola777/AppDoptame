package com.appdoptame.appdoptame.util;

import android.net.Uri;

import com.appdoptame.appdoptame.AppDoptameApp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UriToByteArray {
    public static byte[] convert(Uri uri) {
        try {
            InputStream iStream = AppDoptameApp.getContext().getContentResolver().openInputStream(uri);
            try {
                return convert(iStream);
            } catch (IOException ignored) {
                // close the stream
                iStream.close();
            }
        } catch (IOException ignored) {}
        return null;
    }

    public static byte[] convert(InputStream inputStream) throws IOException {

        byte[] bytesResult;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        } finally {
            // close the stream
            try{ byteBuffer.close(); } catch (IOException ignored){ /* do nothing */ }
        }
        return bytesResult;
    }
}
