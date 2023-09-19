package com.ciagrolasbrisas.myreport.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.ui.VwLogin;
import com.ciagrolasbrisas.myreport.ui.VwMain;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileGenerator {
        ArrayList<MdCuelloBotella> listacb;
        public FileGenerator(){}

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public <T> FileGenerator(Context context, List<T> list) {

                try {
                        if (!VwLogin.dniUsuario.equals( "206040225")){
                                ExcelGenerator crearExcel = new ExcelGenerator();

                                listacb = (ArrayList<MdCuelloBotella>) list;
                                crearExcel.generarExcell(listacb, context);

                                String rutaArchivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+ "rpt_cuellobotella.xlsx";
                                File archivoExcel = new File(rutaArchivo);

                                if (archivoExcel.exists()){
                                        Toast.makeText(context, "Archivo 'rpt_cuellobotella' guardado 'DOWNLOADS/DESCARGAS' ", Toast.LENGTH_LONG).show();
                                }
                        } else {
                                // este bloque se ejecuta unicamente cuando el usuario sea Felix Martínez
                                ArrayList<JSONObject> json_array = new ArrayList<>();
                                for (MdCuelloBotella cb: listacb){
                                        JSONObject datos_json = new JSONObject();
                                        datos_json.put("Encargado", cb.getDniEncargado());
                                        datos_json.put("Fecha", cb.getFecha());
                                        datos_json.put("Motivo", cb.getMotivo());
                                        datos_json.put("Lote", cb.getLote());
                                        datos_json.put("Sección", cb.getSeccion());
                                        datos_json.put("Hora_Inicio", cb.getHora_inicio());
                                        datos_json.put("Hora_Final", cb.getHora_final());
                                        json_array.add(datos_json);
                                }

                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TEXT, json_array.toString());
                                context.startActivity(intent);
                        }
                } catch (Exception e) {
                        Toast.makeText(context, "Error de ejecución: " + e, Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(context.getApplicationContext(), VwMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                //finish(); // finaliza la actividad actual
        }
}
