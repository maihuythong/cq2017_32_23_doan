package com.maihuythong.testlogin.network;

import com.maihuythong.testlogin.model.LoginRequest;
import com.maihuythong.testlogin.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tpl on 12/13/16.
 */

public interface UserService {
    @POST("/login.php")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/logout.php")
    Call<Void> logout();
}
