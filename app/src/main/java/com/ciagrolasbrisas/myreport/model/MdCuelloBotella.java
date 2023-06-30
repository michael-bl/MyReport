package com.ciagrolasbrisas.myreport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MdCuelloBotella implements Serializable {
        @SerializedName("code")
        @Expose
        private String code;

        @SerializedName("fecha")
        @Expose
        private String fecha;

        @SerializedName("dniEncargado")
        @Expose
        private String dniEncargado;

        @SerializedName("lote")
        @Expose
        private String lote;

        @SerializedName("seccion")
        @Expose
        private String seccion;

        @SerializedName("motivo")
        @Expose
        private String motivo;

        @SerializedName("hora_inicio")
        @Expose
        private String hora_inicio;

        @SerializedName("hora_final")
        @Expose
        private String hora_final;

        @SerializedName("accion")
        @Expose
        private int accion;

        public MdCuelloBotella() {
        }

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

        public String getDniEncargado() {
                return dniEncargado;
        }

        public void setDniEncargado(String dniEncargado) {
                this.dniEncargado = dniEncargado;
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

        public String getMotivo() {
                return motivo;
        }

        public void setMotivo(String motivo) {
                this.motivo = motivo;
        }

        public String getHora_inicio() {
                return hora_inicio;
        }

        public void setHora_inicio(String hora_inicio) {
                this.hora_inicio = hora_inicio;
        }

        public String getHora_final() {
                return hora_final;
        }

        public void setHora_final(String hora_final) {
                this.hora_final = hora_final;
        }

        public int getAccion() {
                return accion;
        }

        public void setAccion(int accion) {
                this.accion = accion;
        }
}
