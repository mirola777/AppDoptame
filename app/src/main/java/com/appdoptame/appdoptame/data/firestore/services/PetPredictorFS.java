package com.appdoptame.appdoptame.data.firestore.services;

import android.os.Handler;
import android.os.Looper;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.PetPredictorListener;
import com.appdoptame.appdoptame.data.service.IPetPredictor;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.util.EntityUtils;

public class PetPredictorFS implements IPetPredictor {
    public static Header[] browserHeaders;
    private final static String userAgent                 = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    private final static String userAgentKey              = "User-Agent";
    private final static String UrlApi                    = "http://www.appdoptamepredict.tk";
    private final static String IPApi                     = "http://34.136.55.35";
    private final static String UrlPredictRace            = "/dograce";
    private final static String UrlUrlParam               = "?url=";

    private final static StorageReference storageTemporal = FirestoreDB.getStorageTemporal();

    private static HttpClient httpClient;

    @Override
    public void predictRace(byte[] image, PetPredictorListener listener) {
        String randomID = UUID.randomUUID().toString();
        StorageReference referenceImage = storageTemporal.child(randomID);
        UploadTask uploadTask = referenceImage.putBytes(image);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
            return referenceImage.getDownloadUrl();

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String imageUrl = task.getResult().toString();

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    init();

                    String predictResponse = null;
                    try {
                        predictResponse = get(
                                UrlApi + UrlPredictRace + UrlUrlParam + URLEncoder.encode(imageUrl, "UTF-8"),
                                Arrays.asList(browserHeaders));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    System.out.println(predictResponse);

                    Handler handler = new Handler(Looper.getMainLooper());
                    Looper.prepare();
                    String finalPredictResponse = predictResponse;
                    handler.post(() -> {
                        if(finalPredictResponse != null){
                            listener.onSuccess(finalPredictResponse);
                        } else {
                            listener.onFailure();
                        }
                    });

                    referenceImage.delete();
                });
            } else {
                listener.onFailure();
            }
        });
    }

    private void init(){
        // Creating browser headers
        List<Header> browserHeadersList = new ArrayList<>();
        browserHeadersList.add(new BasicHeader("User-Agent", userAgent));
        browserHeadersList.add(new BasicHeader("Content-Language", "en-US"));
        browserHeadersList.add(new BasicHeader("Cache-Control", "max-age=0"));
        browserHeadersList.add(new BasicHeader("Accept", "*/*"));
        browserHeadersList.add(new BasicHeader("Accept-Charset", "utf-8,ISO-8859-1;q=0.7,*;q=0.3"));
        browserHeadersList.add(new BasicHeader("Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4"));
        browserHeadersList.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"));
        browserHeaders = new Header[browserHeadersList.size()];
        browserHeaders = browserHeadersList.toArray(browserHeaders);

        // Creating the HttpClient
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClient = httpClientBuilder.build();
    }

    public static String get(String url, List<Header> headers) {

        String responseContent = null;
        HttpEntity httpEntity = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(userAgentKey, userAgent);
            if (headers != null) {
                Header[] headersArr = new Header[headers.size()];
                httpGet.setHeaders(headers.toArray(headersArr));
            }

            HttpResponse httpResponse = httpClient.execute(httpGet);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            httpEntity = httpResponse.getEntity();
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode != 200) {
                throw new RuntimeException("status code: " + statusLine.getStatusCode());
            }

            httpEntity.writeTo(baos);
            responseContent = baos.toString("UTF-8");

            JSONObject json = new JSONObject(responseContent);
            return json.getString("race");

        } catch (Exception var20) {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException var19) {
                var19.printStackTrace();
            }
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException var18) {
                var18.printStackTrace();
            }

        }

        return responseContent;
    }
}
