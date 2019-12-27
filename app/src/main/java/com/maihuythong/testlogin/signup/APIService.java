package com.maihuythong.testlogin.signup;

import com.maihuythong.testlogin.ShowListUsers.SendInvationRes;
import com.maihuythong.testlogin.ShowListUsers.UserReq;
import com.maihuythong.testlogin.TourCoordinate.GetNotificationList;
import com.maihuythong.testlogin.TourCoordinate.PostCoordinate;
import com.maihuythong.testlogin.TourCoordinate.SendMessage;
import com.maihuythong.testlogin.firebase.PutTokenFirebase;
import com.maihuythong.testlogin.showTourInfo.GetTourInfo;
import com.maihuythong.testlogin.forgotPassword.SendRequestOTPRes;
import com.maihuythong.testlogin.stopPointInfo.feedbackListRes;
import com.maihuythong.testlogin.userInfo.GetVerifyCodeRes;
import com.maihuythong.testlogin.userInfo.SendVerifyCodeRes;
import com.maihuythong.testlogin.userInfo.UpdateUserInfoRes;
import com.maihuythong.testlogin.userInfo.UserInfoRes;
import com.maihuythong.testlogin.invitationTour.ShowInvitationReq;
import com.maihuythong.testlogin.invitationTour.responseInvitation;
import com.maihuythong.testlogin.model.StopPointInfo;
import com.maihuythong.testlogin.model.StopPoints;
import com.maihuythong.testlogin.rate_comment_review.GetCommentTour;
import com.maihuythong.testlogin.rate_comment_review.GetPointOfTour;
import com.maihuythong.testlogin.rate_comment_review.SendCommentTour;
import com.maihuythong.testlogin.rate_comment_review.SendReviewTour;
import com.maihuythong.testlogin.showAccountTours.ShowAccountToursReq;
import com.maihuythong.testlogin.showlist.ShowListReq;
import com.maihuythong.testlogin.updateTour.UpdateTourReq;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

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

    @GET("/tour/get/review-point-stats")
    Call<GetPointOfTour> getPointOfTour(@Header("Authorization") String s, @Query("tourId") long tourId);

    @GET("/tour/comment-list")
    Call<GetCommentTour> getCommentTour(@Header("Authorization") String s, @Query("tourId") long tourId, @Query("pageIndex") long pageIndex, @Query("pageSize") long pageSize);


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

    @POST("/tour/add/review")
    @FormUrlEncoded
    Call<SendReviewTour> sendReview(
        @Header("Authorization") String s,
        @Field("tourId") long tourId,
        @Field("point") int point,
        @Field("review") String contentReview
    );

    @POST("/tour/comment")
    @FormUrlEncoded
    Call<SendCommentTour> sendCommentTour(
        @Header("Authorization") String s,
        @Field("tourId") int tourId,
        @Field("userId") int userId,
        @Field("comment") String comment
    );


    @GET("/user/info")
    Call<UserInfoRes> getUserInfo(
            @Header("Authorization") String s
    );

    @POST("/user/edit-info")
    @FormUrlEncoded
    Call<UpdateUserInfoRes> EditInfo(
            @Header("Authorization") String s,
            @Field("fullName") String fullName,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("gender") Number gender,
            @Field("dob")Date dob
            );

    @GET("/user/send-active")
    Call<GetVerifyCodeRes> getVerify(
            @Query("userId")
                long userId,
            @Query("type")
                String type
    );

    @GET("/user/active")
    Call<SendVerifyCodeRes> sendVerifycode(
            @Query("userId")
                    long userId,
            @Query("type")
                    String type,
            @Query("verifyCode")
                    String verifyCode
    );


    @GET("/tour/info")
    Call<GetTourInfo> getTourInfo(@Header("Authorization") String s, @Query("tourId") long tourId );

    @POST("/user/request-otp-recovery")
    @FormUrlEncoded
    Call<SendRequestOTPRes> sendOTPRequest(
            @Field("type") String type,
            @Field("value") String value
    );

    @POST("/user/verify-otp-recovery")
    @FormUrlEncoded
    Call<GetVerifyCodeRes> verifyOTP(
            @Field("userId") long userId,
            @Field("newPassword") String newPassword,
            @Field("verifyCode") String verifyCode
    );

    @POST("/user/notification/put-token")
    @FormUrlEncoded
    Call<PutTokenFirebase> putTokenFirebase(
            @Header("Authorization") String s,
            @Field("fcmToken") String fcmToken,
            @Field("deviceId") String deviceId,
            @Field("platform") int platform,
            @Field("appVersion") String appVersion
    );

    @POST("/tour/current-users-coordinate")
    @FormUrlEncoded
    Call<ArrayList<PostCoordinate>> postCoordinate(
            @Header("Authorization") String s,
            @Field("userId") String userId,
            @Field("tourId") String tourId,
            @Field("lat") double latitude,
            @Field("long") double longitude
    );

    @GET("/tour/notification-list")
    Call<GetNotificationList> getNotificationList(@Header("Authorization") String s,
                                          @Query("tourId") String tourId,
                                          @Query("pageIndex") long pageIndex,
                                          @Query("pageSize") String pageSize );

    @POST("/tour/notification")
    @FormUrlEncoded
    Call<SendMessage> sendMessage(
            @Header("Authorization") String s,
            @Field("tourId") String tourId,
            @Field("userId") String userId,
            @Field("noti") String noti
    );

    @GET("/tour/get/feedback-point-stats")
    Call<GetPointOfTour> getFeedbackPointOfService(@Header("Authorization") String s, @Query("serviceId") int serviceId);


    @POST("/tour/add/feedback-service")
    @FormUrlEncoded
    Call<UpdateUserInfoRes> sendfeedback(
            @Header("Authorization") String s,
            @Field("serviceId") long serviceId,
            @Field("point") int point,
            @Field("feedback") String feedback
    );

    @GET("/tour/get/feedback-service")
    Call<feedbackListRes> getFeedBackList(
            @Header("Authorization") String s,
            @Query("serviceId") Number serviceId,
            @Query("pageIndex") Number pageIndex,
            @Query("pageSize") Number pageSize
    );



}
