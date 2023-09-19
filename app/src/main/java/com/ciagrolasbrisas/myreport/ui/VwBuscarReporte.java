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
import com.ciagrolasbrisas.myreport.model.MdPesoCaja;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class VwBuscarReporte extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
        private Button btnBuscar, btnFechaDesde, btnFechaHasta;
        private ArrayList<MdCuelloBotella> listaCuelloBotella;
        private ArrayList<MdPesoCaja> listaPesoCaja;
        private DatabaseController dbController;
        private ArrayList<String> stringArrayList;
        private TextView tvtFecha1, tvtFecha2;
        private String fechaDesde, fechaHasta, opcion;
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
                listaCuelloBotella = new ArrayList<>();
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
                         opcion = tipoReporte.substring(0,1);

                        existDb = new ExistSqliteDatabase();
                        if (existDb.ExistSqliteDatabase()) {

                                switch (opcion) {
                                        case "1":
                                                dbController = new DatabaseController();
                                                listaCuelloBotella = dbController.selectCuelloBotella(this, fechaDesde, fechaHasta, chekRangoFecha.isChecked());
                                                llenarListViewReporte(1);
                                                break;
                                        case "2":
                                                Toast.makeText(this, "VwBuscarReporte.class/Opción en desarrollo!", Toast.LENGTH_LONG).show();
                                                break;
                                        case "3":
                                                Toast.makeText(this, "VwBuscarReporte.class/Opción en desarrollo", Toast.LENGTH_LONG).show();
                                                break;
                                        case "4":
                                                dbController = new DatabaseController();
                                                listaPesoCaja = dbController.selectPesoCaja(this, fechaDesde, fechaHasta, chekRangoFecha.isChecked());
                                                llenarListViewReporte(4);
                                                break;
                                }
                        }
                });

                btnShare.setOnClickListener(view -> {
                        try {
                                if (!VwLogin.dniUsuario.equals( "206040225")){
                                        ExcelGenerator crearExcel = new ExcelGenerator();
                                        crearExcel.generarExcell(listaCuelloBotella, this);
                                        String rutaArchivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+ "rpt_cuellobotella.xlsx";
                                        File archivoExcel = new File(rutaArchivo);
                                        if (archivoExcel.exists()){
                                                Toast.makeText(this, "Archivo 'rpt_cuellobotella' guardado 'DOWNLOADS/DESCARGAS' ", Toast.LENGTH_LONG).show();
                                        }
                                } else {
                                        // este bloque se ejecuta unicamente cuando el usuario sea Felix Martínez
                                        ArrayList<JSONObject> json_array = new ArrayList<>();
                                        for (MdCuelloBotella cb: listaCuelloBotella){
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
                                        startActivity(intent);
                                }
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
                String tiposReporte[] = {"1-Cuello Botella Cosecha", "2-Carretas Cosecha", "3-Premaduraciones", "4-Pesos de Cajas" };
                ArrayAdapter adapter;

                try {
                        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tiposReporte);
                        spTipoReporte.setAdapter(adapter);
                } catch (Exception e){
                        Toast.makeText(this, "VwBuscarReporte.class/Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        private void llenarListViewReporte(int opcion) {
                try {
                        stringArrayList = new ArrayList<>();
                        switch (opcion){
                                case 1:
                                        for (MdCuelloBotella cb: listaCuelloBotella) {
                                                stringArrayList.add(cb.getMotivo() +"   "+ cb.getHora_inicio() +"   "+ cb.getHora_final());
                                        }
                                break;
                                        // 2
                                        //3
                                case 4:
                                        for (MdPesoCaja pc: listaPesoCaja) {
                                                stringArrayList.add(pc.getFecha() +"   "+ pc.getPeso() +"   "+ pc.getCliente()+"   "+ pc.getCalibre()+"   "+ pc.getDni_encargado()+"   "+ pc.getObservacion());
                                        }
                                        break;
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