package com.maihuythong.testlogin.network;

import com.maihuythong.testlogin.model.CreateTourResponse;
import com.maihuythong.testlogin.model.LoginResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Query;

public interface RetrofitServices {
    @FormUrlEncoded
    @POST("/user/login")
    Call<LoginResponse> isValidUser(
            @Field("emailPhone")
                    String emailPhone,
            @Field("password")
                    String password
    );
}
