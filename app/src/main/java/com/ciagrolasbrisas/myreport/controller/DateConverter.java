package com.ciagrolasbrisas.myreport.controller;

import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private LogGenerator logGenerator;
    private String date, time, clase;

    public DateConverter(){}

    public String dateFormat(String date){
        GetStringDate stringDate = new GetStringDate();
        GetStringTime stringTime = new GetStringTime();
        date = stringDate.getFecha();
        time = stringTime.getHora();
        logGenerator = new LogGenerator();
        clase = this.getClass().getSimpleName();
        String funcion = new Throwable().getStackTrace()[0].getMethodName();
        //SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //Date fecha = formatoEntrada.parse(date);
            String fechaFormateada = formatoSalida.format(date);
            return fechaFormateada;
        } catch (Exception e) {
            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e);
            //Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
