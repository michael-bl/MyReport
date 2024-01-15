package com.ciagrolasbrisas.myreport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MdUsuario implements Serializable {

    public MdUsuario(){
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("cedula")
    @Expose
    private String cedula;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("pass")
    @Expose
    private String pass;

    @SerializedName("estado")
    @Expose
    private int estado;

    @SerializedName("accion")
    @Expose
    private int accion;

    @SerializedName("departamento")
    @Expose
    private String departamento;

    @SerializedName("rol")
    @Expose
    private String rol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
