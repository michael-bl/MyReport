package com.ciagrolasbrisas.myreport.controller;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogGenerator {

        public LogGenerator() {

        }

        public boolean generateLogFile(String logString) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File myFile = new File(path, "Logs.txt");

                try {
                        if (!myFile.exists()) {
                                myFile.createNewFile();
                        }

                        // Abre el archivo en modo de escritura (acumulativo)
                        FileWriter writer = new FileWriter(myFile, true);

                        writer.append(logString);
                        writer.append("\n"); // Agrega una nueva línea después del contenido

                        writer.close(); // Cierra el archivo
                } catch (IOException e) {
                        e.printStackTrace();
                }

                return false;
        }
}