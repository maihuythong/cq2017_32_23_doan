package com.maihuythong.testlogin.manager;

import android.app.Application;

import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.model.TokenInfo;

/**
 * Created by tpl on 12/13/16.
 */

public class MyApplication extends Application {
//    private TokenInfo tokenInfo;
//
//    public TokenInfo getTokenInfo() {
//        return tokenInfo;
//    }
//
//    public void setTokenInfo(TokenInfo tokenInfo) {
//        this.tokenInfo = tokenInfo;
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor editor = sharedPref.edit();
//        String str = new Gson().toJson(tokenInfo);
//        editor.putString(getString(R.string.saved_token), str);
//        editor.commit();
//    }
//
//    public void loadTokenInfo() {
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String tokenStr = sharedPref.getString(getString(R.string.saved_token), null);
//        if(TextUtils.isEmpty(tokenStr)) {
//
//        }else{
//            tokenInfo = new Gson().fromJson(tokenStr, new TypeToken<TokenInfo>(){}.getType());
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        loadTokenInfo();
//    }

    // private TokenInfo tokenInfo;
    private String token;

    public String getToken(){return  token;}
//    public TokenInfo getTokenInfo() {
//        return tokenInfo;
//    }

//    public void setTokenInfo(TokenInfo tokenInfo) {
//        this.tokenInfo = tokenInfo;
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor editor = sharedPref.edit();
//   //     String str = new Gson().toJson(tokenInfo);
//        editor.putString(getString(R.string.saved_token), str);
//        editor.commit();
//    }

    public void setToken(String tokenString){
        this.token=tokenString;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login_token",tokenString);
    }
    //    public void loadTokenInfo() {
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String tokenStr = sharedPref.getString(getString(R.string.saved_token), null);
//        if(TextUtils.isEmpty(tokenStr)) {
//
//        }else{
//            tokenInfo = new Gson().fromJson(tokenStr, new TypeToken<TokenInfo>(){}.getType());
//        }
//    }
    public void loadToken(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String tokenStr = sharedPreferences.getString("login_token", null);
        if(TextUtils.isEmpty(tokenStr)) {

        }else{
            token = this.token;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadToken();
    }
}
