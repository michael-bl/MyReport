package com.ciagrolasbrisas.myreport.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

        private final Context myContext;
        private static int databaseVersion = 1;
        private static final String databaseName = "reporte.db";

        private final String tblUsuario = "CREATE TABLE usuario (" +
                "code INTEGER NOT NULL UNIQUE," +
                "dni VARCHAR(9)," +
                "password TEXT(15)," +
                "nombre TEXT(30)," +
                "departamento TEXT(30)," +
                "rol TEXT(10)," +
                " PRIMARY KEY (dni));";

        private final String tblPremas = "CREATE TABLE premaduracion (" +
                " id_muestreo  INTEGER UNIQUE NOT NULL," +
                " grupo_forza  VARCHAR(7)," +
                " fecha_muestreo  VARCHAR(10)," +
                " ciclo  VARCHAR(1)," +
                " lote  VARCHAR(4)," +
                " seccion  VARCHAR(2)," +
                " tamanio  VARCHAR(1)," +
                " brix  VARCHAR(4)," +
                " traslucidez  VARCHAR(4)," +
                " ibd  VARCHAR(1)," +
                " tipo  VARCHAR(1)," +
                " muestreo  VARCHAR(1)," +
                " PRIMARY KEY (id_muestreo, grupo_forza, fecha_muestreo, ciclo, lote, seccion, tamanio));";

        private final String tblForza = "CREATE TABLE forza (" +
                " id_forza INTEGER NOT NULL UNIQUE," +
                " grupo_forza VARCHAR(7)," +
                " fecha_forza VARCHAR(10)," +
                " ciclo VARCHAR(1)," +
                " lote VARCHAR(4)," +
                " seccion VARCHAR(2)," +
                " area VARCHAR(5)," +
                " PRIMARY KEY (id_forza, grupo_forza, fecha_forza, ciclo, lote, seccion));";

        // El campo motivo es un FK la base del servidor referente a tabla de cuellos de botella
        private final String tblCuelloBotella = "CREATE TABLE cuellobotella (" +
                " code INTEGER NOT NULL UNIQUE," +
                " fecha VARCHAR(10)," +
                " dni_encargado VARCHAR(9)," +
                " lote VARCHAR(4)," +
                " seccion VARCHAR(2)," +
                " motivo VARCHAR(2)," +
                " hora_inicio VARCHAR(11)," +
                " hora_final VARCHAR(11)," +
                " PRIMARY KEY (code, fecha, dni_encargado));";

        private final String tblMotivoCuelloBotella = "CREATE TABLE motivocb (" +
                " code INTEGER NOT NULL UNIQUE," +
                " motivo TEXT," +
                " PRIMARY KEY (code));";

        // Consecutivo = cantidad de carretas cosechadas del dia
        // Encargado = Nombre del encargado de cuadrilla
        private final String tblTicketCosecha = "CREATE TABLE ticketcosecha (" +
                " code INTEGER NOT NULL UNIQUE," +
                " encargado VARCHAR(2)," +
                " fecha VARCHAR(10)," +
                " hora VARCHAR(8)," +
                " consecutivo INTEGER(2)," +
                " tipo_cosecha VARCHAR(4)," +
                " tipo_pase VARCHAR(4)," +
                " gp_forza VARCHAR(4)," +
                " ciclo VARCHAR(1)," +
                " lote VARCHAR(4)," +
                " seccion VARCHAR(2)," +
                " frutas INTEGER(5)," +
                " PRIMARY KEY (code));";

        private final String tblPesoCaja = "CREATE TABLE pesocaja (" +
                " code INTEGER NOT NULL UNIQUE," +
                " fecha VARCHAR(10)," +
                " dni_encargado VARCHAR(9)," +
                " cliente VARCHAR(2)," +
                " calibre VARCHAR(2)," +
                " peso VARCHAR(5)," +
                " observacion VARCHAR(30)," +
                " hora_captura VARCHAR(8)," +
                " PRIMARY KEY (code, fecha, dni_encargado, hora_captura));";

        private final String tblCliente = "CREATE TABLE cliente (" +
                " code INTEGER NOT NULL UNIQUE," +
                " nombre VARCHAR(22)," +
                " PRIMARY KEY (code));";

        private final String tblCalibre = "CREATE TABLE calibre  (" +
                " calibre VARCHAR(12) NOT NULL UNIQUE," +
                " PRIMARY KEY (calibre));";

        private final String tblLocalMode = "CREATE TABLE localmode  (" +
                " code INTEGER NOT NULL UNIQUE," +
                " modo INTEGER," +
                " PRIMARY KEY (modo));";

        public DatabaseHelper(@Nullable Context context) {
                super(context, databaseName, null, databaseVersion);
                myContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDb) {
                try {
                        sqLiteDb.execSQL(tblUsuario);
                        sqLiteDb.execSQL(tblPremas);
                        sqLiteDb.execSQL(tblForza);
                        sqLiteDb.execSQL(tblCuelloBotella);
                        sqLiteDb.execSQL(tblMotivoCuelloBotella);
                        sqLiteDb.execSQL(tblTicketCosecha);
                        sqLiteDb.execSQL(tblPesoCaja);
                        sqLiteDb.execSQL(tblCliente);
                        sqLiteDb.execSQL(tblCalibre);
                        sqLiteDb.execSQL(tblLocalMode);
                } catch (SQLiteException sqle) {
                        Toast.makeText(myContext, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDb, int i, int i1) {
                try {
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS usuario");
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS premaduracion");
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS forza");
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS cuellobotella");
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS motivocb");
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS ticketcosecha");
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS pesocaja");
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS cliente");
                        sqLiteDb.execSQL("DROP TABLE IF EXISTS calibre");
                        onCreate(sqLiteDb);
                } catch (SQLiteException sqle) {
                        Toast.makeText(myContext, "DataBaseHelper/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

}
