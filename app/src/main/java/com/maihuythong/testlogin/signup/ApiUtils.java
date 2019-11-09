package com.maihuythong.testlogin.signup;

public class ApiUtils {

    private ApiUtils() {}

//    public static final String BASE_URL = "http://jsonplaceholder.typicode.com/";

    public static final String BASE_URL = "http://35.197.153.192:3000/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}