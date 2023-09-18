package com.ciagrolasbrisas.myreport.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.ciagrolasbrisas.myreport.database.DatabaseHelper;

public class GetConsecutive {
    public GetConsecutive(){

    }

    public int funGetConsecutive(Context context, String nombreTabla){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int code = 0;
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(String.format("select * from %s SQLITE_SEQUENSE", nombreTabla), null);
            if (cursor.moveToFirst()) {
                code = cursor.getInt(0) + 1;
            } else {
                return 1;
            }
        } catch (SQLiteException sqle) {
            Toast.makeText(context, "GetConsecutive/Error: " + sqle, Toast.LENGTH_LONG).show();
        }
        return code;
    }
}
