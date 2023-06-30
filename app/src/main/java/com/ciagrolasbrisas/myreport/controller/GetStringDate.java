package com.ciagrolasbrisas.myreport.controller;

import java.util.Calendar;

public class GetStringDate {
        public GetStringDate(){
        }

        public String getFecha(){
                Calendar calendar = Calendar.getInstance();
                String stringDate;
                int anio = calendar.get(Calendar.YEAR);
                int mes = calendar.get(Calendar.MONTH);
                int dia = calendar.get(Calendar.DAY_OF_MONTH);

                if (dia < 10) if (mes < 10)
                        stringDate = "0" + dia + "/" + "0" + (mes+1) + "/" + anio;
                else stringDate = "0" + dia + "/" + (mes+1) + "/" + anio;
                else if (mes < 10) stringDate = dia + "/" + "0" + (mes+1) + "/" + anio;
                else stringDate = dia + "/" + (mes+1) + "/" + anio;

                return (stringDate);
        }
}
