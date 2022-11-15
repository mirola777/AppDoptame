package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParsePost;
import com.appdoptame.appdoptame.data.service.IPetRecommend;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

public class PetRecommendFS implements IPetRecommend {
    public static Header[] browserHeaders;
    private final static String userAgent                 = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    private final static String userAgentKey              = "User-Agent";
    private final static String UrlApi                    = "http://www.appdoptame.tk";
    private final static String IPApi                     = "http://34.134.244.86";
    private final static String UrlRecommend              = "/recommendation";
    private final static String UrlUserIDParam            = "?user_id=";

    private final static CollectionReference collectionPost = FirestoreDB.getCollectionPost();

    private static HttpClient httpClient;

    @Override
    public void recommend(PostLoaderListener listener) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            init();

            String recommendResponse = null;
            User user = UserRepositoryFS.getInstance().getUserSession();
            recommendResponse = get(IPApi + UrlRecommend + UrlUserIDParam + user.getID(), Arrays.asList(browserHeaders));
            if(recommendResponse != null){
                collectionPost.whereEqualTo("PET.BREED", recommendResponse).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            if(document.getData() != null){
                                Post post = ParsePost.parse(document.getData());
                                listener.onSuccess(post);
                                break;
                            }
                        }
                    } else {
                        listener.onFailure();
                    }
                });
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
            if(json.names().length() > 0){
                int    i           = 0;
                double score       = 0;
                String highestRace = null;
                while (i < json.names().length()){
                    String race = (String) json.names().get(i);

                    if(json.getDouble(race) > score) {
                        score       = json.getDouble(race);
                        highestRace = race;
                    }
                    i++;
                }

                return highestRace;
            } else {
                return null;
            }

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
