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
    private TokenInfo tokenInfo;

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        String str = new Gson().toJson(tokenInfo);
        editor.putString(getString(R.string.saved_token), str);
        editor.commit();
    }

    public void loadTokenInfo() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String tokenStr = sharedPref.getString(getString(R.string.saved_token), null);
        if(TextUtils.isEmpty(tokenStr)) {

        }else{
            tokenInfo = new Gson().fromJson(tokenStr, new TypeToken<TokenInfo>(){}.getType());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadTokenInfo();
    }
}
