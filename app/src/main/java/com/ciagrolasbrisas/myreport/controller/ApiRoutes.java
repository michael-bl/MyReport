package com.ciagrolasbrisas.myreport.controller;

import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.model.MdUsuario;
import com.google.gson.JsonArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiRoutes {

    // Inicio de sesion
    @FormUrlEncoded
    @POST("conexion.php/{user}/{pass}")
    Call<List<MdUsuario>> login(@Field("user") String a, @Field("pass") String b);

    @Headers({"Accept: application/json; Content-Type: application/json; charset=utf-8; deviceplatform:android; Agent:Mozilla/5.0"})
    @POST("cuelloBotellaCos.php")
    Call<List<MdCuelloBotella>> getCuellobCosecha(@Body String reportArray);

}
