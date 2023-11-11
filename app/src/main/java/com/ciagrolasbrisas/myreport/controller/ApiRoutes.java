package com.ciagrolasbrisas.myreport.controller;

import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.model.MdUsuario;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiRoutes {

    // Inicio de sesion
    @FormUrlEncoded
    @POST("conexion.php/{PrmtUser}/{PrmtPass}")
    Call<List<MdUsuario>> login(@Field("PrmtUser") String a, @Field("PrmtPass") String b);


    @FormUrlEncoded
    @POST("reporte_x_encargado.php/{id_manager}/{date}")
    Call<List<MdUsuario>> reporteXencargado(@Field("id_manager") String a, @Field("date") String b);


    // Agrega nuevo cuello de botella de cosecha
    @Headers({ "Content-Type: application/json"})
    @POST("new_cuellobotcos.php")
    Call<JsonObject> newCuelloBotellaCosecha(@Body String reporte);






    // Agrega nuevo, actualiza o desactiva usuario
    @GET("accionusuario/{id}/{fk_localidad}/{nombre}/{pass}/{telefono}/{email}/{direccion}/{accion}/{estado}")
    Call<JsonObject> accionUsuario(@Path("id") String a,
                                   @Path("fk_localidad") int b,
                                   @Path("nombre") String c,
                                   @Path("pass") String d,
                                   @Path("telefono") String e,
                                   @Path("email") String f,
                                   @Path("direccion") String g,
                                   @Path("accion") int h,
                                   @Path("estado") int i);


    // Agrega nuevo, actualiza o desactiva cliente
    @GET("accioncliente/{id}/{fk_localidad}/{nombre}/{telefono}/{email}/{direccion}/{accion}/{estado}")
    Call<JsonObject> accionCliente(@Path("id") String a,
                                   @Path("fk_localidad") int b,
                                   @Path("nombre") String c,
                                   @Path("telefono") String d,
                                   @Path("email") String e,
                                   @Path("direccion") String f,
                                   @Path("accion") int g,
                                   @Path("estado") int h);

    // Retorna lista de productos activos
    @GET("productos")
    Call<JsonArray> getProductos();

    // Agrega nuevo, actualiza o desactiva producto
    @GET("accionproducto/{id}/{fk_unidad}/{descripcion}/{utilidad}/{precio_compra}/{precio_venta}/{accion}/{estado}")
    Call<JsonObject> accionProducto(@Path("id") int a,
                                    @Path("fk_unidad") int b,
                                    @Path("descripcion") String c,
                                    @Path("utilidad") String d,
                                    @Path("precio_compra") String e,
                                    @Path("precio_venta") String f,
                                    @Path("accion") int g,
                                    @Path("estado") int h);

    // Retorna lista de unidades
    @GET("unidades")
    Call<JsonArray> getUnidades();

    // Retorna lista de localidades
    @GET("localidades")
    Call<JsonArray> getLocalidades();

    // Agrega nuevo, actualiza o desactiva localidad
    @GET("accionlocalidad/{id}/{localidad}/{accion}/{estado}")
    Call<JsonObject> accionLocalidad(@Path("id") int a,
                                   @Path("localidad") String b,
                                   @Path("accion") int c,
                                   @Path("estado") int d);

    // Retorna lista de proveedores
    @GET("proveedores")
    Call<JsonArray> getProveedores();

    // Agrega nuevo, actualiza o desactiva proveedor
    @GET("accionproveedor/{id}/{nombre}/{telefono}/{email}/{accion}/{estado}")
    Call<JsonObject> accionProveedor(@Path("id") String a,
                                     @Path("nombre") String b,
                                     @Path("telefono") String c,
                                     @Path("email") String d,
                                     @Path("accion") int e,
                                     @Path("estado") int f);

    // Retorna lista de cabeceras de factura
    @GET("cabecerasfacturas")
    Call<JsonArray> getCabeceraFactura();

    // Retorna lista detalles de factura
    @GET("detallesfacturas")
    Call<JsonArray> getDetalleFactura();
}
