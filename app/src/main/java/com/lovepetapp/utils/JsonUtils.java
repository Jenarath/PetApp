package com.lovepetapp.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JsonUtils {

    public static String okhttpGET(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(7000, TimeUnit.MILLISECONDS)
                .writeTimeout(7000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

//    public static String okhttpPost(String url, RequestBody requestBody) {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .readTimeout(7000, TimeUnit.MILLISECONDS)
//                .writeTimeout(7000, TimeUnit.MILLISECONDS)
//                .build();
//        System.out.println("test " + requestBody);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
////            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//            System.out.println("test2 " + response.body().string());
//            return response.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "0";
//        }
//    }

    public static String okhttpPost(String url, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(7000, TimeUnit.MILLISECONDS)
                .writeTimeout(7000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}