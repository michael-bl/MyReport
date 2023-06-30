package com.ciagrolasbrisas.myreport.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.model.MdLote;
import com.ciagrolasbrisas.myreport.model.MdMuestra;
import com.ciagrolasbrisas.myreport.model.MdUsuario;

import java.util.ArrayList;


public class DatabaseController {
        private DatabaseHelper dbHelper;
        private SQLiteDatabase sqLiteDatabase;
        private static int codigo;

        public DatabaseController() {
        }

        public String crearDbLocal(Context context2) {
                dbHelper = new DatabaseHelper(context2);
                ExistSqliteDatabase existdb = new ExistSqliteDatabase();
                try {
                        if (existdb.ExistSqliteDatabase()) return "1";
                        else {
                                dbHelper.getWritableDatabase();
                                return "2";
                        }
                } catch (IllegalStateException illegalStateException) {
                        illegalStateException.printStackTrace();
                }
                return "0";
        }

        /*---------------------------------------------------Crear Registros por Default----------------------------------------------------------------------------------------------*/
        public void insertDafaultCuelloBotella(Context context) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        long resultado = -1;
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        String[] motivosCB = {"Acomódo de cosechadora", "Almuerzo", "Ataque de abejas", "Auditoría", "Botar cera", "Café", "Campaña de vacunación", "Capacitación", "Desayuno", "Esperando carretas", "Exámen médico", "Jornada", "Pegaderos", "Respuesta taller", "Reunión", "Traslado", "Cambio de tractor", "Tormenta eléctrica", "Problemas de tracción", "Atraso por planta", "Otro"};

                        for (int i = 0; i <= motivosCB.length - 1; i++) {
                                ContentValues values = new ContentValues();
                                values.put("code", (i + 1));
                                values.put("motivo", motivosCB[i]);
                                resultado = sqLiteDatabase.insert("motivocb", null, values);
                        }
                        if (resultado == -1) {
                                Log.i("MyReport", "DataBaseHelper/Error al guardar lista cuellos de botella por defecto.");
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseHelper/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }

        public void insertDafaultUsuario(Context context) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        long resultado = -1;
                        String[] cedulas = {"503610263", "37", "38", "39", "40", "41", "207980186", "206200609", "206050068", "206030844"};
                        String[] password = {"123", "lkjh37", "38lkjh", "39asdf", "asdf40", "zxcv41", "Manfred", "Dayana", "Cristian", "Harold"};
                        String[] depto = {"Oficinas", "Cosecha", "Cosecha", "Cosecha", "Cosecha", "Cosecha", "Planta", "Planta", "Planta", "Planta"};


