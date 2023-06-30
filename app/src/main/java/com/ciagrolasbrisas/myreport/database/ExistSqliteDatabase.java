package com.ciagrolasbrisas.myreport.database;

import java.io.File;

public class ExistSqliteDatabase {
    private final File DB_FILE = new File("/data/data/com.ciagrolasbrisas.myreport/databases/reporte.db");
    public ExistSqliteDatabase(){
    }

    public boolean ExistSqliteDatabase(){
        return DB_FILE.exists();
    }
}