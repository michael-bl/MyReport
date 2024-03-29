package com.ciagrolasbrisas.myreport.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.ciagrolasbrisas.myreport.controller.GetConsecutive;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;

import java.util.ArrayList;

public class CbCosechaController {
        private DatabaseHelper dbHelper;
        private SQLiteDatabase sqLiteDatabase;
        private GetConsecutive getConsecutive;
        private LogGenerator logGenerator;
        private String date, time, clase;

        public CbCosechaController(){
                GetStringDate stringDate = new GetStringDate();
                GetStringTime stringTime = new GetStringTime();
                date = stringDate.getFecha();
                time = stringTime.getHora();
                logGenerator = new LogGenerator();
                clase = this.getClass().getSimpleName();
        }

        public void nuevoRptCuelloBotellaCos(Context context, MdCuelloBotella muestra) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        getConsecutive = new GetConsecutive();
                        int code = getConsecutive.funGetConsecutive(context, "cuellobotellacos");

                        values.put("code", code);
                        values.put("fecha", muestra.getFecha());
                        values.put("cedula", muestra.getCedula());
                        values.put("lote", muestra.getLote());
                        values.put("seccion", muestra.getSeccion());
                        values.put("motivo", muestra.getMotivo());
                        values.put("hora_inicio", muestra.getHora_inicio());
                        values.put("hora_final", muestra.getHora_final());
                        values.put("sync", 0); // 0 = falso o no sincronizado

                        long resultado = sqLiteDatabase.insert("cuellobotellacos", null, values);

                        if (resultado != -1) {
                                Toast.makeText(context, "Reporte guardado exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Error al guardar reporte: " + resultado + " Code " + muestra.getCode()); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Error al guardar reporte: " + resultado + " Code " + muestra.getCode(), Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        public ArrayList<MdCuelloBotella> getCuelloBotellaIncompleto(Context context, String fecha) {
                ArrayList<MdCuelloBotella> listaCB = new ArrayList<>();
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select cb.code, cb.fecha, cb.cedula, cb.lote, cb.seccion, cb.motivo, cb.hora_inicio, cb.hora_final, mt.motivo \n" +
                                "from cuellobotellacos as cb \n" +
                                "inner join motivocbcos as mt \n" +
                                "on cb.motivo = mt.code \n" +
                                "where cb.hora_final='null' \n" +
                                "and cb.fecha = ?", new String[]{fecha});
                        if (cursor.moveToFirst()) {
                                do {
                                        MdCuelloBotella mdCuelloBotella = new MdCuelloBotella();
                                        mdCuelloBotella.setCode(cursor.getString(0));
                                        mdCuelloBotella.setFecha(cursor.getString(1));
                                        mdCuelloBotella.setCedula(cursor.getString(2));
                                        mdCuelloBotella.setLote(cursor.getString(3));
                                        mdCuelloBotella.setSeccion(cursor.getString(4));
                                        mdCuelloBotella.setMotivo(cursor.getString(5) + "-" + cursor.getString(8));
                                        mdCuelloBotella.setHora_inicio(cursor.getString(6));
                                        mdCuelloBotella.setHora_final(cursor.getString(7));
                                        listaCB.add(mdCuelloBotella);
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Advertencia: no hay reportes abiertos."); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Advertencia: no hay reportes abiertos", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
                return listaCB;
        }

        public ArrayList<MdCuelloBotella> getCuelloBotella(Context context, String fecha_desde, String fecha_hasta, boolean esFechaUnica) {
                ArrayList<MdCuelloBotella> listaCB = new ArrayList<>();
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor;
                        if (esFechaUnica) {
                                cursor = sqLiteDatabase.rawQuery("select cb.code, cb.fecha, cb.cedula, cb.lote, cb.seccion, cb.motivo, cb.hora_inicio, cb.hora_final, mt.motivo \n" +
                                        "from cuellobotellacos as cb \n" +
                                        "inner join motivocbcos as mt    \n" +
                                        "on cb.motivo = mt.code  \n" +
                                        "where cb.fecha = ?", new String[]{fecha_desde});
                        } else {
                                cursor = sqLiteDatabase.rawQuery("select cb.code, cb.fecha, cb.cedula, cb.lote, cb.seccion, cb.motivo, cb.hora_inicio, cb.hora_final, mt.motivo \n" +
                                        "from cuellobotellacos as cb \n" +
                                        "inner join motivocbcos as mt    \n" +
                                        "on cb.motivo = mt.code  \n" +
                                        "where cb.fecha >= ? and cb.fecha <= ?", new String[]{fecha_desde, fecha_hasta});
                        }
                        if (cursor.moveToFirst()) {
                                do {
                                        MdCuelloBotella mdCuelloBotella = new MdCuelloBotella();
                                        mdCuelloBotella.setCode(cursor.getString(0));
                                        mdCuelloBotella.setFecha(cursor.getString(1));
                                        mdCuelloBotella.setCedula(cursor.getString(2));
                                        mdCuelloBotella.setLote(cursor.getString(3));
                                        mdCuelloBotella.setSeccion(cursor.getString(4));
                                        mdCuelloBotella.setMotivo(cursor.getString(5) + "-" + cursor.getString(8));
                                        mdCuelloBotella.setHora_inicio(cursor.getString(6));
                                        mdCuelloBotella.setHora_final(cursor.getString(7));
                                        listaCB.add(mdCuelloBotella);
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Advertencia: no hay reportes guardados."); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Advertencia: no hay reportes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
                return listaCB;
        }

        public MdCuelloBotella horasEfectivas(Context context, String fecha, String cuadrilla) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                MdCuelloBotella obj = new MdCuelloBotella();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select cb.hora_inicio, cb.hora_final from cuellobotellacos as cb where fecha = ? and cedula = ? and cb.motivo = 12", new String[]{fecha, cuadrilla});
                        if (cursor.moveToFirst()) {
                                do {
                                        obj.setHora_inicio(cursor.getString(0));
                                        obj.setHora_final(cursor.getString(1));
                                } while (cursor.moveToNext());
                                cursor.close();
                        }
                }  catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
                return obj;
        }


        public void updateCuelloBotella(Context context, MdCuelloBotella muestra) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.put("code", muestra.getCode());
                        values.put("fecha", muestra.getFecha());
                        values.put("cedula", muestra.getCedula());
                        values.put("lote", muestra.getLote());
                        values.put("seccion", muestra.getSeccion());
                        values.put("motivo", muestra.getMotivo());
                        values.put("hora_inicio", muestra.getHora_inicio());
                        values.put("hora_final", muestra.getHora_final());
                        values.put("sync", 0); // 0 = falso o no sincronizado

                        long resultado = sqLiteDatabase.update("cuellobotellacos", values, String.format("code = %s", muestra.getCode()), null);

                        if (resultado != -1) {
                                Toast.makeText(context, "Datos actualizados exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + " Error al actualizar información!"); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Error al actualizar información!", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        /*---------------------------------------------------Verifica existencia de jornada especifica-------------------------------------------------------------------------*/
        public boolean existJornada(Context context, String fecha, String cedula, String motivo) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select cb.code  from cuellobotellacos  as cb where fecha = ? and cedula = ? and motivo = ?", new String[]{fecha, cedula, motivo});
                        if (cursor.moveToFirst()) {
                                return true;
                        } else {
                                return false;
                        }
                } catch (IllegalArgumentException e) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e); // Agrega error en Descargas/Logs.txt
                        return false;
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle); // Agrega error en Descargas/Logs.txt
                        return false;
                }
        }
}
