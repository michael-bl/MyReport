package com.ciagrolasbrisas.myreport.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import java.util.ArrayList;

public class ReadControllerDb {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public ReadControllerDb(){

    }

    public ArrayList<String> getDataList(Context context, String tableName){
            ArrayList<String> lista = new ArrayList<>();
            try {
                dbHelper = new DatabaseHelper(context);
                sqLiteDatabase = dbHelper.getWritableDatabase();
                Cursor cursor;
                cursor = sqLiteDatabase.rawQuery("select * from " + tableName, null);

                if (cursor.moveToFirst()) {
                    do {
                        lista.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                    cursor.close();
                } else {
                    Toast.makeText(context, "No hay datos en la tabla " + tableName, Toast.LENGTH_LONG).show();
                }
            } catch (SQLiteException sqle) {
                Toast.makeText(context, "DataBaseHelper/Error: " + sqle, Toast.LENGTH_LONG).show();
            }
            return lista;
        }
}
