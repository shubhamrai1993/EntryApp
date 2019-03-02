package com.example.entryapp;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHttpClient {
    private static final String BASE_DOMAIN = "http://13.127.61.109:8080";

    private static RetrofitPathService pathServiceInstance;

    private RetrofitHttpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .build();
        pathServiceInstance = retrofit.create(RetrofitPathService.class);
    }

    public static RetrofitPathService getPathServiceInstance() {
        if (pathServiceInstance == null) {
            new RetrofitHttpClient();
        }
        return pathServiceInstance;
    }
}
