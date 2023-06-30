package com.ciagrolasbrisas.myreport.controller;

import java.util.Calendar;

public class GetStringTime {

        public GetStringTime(){}

        public String getHora() {
                Calendar calendar = Calendar.getInstance();
                int hora = calendar.get(Calendar.HOUR_OF_DAY);
                int minuto = calendar.get(Calendar.MINUTE);
                int segundo = calendar.get(Calendar.SECOND);

                String sthora="", stmin="", stseg="";

                if (hora<10){
                        sthora = "0" + hora;
                } else {
                        sthora = String.valueOf(hora);
                }
                if (minuto<10){
                        stmin = "0" + minuto;
                }else {
                        stmin = String.valueOf(minuto);
                }
                if (segundo<10){
                        stseg = "0" + segundo;
                }else {
                        stseg = String.valueOf(segundo);
                }

                return  sthora + ":" + stmin + ":" + stseg;
        }
}
