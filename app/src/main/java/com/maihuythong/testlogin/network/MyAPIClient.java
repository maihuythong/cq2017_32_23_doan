package com.maihuythong.testlogin.network;

import android.text.TextUtils;

import com.maihuythong.testlogin.manager.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tpl on 12/13/16.
 */

public class MyAPIClient {
    private static MyAPIClient instance;

    private Retrofit adapter;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;

    private MyAPIClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (!TextUtils.isEmpty(accessToken)) {
                            request = request.newBuilder()
                                    .addHeader("Authorization", "Bearer " + accessToken)
                                    .build();
                        }
                        Response response = chain.proceed(request);
                        return response;
                    }
                })
                .build();

        adapter = new Retrofit.Builder()
                .baseUrl(Constants.APIEndpoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getAdapter() {
        return adapter;
    }

    public static MyAPIClient getInstance() {
        if (instance == null)
            instance = new MyAPIClient();
        return instance;
    }
}
