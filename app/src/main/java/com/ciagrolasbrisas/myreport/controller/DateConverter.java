package com.ciagrolasbrisas.myreport.controller;

import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private LogGenerator logGenerator;
    private String date, time, clase;

    public DateConverter(){}

    public String dateFormat(String data){
        String funcion = new Throwable().getStackTrace()[0].getMethodName();
        GetStringDate stringDate = new GetStringDate();
        GetStringTime stringTime = new GetStringTime();
        date = stringDate.getFecha();
        time = stringTime.getHora();

        logGenerator = new LogGenerator();
        clase = this.getClass().getSimpleName();

        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date fecha = formatoEntrada.parse(data);
            String fechaFormateada = formatoSalida.format(fecha);
            return fechaFormateada;
        } catch (Exception e) {
            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e);
        }
        return null;
    }
}
