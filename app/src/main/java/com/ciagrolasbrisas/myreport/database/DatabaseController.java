package com.ciagrolasbrisas.myreport.database;

import static android.util.Log.i;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ciagrolasbrisas.myreport.controller.GetConsecutive;
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
        public void insertDefaultCuelloBotella(Context context) {
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
                                i("MyReport", "DataBaseController/Error al guardar lista cuellos de botella por defecto.");
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseController/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }

        public void insertDefaultUsuario(Context context) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        long resultado = -1;

                        HashMap<String, String[]> datosUsuarios = new HashMap<>();
                        datosUsuarios.put("cedulas", new String[]{"503610263", "37", "38", "39", "40", "41", "503970765", "206200609",  "206040225"});
                        datosUsuarios.put("password", new String[]{"123", "lkjh37", "38lkjh", "39asdf", "asdf40", "zxcv41", "Claudia", "Dayana", "Felix"});
                        datosUsuarios.put("nombre", new String[]{"Michael Busto L", "Cuadrilla 37", "Cuadrilla 38", "Cuadrilla 39", "Cuadrilla 40", "Cuadrilla 41", "Planta", "Planta", "Cosecha"});
                        datosUsuarios.put("depto", new String[]{"Admin", "Cosecha", "Cosecha", "Cosecha", "Cosecha", "Cosecha", "Claudia Barrios P", "Dayana Ruiz H",  "Cosecha"});

                        for (int i = 0; i < datosUsuarios.get("cedulas").length; i++) {
                                ContentValues values = new ContentValues();
                                values.put("code", (i + 1));
                                values.put("dni", datosUsuarios.get("cedulas")[i]);
                                values.put("password", datosUsuarios.get("password")[i]);
                                values.put("nombre", datosUsuarios.get("nombre")[i]);
                                values.put("departamento", datosUsuarios.get("depto")[i]);
                                resultado = sqLiteDatabase.insert("usuario", null, values);
                        }

                        if (resultado == -1) {
                                i("MyReport", "DataBaseController/Error al guardar lista de usuarios por defecto.");
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseController/Error" + npe, Toast.LENGTH_LONG).show();
                }

        }


        public void insertDefaultCliente(Context context) {
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
                                i("MyReport", "DataBaseController/Error al guardar lista de clientes.");
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseController/Error" + npe, Toast.LENGTH_LONG).show();
                }

        }

        public void insertDefaultCalibre(Context context) {
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
                                i("MyReport", "DataBaseController/Error al guardar lista de calibres.");
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseController/Error" + npe, Toast.LENGTH_LONG).show();
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
                        int code = getConsecutive.funGetConsecutive(context,"cuellobotella");

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

                        if (resultado != -1)
                                i("MyReport", "DataBaseController/Error al guardar el muestreo de premaduraciones");

                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseController/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }

        public void nuevoRptCuelloBotella(Context context, MdCuelloBotella muestra) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        getConsecutive = new GetConsecutive();
                        int code = getConsecutive.funGetConsecutive(context,"cuellobotella");

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
                                Toast.makeText(context, "DataBaseController/Reporte guardado exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                Toast.makeText(context, "DataBaseController/Error al guardar reporte: " + resultado + " Code " + muestra.getCode(), Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseController/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }

        public void nuevoUsuario(Context context, MdUsuario usuario) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        int code = getConsecutive.funGetConsecutive(context, "usuario");
                        values.put("code", code);
                        values.put("dni", usuario.getId());
                        values.put("password", usuario.getPass());
                        values.put("nombre",usuario.getNombre());
                        values.put("departamento",usuario.getDepartamento());

                        long resultado = sqLiteDatabase.insert("usuario", null, values);

                        if (resultado != -1) {
                                Toast.makeText(context, "DataBaseController/Usuario guardado exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                Toast.makeText(context, "DataBaseController/Error al guardar el usuario: " + resultado + " Code " + usuario.getId(), Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseController/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }

        public void nuevoPesoCaja(Context context, MdPesoCaja pesoCaja) {
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        getConsecutive = new GetConsecutive();
                        int code = getConsecutive.funGetConsecutive(context,"pesocaja");

                        values.put("code", code);
                        values.put("fecha", pesoCaja.getFecha());
                        values.put("dni_encargado", pesoCaja.getDni_encargado());
                        values.put("cliente", pesoCaja.getCliente());
                        values.put("calibre", pesoCaja.getCalibre());
                        values.put("peso", pesoCaja.getPeso());
                        values.put("observacion", pesoCaja.getObservacion());
                        values.put("hora_captura", pesoCaja.getHora_captura());

                        long resultado = sqLiteDatabase.insert("pesocaja", null, values);

                        if (resultado != -1) {
                                Toast.makeText(context, "DataBaseController/Peso guardado exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                Toast.makeText(context, "DataBaseController/Error al guardar: " + resultado, Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        i("MyReport", "DataBaseController/Error: " + npe.getMessage());
                        Toast.makeText(context, "DataBaseController/Error: " + npe, Toast.LENGTH_LONG).show();
                }
        }

        /*---------------------------------------------------Verifica existencia de usuarios-------------------------------------------------------------------------------------------*/
        public boolean existUsersData(Context context){
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select count(*)  from usuario ", null);
                        if(cursor.moveToFirst()){
                                return !cursor.getString(0).equals("0");
                        } else {
                                return  false;
                        }
                }  catch (IllegalArgumentException e) {
                        i("MyReport: DataBaseController", e.getMessage());
                        return false;
                }
        }

        /*---------------------------------------------------Verifica existencia de jornada especifica-------------------------------------------------------------------------*/
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
                        i("MyReport: DataBaseController", e.getMessage());
                        return false;
                }
        }

        /*---------------------------------------------------Listar registros-------------------------------------------------------------------------------------------------------------------*/
        public boolean loginUser(Context context, @NonNull MdUsuario usuario) {
                dbHelper = new DatabaseHelper(context);
                sqLiteDatabase = dbHelper.getReadableDatabase();
                @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select nombre from usuario where dni = ? and password = ?", new String[]{usuario.getId(), usuario.getPass()});
                if (cursor.moveToFirst()) {
                        nombreUsuario = cursor.getString(0);
                        return  true;
                }
                return false;
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
                                Toast.makeText(context, "DataBaseController/Advertencia: no hay reportes abiertos", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle, Toast.LENGTH_LONG).show();
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
                                Toast.makeText(context, "DataBaseController/Advertencia: no hay reportes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle, Toast.LENGTH_LONG).show();
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

        public ArrayList<String> selectClientesCajas(Context context) {
                ArrayList<String> listaClientes = new ArrayList<>();
                try {
                        dbHelper = new DatabaseHelper(context);
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        Cursor cursor;
                        cursor = sqLiteDatabase.rawQuery("select cl.code, cl.nombre \n" +
                                "from cliente as cl", null);

                        if (cursor.moveToFirst()) {
                                do {
                                        listaClientes.add(cursor.getString(0)+ "-" + cursor.getString(1));
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                Toast.makeText(context, "DataBaseController/Advertencia: no hay clientes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle, Toast.LENGTH_LONG).show();
                }
                return listaClientes;
        }

        public ArrayList<String> selectCalibres(Context context) {
                ArrayList<String> listaCalibres = new ArrayList<>();
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
                                Toast.makeText(context, "DataBaseController/Advertencia: no hay clientes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle, Toast.LENGTH_LONG).show();
                }
                return listaCalibres;
        }


        public ArrayList<MdPesoCaja> selectPesoCaja(Context context,  String fecha_desde, String fecha_hasta, boolean esFechaUnica) {
                ArrayList<MdPesoCaja> listaPesoCaja = new ArrayList<>();
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
                                        mdpesoCaja.setDni_encargado(cursor.getString(4));
                                        mdpesoCaja.setCliente(cursor.getString(2));
                                        mdpesoCaja.setCalibre(cursor.getString(3));
                                        mdpesoCaja.setPeso(cursor.getString(1));
                                        mdpesoCaja.setObservacion(cursor.getString(5));
                                        listaPesoCaja.add(mdpesoCaja);
                                } while (cursor.moveToNext());
                                cursor.close();
                        } else {
                                Toast.makeText(context, "DataBaseController/Advertencia: no hay reportes guardados", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle, Toast.LENGTH_LONG).show();
                }
                return listaPesoCaja;
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
                                Toast.makeText(context, "DataBaseController/Datos actualizados exitosamente!", Toast.LENGTH_LONG).show();
                        } else {
                                Toast.makeText(context, "DataBaseController/Error al actualizar información!", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(context, "DataBaseController/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException npe) {
                        Toast.makeText(context, "DataBaseController/Error" + npe, Toast.LENGTH_LONG).show();
                }
        }
}