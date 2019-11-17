package com.maihuythong.testlogin.signup;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.model.StopPointInfo;
import com.maihuythong.testlogin.model.StopPoints;
import com.maihuythong.testlogin.model.StopPoints;
import com.maihuythong.testlogin.showlist.ShowListReq;
import com.maihuythong.testlogin.showlist.ShowListRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
//    @POST("/posts")
//    @FormUrlEncoded
//    Call<Post> savePost(@Field("title") String title,
//                        @Field("body") String body,
//                        @Field("userId") long userId);

    @POST("/user/register")
    @FormUrlEncoded
    Call<Post> signUp(@Field("email") String email,
                      @Field("password") String password,
                      @Field("phone") String phone);


    @POST("/tour/set-stop-points")
    @FormUrlEncoded
    Call<StopPoints> createStopPoints(@Field("tourId") String tourId,
                                      @Field("stopPoints") ArrayList<StopPointInfo> stopPoints);


    @GET("/tour/list")
    Call<ShowListReq> getList(@Header("Authorization") String s, @Query("rowPerPage") int rowPerPage, @Query("pageNum") int pageNum);
}
