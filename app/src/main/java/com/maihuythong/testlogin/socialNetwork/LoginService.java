package com.maihuythong.testlogin.socialNetwork;

import com.maihuythong.testlogin.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {

    @FormUrlEncoded
    @POST("/user/login/by-facebook")
    Call<LoginResponse> isValidUser(
            @Field("accessToken")
                    String accessToken
    );
}