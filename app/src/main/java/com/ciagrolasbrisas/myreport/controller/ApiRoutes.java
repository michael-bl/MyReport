package com.ciagrolasbrisas.myreport.controller;

import com.ciagrolasbrisas.myreport.model.MdUsuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRoutes {

    // Inicio de sesion
    @FormUrlEncoded
    @POST("conexion.php/{PrmtUser}/{PrmtPass}")
    Call<List<MdUsuario>> login(@Field("PrmtUser") String a, @Field("PrmtPass") String b);

}
