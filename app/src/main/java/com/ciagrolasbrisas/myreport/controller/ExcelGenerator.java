package com.ciagrolasbrisas.myreport.controller;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelGenerator {

        public ExcelGenerator(){}

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void generarExcell(@NonNull ArrayList<MdCuelloBotella> listCuelloBotella, Context context){
                if(listCuelloBotella.size()!=0){
                        // Nuevo libro de Excel
                        Workbook workbook = new XSSFWorkbook();

                        // Nueva hoja de trabajo
                        Sheet sheet = workbook.createSheet("Datos");

                        // Crear el encabezado de las columnas
                        Row headerRow = sheet.createRow(0);
                        String[] headers = {"Encargado", "Fecha", "Motivo", "Lote", "Sección", "Hora Inicio", "Hora Final"};
                        for (int i = 0; i < headers.length; i++) {
                                Cell cell = headerRow.createCell(i);
                                cell.setCellValue(headers[i]);
                        }

                        // Creando el objeto con los datos de la lista de cuellos de botella
                        Map<Integer, Object[]> data = new TreeMap<>();
                        for (int x=0; x<listCuelloBotella.size();x++){
                                // Creando el objeto con los datos recibidos
                                data .put(x+1, new Object[]{
                                        listCuelloBotella.get(x).getDniEncargado() ,
                                        listCuelloBotella.get(x).getFecha(),
                                        listCuelloBotella.get(x).getMotivo(),
                                        listCuelloBotella.get(x).getLote(),
                                        listCuelloBotella.get(x).getSeccion(),
                                        listCuelloBotella.get(x).getHora_inicio(),
                                        listCuelloBotella.get(x).getHora_final() });
                        }

                        // Iterar sobre datos para escribir en la hoja
                        Set<Integer> keyset = data.keySet();
                        int numeroRenglon = 1;
                        for (int key : keyset) {
                                Row row = sheet.createRow(numeroRenglon++);
                                Object[] arregloObjetos = data.get(key);
                                int numeroCelda = 0;
                                assert arregloObjetos != null;
                                for (Object obj : arregloObjetos) {
                                        Cell cell = row.createCell(numeroCelda++);
                                        if (obj instanceof String) {
                                                cell.setCellValue((String) obj);
                                        } else if (obj instanceof Integer) {
                                                cell.setCellValue((Integer) obj);
                                        }
                                }
                        }


                        try {
                                // CREAR METODO PARA AGREGAR EL NOMBRE DEL ENCARGADO Y FECHA DE LOS DATOS EN EL NOMBRE DEL ARCHIVO
                                File directorioAlmacenamiento = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                                // Crea una referencia al archivo en el directorio de almacenamiento
                                File archivo = new File(directorioAlmacenamiento, "rpt_cuellobotella.xlsx");

                                // Bytes de salida
                                FileOutputStream outputStream = new FileOutputStream(archivo);
                                workbook.write(outputStream);
                                outputStream.close();

                                Toast.makeText(context, "El archivo de Excel ha sido generado exitosamente.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                                Toast.makeText(context, "Error al crear el archivo: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                } else {
                        Toast.makeText(context, "Error: no seleccionaste datos para descargar!", Toast.LENGTH_LONG).show();
                }
        }
}
