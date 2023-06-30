package com.ciagrolasbrisas.myreport.model;

import java.io.Serializable;

public class MdMuestra implements Serializable {

    private int ibd;
    private MdLote mdLote;
    private float brix;
    private int accion;
    private int tamanio;
    private String fecha;
    private String muestreo;
    private float traslucidez;
    private int semanasCosecha;
    private String tipoMuestreo;

    public MdMuestra(){}

    public String getMuestreo() {
        return muestreo;
    }

    public void setMuestreo(String muestreo) {
        this.muestreo = muestreo;
    }

    public MdLote getLote() {
        return mdLote;
    }

    public void setLote(MdLote mdLote) {
        this.mdLote = mdLote;
    }

    public String getTipoMuestreo() {
        return tipoMuestreo;
    }

    public void setTipoMuestreo(String tipoMuestreo) {
        this.tipoMuestreo = tipoMuestreo;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public int getIbd() {
        return ibd;
    }

    public void setIbd(int ibd) {
        this.ibd = ibd;
    }

    public float getTraslucidez() {
        return traslucidez;
    }

    public void setTraslucidez(float traslucidez) {
        this.traslucidez = traslucidez;
    }

    public float getBrix() {
        return brix;
    }

    public void setBrix(float brix) {
        this.brix = brix;
    }
    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getSemanasCosecha() {
        return semanasCosecha;
    }

    public void setSemanasCosecha(int semanasCosecha) {
        this.semanasCosecha = semanasCosecha;
    }
}
