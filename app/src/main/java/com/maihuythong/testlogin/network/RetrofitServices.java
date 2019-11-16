package com.maihuythong.testlogin.network;

import com.maihuythong.testlogin.model.LoginResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.Call;

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

interface CreateTourServices{
    @POST("/tour/create")
    Call<LoginResponse> isValidUser(
            @Field("name")
                    String tourName,
            @Field("startDate")
                    Number startDate,
            @Field("endDate")
                    Number endDate,
            @Field("sourceLat")
                    Number sourceLat,
            @Field("endDate")
                    Number sourceLong,
            @Field("desLat")
                    Number desLat,
            @Field("desLong")
                    Number desLong,
            @Field("isPrivate")
                    Boolean isPrivate,
            @Field("adults")
                    Number adults,
            @Field("childs")
                    Number childs,
            @Field("minCost")
                    Number minCost,
            @Field("maxCost")
                    Number maxCost,
            @Field("avatar")
                    String avatar

    );


}

