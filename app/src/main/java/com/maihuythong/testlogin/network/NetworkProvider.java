package com.maihuythong.testlogin.network;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkProvider {

    private static volatile NetworkProvider mInstance = null;
    private Retrofit retrofit;
    private NetworkProvider() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://35.197.153.192:3000/")
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
    }
    public static NetworkProvider self() {
        if (mInstance == null)
            mInstance = new NetworkProvider();
        return mInstance;
    }
}

