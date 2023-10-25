package com.ciagrolasbrisas.myreport.controller;

public class ApiUtils {

    private ApiUtils() {}
    public static final String BASE_URL = "https://reportes.ciagrolasbrisas.com/";
    public static ApiRoutes getApiServices() {
        return RetrofitAdapter.getClient(BASE_URL).create(ApiRoutes.class);
    }
}
