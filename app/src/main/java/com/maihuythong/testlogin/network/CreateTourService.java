package com.maihuythong.testlogin.network;

import com.maihuythong.testlogin.model.CreateTourResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CreateTourService {
    @FormUrlEncoded
    @POST("/tour/create")
    Call<CreateTourResponse> getTourInfo(
            @Header("Authorization")
                    String token,
            @Field("name")
                    String tourName,
            @Field("startDate")
                    Number startDate,
            @Field("endDate")
                    Number endDate,
            @Field("sourceLat")
                    Number sourceLat,
            @Field("sourceLong")
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
  /* @POST("/tour/create")
    Call<CreateTourResponse> getList(@Header("Authorization") String token, @Query("name") String tourName, @Query("startDate") Number startDate,
                                     @Query("endDate") Number endDate,@Query("sourceLat") Number srcLat,@Query("sourceLong") Number srcLong,
                                     @Query("desLat") Number desLat,@Query("desLong") Number desLong,@Query("isPrivate") Boolean isPrivate);*/
}
