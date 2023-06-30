package com.ciagrolasbrisas.myreport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MdLote implements Serializable {
    @SerializedName("grupoForza")
    @Expose
    private String grupoForza;

    @SerializedName("ciclo")
    @Expose
    private int ciclo;

    @SerializedName("lote")
    @Expose
    private String lote;

    @SerializedName("seccion")
    @Expose
    private String seccion;

    public MdLote(){}

    public String getGrupoForza() {
        return grupoForza;
    }

    public void setGrupoForza(String grupoForza) {
        this.grupoForza = grupoForza;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
}
