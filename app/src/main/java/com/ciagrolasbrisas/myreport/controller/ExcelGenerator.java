package com.ciagrolasbrisas.myreport.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.model.MdPesoCaja;
import com.ciagrolasbrisas.myreport.ui.VwLogin;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelGenerator {
        private Workbook workbook;
        private Sheet sheet;
        private Cell headerCell, cell;
        private Row row, headerRow;
        private Set<Integer> keyset;
        private Object[] arregloObjetos;
        private Map<Integer, Object[]> data;

        private ArrayList<MdCuelloBotella> listCuelloBotella;
        private ArrayList<MdPesoCaja> listPesoCaja;
        private LogGenerator logGenerator;
        private String date, time;

        public ExcelGenerator() {
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public <T> void generarExcell(Context context, @NonNull List<T> list) {

                //GetStringDate stringDate = new GetStringDate();
                //GetStringTime stringTime = new GetStringTime();
                date = new GetStringDate().getFecha();
                time = new GetStringTime().getHora();

                logGenerator = new LogGenerator();

                if (list.size() != 0) {

                        /* Objetos util para cualquier tipo de reporte */
                        // Nuevo libro de Excel
                        workbook = new XSSFWorkbook();
                        // Nueva hoja de trabajo
                        sheet = workbook.createSheet("Datos");
                        // Crear el encabezado de las columnas
                        headerRow = sheet.createRow(0);

                        T objt = list.get(0);
                        switch (objt.getClass().getSimpleName()) {
                                case "MdCuelloBotella":

                                        try {
                                                if (!VwLogin.dniUser.equals( "206040225")){

                                                        listCuelloBotella = (ArrayList<MdCuelloBotella>) list;
                                                        String[] headersCuelloB = {"Encargado", "Fecha", "Motivo", "Lote", "Sección", "Hora Inicio", "Hora Final"};

                                                        // Generamos las cabeceras
                                                        makeHeader(headersCuelloB);

                                                        // Creando el objeto con los datos de la lista de cuellos de botella, el parametro indica el tipo de
                                                        // objeto a mapear
                                                        fillTrymapData(1);

                                                        // Iterar sobre datos para escribir en la hoja
                                                        writeOnSheet();

                                                        // creando el archivo en la carpeta descargas
                                                        writeFileOnPath(context, "rpt_cuellobotella.xlsx");

                                                } else {
                                                        // este bloque se ejecuta unicamente cuando el usuario sea Felix Martínez
                                                        ArrayList<JSONObject> json_array = new ArrayList<>();
                                                        for (MdCuelloBotella cb: listCuelloBotella){
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
                                                Toast.makeText(context, "ExcelGenerator: error de ejecución: " + e, Toast.LENGTH_LONG).show();
                                                logGenerator.generateLogFile(date + ": " + time + ": " + e.getMessage()); // agregamos el error al archivo Logs.txt
                                        }

                                        break;

                                case "MdPesoCaja":
                                        listPesoCaja = (ArrayList<MdPesoCaja>) list;
                                        String[] headersPesoC = {"'Fecha'", "'Peso Caja", "'Cliente'", "'Calibre'", "'Usuario'", "'Detalle'"};

                                        // Generamos las cabeceras
                                        makeHeader(headersPesoC);

                                        // Creando el objeto con los datos de la lista de cuellos de botella, el parametro indica el tipo de
                                        // objeto a mapear
                                        fillTrymapData(2);

                                        // Iterar sobre datos para escribir en la hoja
                                        writeOnSheet();

                                        // Creando el archivo en la carpeta descargas
                                        writeFileOnPath(context, "rpt_pesocaja.xlsx");
                                        break;
                        }

                } else {
                        Toast.makeText(context, "Error: no seleccionaste datos para descargar!", Toast.LENGTH_LONG).show();
                }
        }

        private void writeFileOnPath(Context cont, String reportName) {
                try {
                        // Directorio donde se almacenara el archivo
                        File directorioAlmacenamiento = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                        // Crea una referencia al archivo en el directorio de almacenamiento
                        File archivo = new File(directorioAlmacenamiento, reportName);

                        // Bytes de salida
                        FileOutputStream outputStream = new FileOutputStream(archivo);
                        workbook.write(outputStream);
                        outputStream.close();
                        Toast.makeText(cont, "El archivo de Excel ha sido generado exitosamente.", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + e.getMessage()); // agregamos el error al archivo Logs.txt
                        Toast.makeText(cont, "Error al crear el archivo: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        private void writeOnSheet() {
                keyset = data.keySet();
                int numeroRenglon = 1;
                for (int key : keyset) {
                        row = sheet.createRow(numeroRenglon++);
                        arregloObjetos = data.get(key);
                        int numeroCelda = 0;
                        assert arregloObjetos != null;
                        for (Object obj : arregloObjetos) {
                                cell = row.createCell(numeroCelda++);
                                if (obj instanceof String) {
                                        cell.setCellValue((String) obj);
                                } else if (obj instanceof Integer) {
                                        cell.setCellValue((Integer) obj);
                                }
                        }
                }
        }

        private void fillTrymapData(int flag) {
                switch (flag) {
                        case 1:
                                data = new TreeMap<>();
                                for (int x = 0; x < listCuelloBotella.size(); x++) {
                                        // Creando el objeto con los datos recibidos
                                        data.put(x + 1, new Object[]{
                                                listCuelloBotella.get(x).getDniEncargado(),
                                                listCuelloBotella.get(x).getFecha(),
                                                listCuelloBotella.get(x).getMotivo(),
                                                listCuelloBotella.get(x).getLote(),
                                                listCuelloBotella.get(x).getSeccion(),
                                                listCuelloBotella.get(x).getHora_inicio(),
                                                listCuelloBotella.get(x).getHora_final()});
                                }
                                break;

                        case 2:
                                data = new TreeMap<>();
                                for (int x = 0; x < listPesoCaja.size(); x++) {
                                        // Creando el objeto con los datos recibidos
                                        data.put(x + 1, new Object[]{
                                                listPesoCaja.get(x).getFecha(),
                                                listPesoCaja.get(x).getPeso(),
                                                listPesoCaja.get(x).getCliente(),
                                                listPesoCaja.get(x).getCalibre(),
                                                listPesoCaja.get(x).getDni_encargado(),
                                                listPesoCaja.get(x).getObservacion()});
                                }
                                break;
                }
        }

        private void makeHeader(@NonNull String[] headersCuelloB) {
                // Genera celdas para cada columna del header
                for (int i = 0; i < headersCuelloB.length; i++) {
                        headerCell = headerRow.createCell(i);
                        headerCell.setCellValue(headersCuelloB[i]);
                }
        }
}
