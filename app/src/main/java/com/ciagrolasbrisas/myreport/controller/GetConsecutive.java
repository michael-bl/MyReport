package com.ciagrolasbrisas.myreport.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.ciagrolasbrisas.myreport.database.DatabaseHelper;

public class GetConsecutive {
    private String date, time;
    private LogGenerator logGenerator;
    public GetConsecutive(){

    }

    public int funGetConsecutive(Context context, String nombreTabla){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int code = 0;

        logGenerator = new LogGenerator();
        GetStringDate stringDate = new GetStringDate();
        GetStringTime stringTime = new GetStringTime();
        date = stringDate.getFecha();
        time = stringTime.getHora();

        try {
            Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT COUNT(*) FROM %s", nombreTabla), null);
            if (cursor.moveToFirst()) {
                do{
                    code = cursor.getInt(0) + 1;
                } while (cursor.moveToNext());
            } else {
                return 1;
            }
        } catch (SQLiteException sqle) {
            logGenerator.generateLogFile(date + ": " + time + ": " + this + ": "  + new Throwable().getStackTrace()[0].getMethodName() + ": " + sqle); // Agregamos el error al archivo Descargas/Logs.txt
            Toast.makeText(context, "GetConsecutive/Error: " + sqle.getMessage(), Toast.LENGTH_LONG).show();
        }
        return code;
    }
}