                        for (int i = 0; i <= cedulas.length - 1; i++) {
                                ContentValues values = new ContentValues();
                                values.put("code", (i + 1));
                                values.put("dni", cedulas[i]);
                                values.put("password", password[i]);
                                values.put("departamento", depto[i]);
                                resultado = sqLiteDatabase.insert("usuario", null, values);
                        }
                        if (resultado == -1) {
                                Log.i("MyReport", "DataBaseHelper/Error al guardar lista de usuarios por defecto.");
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseHelper/Error" + npe, Toast.LENGTH_LONG).show();
                }

        }


        public void insertDafaultCliente(Context context) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        long resultado = -1;
                        String[] nombre = {"ALEXANDERS-GENERICO", "KEELINGS", "WALMART", "TICO FARM", "CAPA-GENERICO", "COUNTRY FRESH", "VERITA", "JALARAM", "CHIQUITA", "LAS BRISAS", "MICHELLE"};

                        for (int i = 0; i <= nombre.length - 1; i++) {
                                ContentValues values = new ContentValues();
                                values.put("code", (i + 1));
                                values.put("nombre", nombre[i]);
                                resultado = sqLiteDatabase.insert("cliente", null, values);
                        }
                        if (resultado == -1) {
                                Log.i("MyReport", "DataBaseHelper/Error al guardar lista de clientes.");
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseHelper/Error" + npe, Toast.LENGTH_LONG).show();
                }

        }

        public void insertDafaultCalibre(Context context) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        long resultado = -1;
                        String[] nombre = {"4", "5", "6", "7", "8", "9", "10", "12", "4 PIEZAS", "5 PIEZAS", "6 PIEZAS", "7 PIEZAS", "8 PIEZAS", "9 PIEZAS", "10 PIEZAS"};

                        for (int i = 0; i <= nombre.length - 1; i++) {
                                ContentValues values = new ContentValues();
                                values.put("code", (i + 1));
                                values.put("calibre", nombre[i]);
                                resultado = sqLiteDatabase.insert("calibre", null, values);
                        }
                        if (resultado == -1) {
                                Log.i("MyReport", "DataBaseHelper/Error al guardar lista de calibres.");
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseHelper/Error" + npe, Toast.LENGTH_LONG).show();
                }

        }

        /*---------------------------------------------------Crear Nuevo Registro--------------------------------------------------------------------------------------------------------*/
        public void nuevoMuestreoPremas(Context context, MdMuestra muestreo) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        sqLiteDatabase.beginTransaction();
                        ContentValues values = new ContentValues();
                        MdLote mdLote = muestreo.getLote();

                        values.put("id_muestreo", consecutivoPremaduracion());
                        values.put("grupo_forza", mdLote.getGrupoForza());
                        values.put("fecha_muestreo", muestreo.getFecha());
                        values.put("ciclo", mdLote.getCiclo());
                        values.put("mdLote", mdLote.getLote());
                        values.put("seccion", mdLote.getSeccion());
                        values.put("tamanio", muestreo.getTamanio());
                        values.put("brix", muestreo.getBrix());
                        values.put("traslucidez", muestreo.getTraslucidez());
                        values.put("ibd", muestreo.getIbd());
                        values.put("tipo", muestreo.getTipoMuestreo());
                        values.put("muestreo", muestreo.getMuestreo());

                        long resultado = sqLiteDatabase.insert("premaduracion", null, values);
                        sqLiteDatabase.endTransaction();

                        if (resultado != -1)
                                Log.i("MyReport", "DataBaseHelper/Error al guardar el muestreo de premaduraciones");

                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseHelper/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }

        public void nuevoRptCuelloBotella(Context context, MdCuelloBotella muestra) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        int code = consecutivoCuelloBotella();

                        values.put("code", code);
                        values.put("fecha", muestra.getFecha());
                        values.put("dni_encargado", muestra.getDniEncargado());
                        values.put("lote", muestra.getLote());
                        values.put("seccion", muestra.getSeccion());
                        values.put("motivo", muestra.getMotivo());
                        values.put("hora_inicio", muestra.getHora_inicio());
                        values.put("hora_final", muestra.getHora_final());

                        long resultado = sqLiteDatabase.insert("cuellobotella", null, values);

                        if (resultado != -1) {
                                Toast.makeText(context, "DataBaseHelper/Reporte guardado exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                Toast.makeText(context, "DataBaseHelper/Error al guardar reporte: " + resultado + " Code " + muestra.getCode(), Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseHelper/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }

        public void nuevoUsuario(Context context, MdUsuario usuario) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        //int code = consecutivoCuelloBotella();
                        values.put("code", usuario.getId());
                        values.put("dni", usuario.getNombre());
                        values.put("password", usuario.getPass());

                        long resultado = sqLiteDatabase.insert("usuario", null, values);

                        if (resultado != -1) {
                                Toast.makeText(context, "DataBaseHelper/Usuario guardado exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                Toast.makeText(context, "DataBaseHelper/Error al guardar el usuario: " + resultado + " Code " + usuario.getId(), Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseHelper/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }

        /*---------------------------------------------------Obtener el consecutivo----------------------------------------------------------------------------------------------------*/
        private int consecutivoPremaduracion() {
                sqLiteDatabase.beginTransaction();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from premaduracion SQLITE_SEQUENSE", null);
                if (cursor.moveToFirst()) {
                        codigo = cursor.getInt(0) + 1;
                        sqLiteDatabase.endTransaction();
                } else {
                        sqLiteDatabase.endTransaction();
                        return 1;
                }
                cursor.close();
                return codigo;
        }

        private int consecutivoCuelloBotella() {
                try (Cursor cursor = sqLiteDatabase.rawQuery("select cb.code from cuellobotella as cb order by cb.code desc limit 1", null)) {
                        if (cursor.moveToFirst())
                                codigo = cursor.getInt(0) + 1;
                        else return 1;
                }
                return codigo;
        }

        private int consecutivoForza(Context context) {
                try {
                        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select * from forza SQLITE_SEQUENSE", null);
                        if (cursor.moveToFirst()) {
                                codigo = cursor.getInt(0) + 1;
                                sqLiteDatabase.endTransaction();
                        } else {
                                sqLiteDatabase.endTransaction();
                                return 1;
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle, Toast.LENGTH_LONG).show();
                }
                return codigo;
        }

        public boolean existJornada(Context context, String fecha, String dni_encargado, String motivo) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select cb.code  from cuellobotella  as cb where fecha = ? and dni_encargado = ? and motivo = ?", new String[]{fecha, dni_encargado, motivo});
                        if (cursor.moveToFirst()) {
                                return true;
                        } else {
                                return false;
                        }
                } catch (IllegalArgumentException e) {
                        Log.i("MyReport", e.getMessage());
                        return false;
                }
        }

        /*---------------------------------------------------Listar registros-------------------------------------------------------------------------------------------------------------------*/
        public boolean loginUser(Context context, @NonNull MdUsuario usuario) {
                dbHelper = new DatabaseHelper(context);
                sqLiteDatabase = dbHelper.getReadableDatabase();
                @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select * from usuario where dni = ? and password = ?", new String[]{usuario.getId(), usuario.getPass()});
                return cursor.moveToFirst();
        }

        public ArrayList<MdCuelloBotella> selectCuelloBotellaIncompleto(Context context) {
                ArrayList<MdCuelloBotella> listaCB = new ArrayList<>();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select cb.code, cb.fecha, cb.dni_encargado, cb.lote, cb.seccion, cb.motivo, cb.hora_inicio, cb.hora_final, mt.motivo \n" +
                                "from cuellobotella as cb \n" +
                                "inner join motivocb as mt \n" +
                                "on cb.motivo = mt.code \n" +
                                "where cb.hora_final='00:00:00' \n", null);
                        if (cursor.moveToFirst()) {
                                do {
                                        MdCuelloBotella mdCuelloBotella = new MdCuelloBotella();
                                        mdCuelloBotella.setCode(cursor.getString(0));
                                        mdCuelloBotella.setFecha(cursor.getString(1));
                                        mdCuelloBotella.setDniEncargado(cursor.getString(2));
                                        mdCuelloBotella.setLote(cursor.getString(3));
                                        mdCuelloBotella.setSeccion(cursor.getString(4));
                                        mdCuelloBotella.setMotivo(cursor.getString(5) + "-" + cursor.getString(8));
                                        mdCuelloBotella.setHora_inicio(cursor.getString(6));
                                        mdCuelloBotella.setHora_final(cursor.getString(7));
                                        listaCB.add(mdCuelloBotella);
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                Toast.makeText(context, "DataBaseHelper/Advertencia: no hay reportes abiertos", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle, Toast.LENGTH_LONG).show();
                }
                return listaCB;
        }

        public ArrayList<MdCuelloBotella> selectCuelloBotella(Context context, String fecha_desde, String fecha_hasta, boolean esFechaUnica) {
                ArrayList<MdCuelloBotella> listaCB = new ArrayList<>();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor;
                        if (esFechaUnica) {
                                cursor = sqLiteDatabase.rawQuery("select cb.code, cb.fecha, cb.dni_encargado, cb.lote, cb.seccion, cb.motivo, cb.hora_inicio, cb.hora_final, mt.motivo \n" +
                                        "from cuellobotella as cb \n" +
                                        "inner join motivocb as mt    \n" +
                                        "on cb.motivo = mt.code  \n" +
                                        "where cb.fecha = ?", new String[]{fecha_desde});
                        } else {
                                cursor = sqLiteDatabase.rawQuery("select cb.code, cb.fecha, cb.dni_encargado, cb.lote, cb.seccion, cb.motivo, cb.hora_inicio, cb.hora_final, mt.motivo \n" +
                                        "from cuellobotella as cb \n" +
                                        "inner join motivocb as mt    \n" +
                                        "on cb.motivo = mt.code  \n" +
                                        "where cb.fecha >= ? and cb.fecha <= ?", new String[]{fecha_desde, fecha_hasta});
                        }
                        if (cursor.moveToFirst()) {
                                do {
                                        MdCuelloBotella mdCuelloBotella = new MdCuelloBotella();
                                        mdCuelloBotella.setCode(cursor.getString(0));
                                        mdCuelloBotella.setFecha(cursor.getString(1));
                                        mdCuelloBotella.setDniEncargado(cursor.getString(2));
                                        mdCuelloBotella.setLote(cursor.getString(3));
                                        mdCuelloBotella.setSeccion(cursor.getString(4));
                                        mdCuelloBotella.setMotivo(cursor.getString(8));
                                        mdCuelloBotella.setHora_inicio(cursor.getString(6));
                                        mdCuelloBotella.setHora_final(cursor.getString(7));
                                        listaCB.add(mdCuelloBotella);
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                Toast.makeText(context, "DataBaseHelper/Advertencia: no hay reportes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle, Toast.LENGTH_LONG).show();
                }
                return listaCB;
        }

        public String horasEfectivas(Context context, String fecha, String cuadrilla) {
                String respuesta = null;
                dbHelper = new DatabaseHelper(context);
                sqLiteDatabase = dbHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select cb.hora_inicio, cb.hora_final from cuellobotella as cb where fecha = ? and dni_encargado = ? and cb.motivo = 12", new String[]{fecha, cuadrilla});
                if (cursor.moveToFirst()) {
                        do {
                                String hora_inicio = cursor.getString(0);
                                String hora_fin = cursor.getString(1);
                                respuesta = hora_inicio + "/" + hora_fin;
                        } while (cursor.moveToNext());
                        cursor.close();
                }
                return respuesta;
        }

        /*---------------------------------------------------Actualizar registros-----------------------------------------------------------------------------------------------------------*/
        public void updateCuelloBotella(Context context, MdCuelloBotella muestra) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.put("code", muestra.getCode());
                        values.put("fecha", muestra.getFecha());
                        values.put("dni_encargado", muestra.getDniEncargado());
                        values.put("lote", muestra.getLote());
                        values.put("seccion", muestra.getSeccion());
                        values.put("motivo", muestra.getMotivo());
                        values.put("hora_inicio", muestra.getHora_inicio());
                        values.put("hora_final", muestra.getHora_final());

                        long resultado = sqLiteDatabase.update("cuellobotella", values, String.format("code = %s", muestra.getCode()), null);

                        if (resultado != -1) {
                                Toast.makeText(context, "DataBaseHelper/Datos actualizados exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                Toast.makeText(context, "DataBaseHelper/Error al actualizar información!", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseHelper/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }
}