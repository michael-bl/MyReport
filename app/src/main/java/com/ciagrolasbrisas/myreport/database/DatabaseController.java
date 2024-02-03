package com.ciagrolasbrisas.myreport.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ciagrolasbrisas.myreport.controller.GetConsecutive;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.model.MdLote;
import com.ciagrolasbrisas.myreport.model.MdMuestra;
import com.ciagrolasbrisas.myreport.model.MdPesoCaja;
import com.ciagrolasbrisas.myreport.model.MdUsuario;

import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseController {
        private DatabaseHelper dbHelper;
        private SQLiteDatabase sqLiteDatabase;
        private GetConsecutive getConsecutive;
        public static String nombreUsuario;
        private LogGenerator logGenerator;
        private String date, time, clase;

        public DatabaseController() {
                GetStringDate stringDate = new GetStringDate();
                GetStringTime stringTime = new GetStringTime();
                date = stringDate.getFecha();
                time = stringTime.getHora();
                logGenerator = new LogGenerator();
                clase = this.getClass().getSimpleName();
        }

        public String crearDbLocal(Context context2) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                dbHelper = new DatabaseHelper(context2);
                ExistSqliteDatabase existdb = new ExistSqliteDatabase();
                try {
                        if (existdb.ExistSqliteDatabase()) return "1";
                        else {
                                dbHelper.getWritableDatabase();
                                return "2";
                        }
                } catch (IllegalStateException ise) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + ise); // Agrega error en Descargas/Logs.txt
                }
                return "0";
        }

        /*---------------------------------------------------Crear Registros por Default----------------------------------------------------------------------------------------------*/
        public void insertDefaultCliente(Context context) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        long resultado = -1;
                        String[] nombre = {"ALEXANDER-GENERICO", "KEELINGS", "WALMART", "TICO FARM", "CAPA-GENERICO", "COUNTRY FRESH", "VERITA", "JALARAM", "CHIQUITA", "LAS BRISAS", "MICHELLE"};

                        for (int i = 0; i <= nombre.length - 1; i++) {
                                ContentValues values = new ContentValues();
                                values.put("code", (i + 1));
                                values.put("nombre", nombre[i]);
                                resultado = sqLiteDatabase.insert("cliente", null, values);
                        }
                        if (resultado == -1) {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Error al guardar lista de clientes."); // Agrega error en Descargas/Logs.txt
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
                }

        }

        public void insertDefaultCalibre(Context context) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        long resultado = -1;
                        String[] nombre = {"4", "5", "6", "7", "8", "9", "10", "12", "4 PIEZAS", "5 PIEZAS", "6 PIEZAS", "7 PIEZAS", "8 PIEZAS", "9 PIEZAS", "10 PIEZAS"};

                        for (int i = 0; i <= nombre.length - 1; i++) {
                                ContentValues values = new ContentValues();
                                //values.put("code", (i + 1));
                                values.put("calibre", nombre[i]);
                                resultado = sqLiteDatabase.insert("calibre", null, values);
                        }
                        if (resultado == -1) {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Error al guardar lista de calibres."); // Agrega error en Descargas/Logs.txt
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
                }

        }

        public void insertDefaultLocalMode(Context context) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        long resultado = -1;
                        sqLiteDatabase = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("code", 1);
                        values.put("modo", 0); // indica que se guardaran los datos en remoto
                        resultado = sqLiteDatabase.insert("localmode", null, values);
                        if (resultado == -1) {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Error al guardar modo almacenamiento por defecto."); // Agrega error en Descargas/Logs.txt
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        public void insertDefaultMotivoCb(Context context) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        long resultado = -1;
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        String[] motivosCB = {"Acomódo de cosechadora", "Almuerzo", "Ataque de abejas", "Auditoría", "Botar cera", "Café", "Campaña de vacunación", "Capacitación", "Desayuno", "Esperando carretas", "Exámen médico", "Jornada", "Pegaderos", "Respuesta taller", "Reunión", "Traslado", "Cambio de tractor", "Tormenta eléctrica", "Problemas de tracción", "Atraso por planta", "Otro", "Averías"};

                        for (int i = 0; i <= motivosCB.length - 1; i++) {
                                ContentValues values = new ContentValues();
                                values.put("code", (i + 1));
                                values.put("motivo", motivosCB[i]);
                                values.put("sync", 0);
                                resultado = sqLiteDatabase.insert("motivocbcos", null, values);
                        }
                        if (resultado == -1) {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + " Error al guardar lista cuellos de botella por defecto."); // Agrega error en Descargas/Logs.txt
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        /*---------------------------------------------------Crear Nuevo Registro--------------------------------------------------------------------------------------------------------*/
        public void nuevoMuestreoPremas(Context context, MdMuestra muestreo) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        sqLiteDatabase.beginTransaction();
                        ContentValues values = new ContentValues();
                        MdLote mdLote = muestreo.getLote();
                        int code = getConsecutive.funGetConsecutive(context, "premaduracion");

                        values.put("id_muestreo", code);
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

                        if (resultado == -1)
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Error al guardar el muestreo de premaduraciones."); // Agrega error en Descargas/Logs.txt
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        public void nuevoUsuario(Context context, MdUsuario usuario) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        getConsecutive = new GetConsecutive();
                        int code = getConsecutive.funGetConsecutive(context, "usuario");

                        values.put("code", code);
                        values.put("cedula", usuario.getCedula());
                        values.put("password", usuario.getPass());
                        values.put("nombre", usuario.getNombre());
                        values.put("departamento", usuario.getDepartamento());
                        values.put("rol", usuario.getRol());

                        long resultado = sqLiteDatabase.insert("usuario", null, values);

                        if (resultado != -1) {
                                Toast.makeText(context, "Usuario guardado exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Error al guardar el usuario: " + resultado + " Code " + usuario.getId()); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Error al guardar el usuario: " + resultado + " Code " + usuario.getId(), Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error" + npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        public void nuevoPesoCaja(Context context, MdPesoCaja pesoCaja) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        getConsecutive = new GetConsecutive();
                        int code = getConsecutive.funGetConsecutive(context, "pesocaja");

                        values.put("code", code);
                        values.put("fecha", pesoCaja.getFecha());
                        values.put("dni_encargado", pesoCaja.getCedula());
                        values.put("cliente", pesoCaja.getCliente());
                        values.put("calibre", pesoCaja.getCalibre());
                        values.put("peso", pesoCaja.getPeso());
                        values.put("observacion", pesoCaja.getObservacion());
                        values.put("hora_captura", pesoCaja.getHora_captura());

                        long resultado = sqLiteDatabase.insert("pesocaja", null, values);

                        if (resultado != -1) {
                                Toast.makeText(context, "Peso guardado exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Error al guardar peso de caja: " + resultado); // Agrega error en Descargas/Logs.txt
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error: " + npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        public void nuevoLocalMode(Context context, int localmode) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        getConsecutive = new GetConsecutive();
                        int code = getConsecutive.funGetConsecutive(context, "localmode");
                        long resultado = 0;

                        values.put("modo", localmode);

                        if (code == 1) {
                                resultado = sqLiteDatabase.insert("localmode", null, values);
                        } else {
                                resultado = sqLiteDatabase.update("localmode", values, String.format("code = %s", (code-1)), null);
                        }

                        if (resultado == -1) {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Error al guardar modo local: " + resultado); // Agrega error en Descargas/Logs.txt
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                        Toast.makeText(context, "Error: " + npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        /*---------------------------------------------------Verifica existencia de usuarios-------------------------------------------------------------------------------------------*/
        public boolean existUsersData(Context context) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select count(*)  from usuario ", null);
                        if (cursor.moveToFirst()) {
                                return !cursor.getString(0).equals("0");
                        } else {
                                return false;
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                        return false;
                } catch (IllegalArgumentException e) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e); // Agrega error en Descargas/Logs.txt
                        return false;
                }
        }

        public boolean existUser(Context context, String id) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select count(*)  from usuario where cedula = ?", new String[]{id});
                        if (cursor.moveToFirst()) {
                                return !cursor.getString(0).equals("0");
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

        public String selectCedulaUser(Context context) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select cedula  from usuario limit 1", null);
                        if (cursor.moveToFirst()) {
                                return cursor.getString(0);
                        }
                        return null;
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle); // Agrega error en Descargas/Logs.txt
                        return null;
                }
        }

        public String selectRolUser(Context context) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select rol  from usuario limit 1", null);
                        if (cursor.moveToFirst()) {
                                return cursor.getString(0);
                        }
                        return null;
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle); // Agrega error en Descargas/Logs.txt
                        return null;
                }
        }

        /*---------------------------------------------------Listar registros-------------------------------------------------------------------------------------------------------------------*/
        public boolean loginUser(Context context, @NonNull MdUsuario usuario) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select nombre from usuario where cedula = ? and password = ?", new String[]{usuario.getCedula(), usuario.getPass()});
                        if (cursor.moveToFirst()) {
                                nombreUsuario = cursor.getString(0);
                                return true;
                        }
                        return false;
                } catch (IllegalArgumentException e) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e); // Agrega error en Descargas/Logs.txt
                        return false;
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle); // Agrega error en Descargas/Logs.txt
                        return false;
                }
        }

        public ArrayList<String> selectClientesCajas(Context context) {
                ArrayList<String> listaClientes = new ArrayList<>();
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor;
                        cursor = sqLiteDatabase.rawQuery("select cl.code, cl.nombre \n" +
                                "from cliente as cl", null);

                        if (cursor.moveToFirst()) {
                                do {
                                        listaClientes.add(cursor.getString(0) + "-" + cursor.getString(1));
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + " Advertencia: no hay clientes guardados."); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Advertencia: no hay clientes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
                return listaClientes;
        }

        public ArrayList<String> selectCalibres(Context context) {
                ArrayList<String> listaCalibres = new ArrayList<>();
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor;
                        cursor = sqLiteDatabase.rawQuery("select cl.calibre from calibre as cl", null);
                        if (cursor.moveToFirst()) {
                                do {
                                        listaCalibres.add(cursor.getString(0));
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + " Advertencia: no hay calibres guardados."); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Advertencia: no hay clientes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
                return listaCalibres;
        }

        public ArrayList<MdPesoCaja> selectPesoCaja(Context context, String fecha_desde, String fecha_hasta, boolean esFechaUnica) {
                ArrayList<MdPesoCaja> listaPesoCaja = new ArrayList<>();
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor;
                        if (esFechaUnica) {
                                cursor = sqLiteDatabase.rawQuery("select pc.fecha as 'Fecha', pc.peso as 'Peso Caja', ci.nombre as 'Cliente', pc.calibre as 'Calibre', us.nombre as 'Usuario', pc.observacion as 'Detalle' \n" +
                                        "from pesocaja as pc \n" +
                                        "inner join usuario as us on pc.dni_encargado = us.dni \n" +
                                        "inner join cliente as ci on pc.cliente = ci.code \n" +
                                        "where pc.fecha = ?", new String[]{fecha_desde});
                        } else {
                                cursor = sqLiteDatabase.rawQuery("select pc.fecha as 'Fecha', pc.peso as 'Peso Caja', ci.nombre as 'Cliente', pc.calibre as 'Calibre', us.nombre as 'Usuario', pc.observacion as 'Detalle' \n" +
                                        "from pesocaja as pc \n" +
                                        "inner join usuario as us on pc.dni_encargado = us.dni \n" +
                                        "inner join cliente as ci on pc.cliente = ci.code \n" +
                                        "where pc.fecha >= ? and pc.fecha <= ?", new String[]{fecha_desde, fecha_hasta});
                        }
                        if (cursor.moveToFirst()) {
                                do {
                                        MdPesoCaja mdpesoCaja = new MdPesoCaja();
                                        mdpesoCaja.setFecha(cursor.getString(0));
                                        mdpesoCaja.setCedula(cursor.getString(4));
                                        mdpesoCaja.setCliente(cursor.getString(2));
                                        mdpesoCaja.setCalibre(cursor.getString(3));
                                        mdpesoCaja.setPeso(cursor.getString(1));
                                        mdpesoCaja.setObservacion(cursor.getString(5));
                                        listaPesoCaja.add(mdpesoCaja);
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + " Advertencia: no hay reportes guardados."); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Advertencia: no hay reportes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
                return listaPesoCaja;
        }

        public boolean selectLocalMode(Context context) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor;
                        cursor = sqLiteDatabase.rawQuery("select modo from localmode", null);
                        if (cursor.moveToFirst()) {
                                if (cursor.getInt(0) == 0) {
                                        return false;
                                } else {
                                        if (cursor.getInt(0) == 1) return true;
                                }
                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + " Advertencia: modo almacenamiento definido."); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(context, "Advertencia: no hay clientes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + sqle);
                        Toast.makeText(context, "Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
                return false;
        }
}