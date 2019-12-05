package com.maihuythong.testlogin.signup;

import com.maihuythong.testlogin.ShowListUsers.SendInvationRes;
import com.maihuythong.testlogin.ShowListUsers.UserReq;
import com.maihuythong.testlogin.UserInfo.UserInfoRes;
import com.maihuythong.testlogin.invitationTour.ShowInvitationReq;
import com.maihuythong.testlogin.invitationTour.responseInvitation;
import com.maihuythong.testlogin.model.StopPointInfo;
import com.maihuythong.testlogin.model.StopPoints;
import com.maihuythong.testlogin.showAccountTours.ShowAccountToursReq;
import com.maihuythong.testlogin.showlist.ShowListReq;
import com.maihuythong.testlogin.updateTour.UpdateTourReq;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @POST("/user/register")
    @FormUrlEncoded
    Call<Post> signUp(@Field("email") String email,
                      @Field("password") String password,
                      @Field("phone") String phone);


    @POST("/tour/set-stop-points")
    @FormUrlEncoded
    Call<StopPoints> createStopPoints(@Header("Authorization") String s,
                                      @Field("tourId") String tourId,
                                      @Field("stopPoints") ArrayList<StopPointInfo> stopPoints);


    @GET("/tour/list")
    Call<ShowListReq> getList(@Header("Authorization") String s, @Query("rowPerPage") int rowPerPage, @Query("pageNum") int pageNum);

    @GET("/tour/history-user")
    Call<ShowAccountToursReq> getAccountTours(@Header("Authorization") String s, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("/tour/get/invitation")
    Call<ShowInvitationReq> getInvitation(@Header("Authorization") String s, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @POST("/tour/response/invitation")
    @FormUrlEncoded
    Call<responseInvitation> responseInvitation(@Header("Authorization") String s,
                                                @Field("tourId") long tourId,
                                                @Field("isAccepted") Boolean isAccepted
    );

    @POST("/tour/update-tour")
    @FormUrlEncoded
    Call<UpdateTourReq> updateTour(@Header("Authorization") String s,
                                   @Field("id") long id,
                                   @Field("name") String name,
                                   @Field("startDate") String startDate,
                                   @Field("endDate") String endDate,
//                                   @Field("sourceLat") String sourceLat,
//                                   @Field("sourceLong") String sourceLong,
//                                   @Field("desLong") String desLong,
//                                   @Field("desLat") String desLat,
                                   @Field("adults") long adults,
                                   @Field("childs") long childs,
                                   @Field("minCost") String minCost,
                                   @Field("maxCost") String maxCost,
                                   @Field("isPrivate") boolean isPrivate
//                                   @Field("avatar") String avatar,
//                                   @Field("status") String status
                                    );

    @GET("/user/search")
    Call<UserReq> getListUsers(
            @Query("searchKey")
                    String searchKey,
            @Query("pageIndex")
                    Number pageIndex,
            @Query("pageSize")
                    Number pageSize
    );


    @POST("/tour/add/member")
    @FormUrlEncoded
    Call<SendInvationRes> senInvation(
        @Header("Authorization") String s,
        @Field("tourId") long tourId,
        @Field("invitedUserId") String invitedUserId,
        @Field("isInvited") Boolean isInvited
    );


    @GET("/user/info")
    Call<UserInfoRes> getUserInfo(
            @Header("Authorization") String s
    );

}
