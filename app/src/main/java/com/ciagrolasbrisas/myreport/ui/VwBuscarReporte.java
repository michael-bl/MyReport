package com.ciagrolasbrisas.myreport.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ExcelGenerator;
import com.ciagrolasbrisas.myreport.controller.MyDatePicker;
import com.ciagrolasbrisas.myreport.controller.SelectionAdapter;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.database.ExistSqliteDatabase;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class VwBuscarReporte extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
        private Button btnBuscar, btnFechaDesde, btnFechaHasta;
        private ArrayList<MdCuelloBotella> listaCuellosBotella;
        private DatabaseController dbController;
        private ArrayList<String> stringArrayList;
        private TextView tvtFecha1, tvtFecha2;
        private String fechaDesde, fechaHasta;
        private FloatingActionButton btnShare;
        private SelectionAdapter myAdapter;
        private CheckBox chekRangoFecha;
        private ExistSqliteDatabase existDb;
        private Spinner spTipoReporte;
        private String tipoReporte;
        private ListView lvReports;
        private int btnFechaSeleccionado;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_buscar_reporte);
                chekRangoFecha = findViewById(R.id.checkBoxRangoFecha);
                lvReports = findViewById(R.id.lvDatosReporte);
                tvtFecha1 = findViewById(R.id.tvDesde);
                tvtFecha2 = findViewById(R.id.tvHasta);
                spTipoReporte = findViewById(R.id.spTipoReporte);
                btnBuscar = findViewById(R.id.btnBuscarXfecha);
                btnFechaDesde = findViewById(R.id.btnFecha1);
                btnFechaHasta = findViewById(R.id.btnFecha2);
                btnShare = findViewById(R.id.fabShare);
                chekRangoFecha = findViewById(R.id.checkBoxRangoFecha);
                listaCuellosBotella = new ArrayList<>();
                llenarSpinnerTiposReporte();

                chekRangoFecha.setOnClickListener(view ->{
                        if(chekRangoFecha.isChecked()){
                                btnFechaHasta.setEnabled(false);
                                tvtFecha2.setText(null);
                        } else {
                                btnFechaHasta.setEnabled(true);
                        }
                });

                btnFechaDesde.setOnClickListener(view->{
                        btnFechaSeleccionado = 1;
                        showDatePickerDialog();
                });

                btnFechaHasta.setOnClickListener(view->{
                        btnFechaSeleccionado = 2;
                        showDatePickerDialog();
                });

                btnBuscar.setOnClickListener(view -> {
                         tipoReporte = spTipoReporte.getItemAtPosition(spTipoReporte.getSelectedItemPosition()).toString();
                         fechaDesde = tvtFecha1.getText().toString().trim();
                         fechaHasta = tvtFecha2.getText().toString().trim();
                         String opcion = tipoReporte.substring(0,1);

                        existDb = new ExistSqliteDatabase();
                        if (existDb.ExistSqliteDatabase()) {

                                switch (opcion) {
                                        case "1":
                                                dbController = new DatabaseController();
                                                listaCuellosBotella = dbController.selectCuelloBotella(this, fechaDesde, fechaHasta, chekRangoFecha.isChecked());
                                                llenarListViewCuelloBotella();
                                                break;
                                        case "2":
                                                Toast.makeText(this, "VwBuscarReporte.class/Opción en desarrollo!", Toast.LENGTH_LONG).show();
                                                break;
                                        case "3":
                                                Toast.makeText(this, "VwBuscarReporte.class/Opción en desarrollo", Toast.LENGTH_LONG).show();
                                                break;
                                }
                        }
                });

                btnShare.setOnClickListener(view -> {
                        try {
                                ExcelGenerator crearExcel = new ExcelGenerator();
                                crearExcel.generarExcell(listaCuellosBotella, this);
                                String rutaArchivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+ "rpt_cuellobotella.xlsx";
                                File archivoExcel = new File(rutaArchivo);
                                if (archivoExcel.exists()){
                                        Toast.makeText(this, "Archivo 'rpt_cuellobotella' guardado 'DOWNLOADS/DESCARGAS' ", Toast.LENGTH_LONG).show();
                                }

                                /*     // validar la version del android del dispositivo
                                // menores a api 29 con la linea siguiente
                                // ruta que devuelve el siguiente metodo /storage/emulated/0/Download/rpt_cuellobotella.xlsx
                                //String rutaArchivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+ "rpt_cuellobotella.xlsx";
                                String rutaArchivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() ;
                                // Igual o superior a 29 con la linea siguiente
                                // getExternalFilesDir() averiguar mas acerca de este metodo

                                File archivoExcel = new File(rutaArchivo);
                                if (archivoExcel.exists()) {
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("application/vnd.ms-excel");
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.putExtra(Intent.EXTRA_STREAM, archivoExcel);
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                startActivity(Intent.createChooser(intent, "Seleccione el adjunto a enviar"));
                                        } else {
                                                Toast.makeText(this, "No hay aplicaciones instaladas que puedan manejar la acción SEND", Toast.LENGTH_LONG).show();
                                        }
                                }
                                else {
                                        Toast.makeText(this, "El archivo no existe o no se puede acceder a él", Toast.LENGTH_LONG).show();
                                }*/

                        } catch (Exception e) {
                                Toast.makeText(this, "Error de ejecución: " + e, Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(getApplicationContext(), VwMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                });
        }

        private void llenarSpinnerTiposReporte() {
                String tiposReporte[] = {"1-Cuello Botella Cosecha", "2-Carretas Cosecha", "3-Premaduraciones" };
                ArrayAdapter adapter;

                try {
                        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tiposReporte);
                        spTipoReporte.setAdapter(adapter);
                } catch (Exception e){
                        Toast.makeText(this, "VwBuscarReporte.class/Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        private void llenarListViewCuelloBotella() {
                try {
                        stringArrayList = new ArrayList<>();
                        for (MdCuelloBotella cb: listaCuellosBotella) {
                                stringArrayList.add(cb.getMotivo() +"   "+ cb.getHora_inicio() +"   "+ cb.getHora_final());
                        }

                        lvReports = findViewById(R.id.lvDatosReporte);
                        myAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArrayList);
                        lvReports.setAdapter(myAdapter);
                } catch (NullPointerException npe) {
                        Toast.makeText(this, "Error: " + npe, Toast.LENGTH_LONG).show();
                }
        }

        private void showDatePickerDialog() {
                MyDatePicker datePickerFragment = new MyDatePicker();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");

        }

        @Override
        public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                String stringFecha;
                if (dia < 10) if (mes < 10)
                        stringFecha = "0" + dia + "/" + "0" + (mes+1) + "/" + anio;
                else stringFecha = "0" + dia + "/" + (mes+1) + "/" + anio;
                else if (mes < 10) stringFecha = dia + "/" + "0" + (mes+1) + "/" + anio;
                else stringFecha = dia + "/" + (mes+1) + "/" + anio;

                if (btnFechaSeleccionado==1){
                        tvtFecha1.setText(stringFecha);
                } else {
                        tvtFecha2.setText(stringFecha);
                }
        }
}