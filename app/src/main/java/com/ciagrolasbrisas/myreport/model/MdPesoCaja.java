package com.ciagrolasbrisas.myreport.model;

public class MdPesoCaja {
    private String code;
    private String fecha;
    private String dni_encargado;
    private String cliente;
    private String calibre;
    private String peso;
    private String observacion;
    private String hora_captura;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDni_encargado() {
        return dni_encargado;
    }

    public void setDni_encargado(String dni_encargado) {
        this.dni_encargado = dni_encargado;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getHora_captura() {
        return hora_captura;
    }

    public void setHora_captura(String hora_captura) {
        this.hora_captura = hora_captura;
    }
}
