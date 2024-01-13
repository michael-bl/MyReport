package com.ciagrolasbrisas.myreport.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.ExcelGenerator;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.controller.MyDatePicker;
import com.ciagrolasbrisas.myreport.controller.SelectionAdapter;
import com.ciagrolasbrisas.myreport.controller.cosecha.CbServidorController;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.database.ExistSqliteDatabase;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.model.MdPesoCaja;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class VwBuscarReporte extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
        private Button btnBuscar, btnFechaDesde, btnFechaHasta;
        private ArrayList<MdCuelloBotella> listaCuelloBotella;
        private ArrayList<MdPesoCaja> listaPesoCaja;
        private DatabaseController dbController;
        private ArrayList<String> stringArrayList;
        private TextView tvtFecha1, tvtFecha2;
        private String fechaDesde, fechaHasta, opcion, tipoReporte, date, time, clase, dniUser;
        private LogGenerator logGenerator;
        private FloatingActionButton btnShare;
        private SelectionAdapter myAdapter;
        private CheckBox checkRangoFecha;
        private ExistSqliteDatabase existDb;
        private Spinner spTipoReporte;
        private ListView lvReports;
        private boolean localMode;
        private MdCuelloBotella mdCuelloBotella;
        private int btnFechaSeleccionado, flagReportType;
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_buscar_reporte);

                lvReports = findViewById(R.id.lvDatosReporte);
                tvtFecha1 = findViewById(R.id.tvDesde);
                tvtFecha2 = findViewById(R.id.tvHasta);
                spTipoReporte = findViewById(R.id.spTipoReporte);
                btnBuscar = findViewById(R.id.btnBuscarXfecha);
                btnFechaDesde = findViewById(R.id.btnFecha1);
                btnFechaHasta = findViewById(R.id.btnFecha2);
                btnShare = findViewById(R.id.fabShare);
                checkRangoFecha = findViewById(R.id.checkBoxRangoFecha);
                listaCuelloBotella = new ArrayList<>();
                mdCuelloBotella = new MdCuelloBotella();
                llenarSpinnerTiposReporte();

                checkRangoFecha.setOnClickListener(view -> {
                        if (checkRangoFecha.isChecked()) {
                                btnFechaHasta.setEnabled(false);
                                tvtFecha2.setText(null);
                        } else {
                                btnFechaHasta.setEnabled(true);
                        }
                });

                btnFechaDesde.setOnClickListener(view -> {
                        btnFechaSeleccionado = 1;
                        showDatePickerDialog();
                });

                btnFechaHasta.setOnClickListener(view -> {
                        btnFechaSeleccionado = 2;
                        showDatePickerDialog();
                });

                btnBuscar.setOnClickListener(view -> {
                        tipoReporte = spTipoReporte.getItemAtPosition(spTipoReporte.getSelectedItemPosition()).toString();
                        fechaDesde = tvtFecha1.getText().toString().trim();
                        fechaHasta = tvtFecha2.getText().toString().trim();
                        opcion = tipoReporte.substring(0, 1);

                        existDb = new ExistSqliteDatabase();
                        if (existDb.ExistSqliteDatabase()) {

                                switch (opcion) {
                                        case "1":
                                                flagReportType = 1;
                                                dbController = new DatabaseController();
                                                localMode = dbController.selectLocalMode(this);

                                                if (localMode) {
                                                        listaCuelloBotella = dbController.selectCuelloBotella(this, fechaDesde, fechaHasta, checkRangoFecha.isChecked());
                                                } else {
                                                        getCBSinCerrarCosDbRemota();
                                                }

                                                break;
                                        case "2":
                                                Toast.makeText(this, "Opción en desarrollo!", Toast.LENGTH_LONG).show();
                                                break;
                                        case "3":
                                                Toast.makeText(this, "Opción en desarrollo", Toast.LENGTH_LONG).show();
                                                break;
                                        case "4":
                                                flagReportType = 4;
                                                dbController = new DatabaseController();
                                                listaPesoCaja = dbController.selectPesoCaja(this, fechaDesde, fechaHasta, checkRangoFecha.isChecked());
                                                for (MdPesoCaja pc : listaPesoCaja) {
                                                        stringArrayList.add(pc.getFecha() + "   " + pc.getPeso() + "   " + pc.getCliente() + "   " + pc.getCalibre() + "   " + pc.getDni_encargado() + "   " + pc.getObservacion());
                                                }
                                                llenarListViewReporte(stringArrayList);
                                                break;
                                }
                        }
                });

                btnShare.setOnClickListener(view -> {
                        ExcelGenerator crearExcel = new ExcelGenerator();
                        String dniUser = "";
                        switch (flagReportType) {
                                case 1:
                                        crearExcel.generarExcell(this, listaCuelloBotella, dniUser);
                                        break;

                                case 4:
                                        crearExcel.generarExcell(this, listaPesoCaja, dniUser);
                                        break;
                        }

                        Intent intent = new Intent(getApplicationContext(), VwMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                });
        }

        private void getCBSinCerrarCosDbRemota() {
                logGenerator = new LogGenerator();
                OkHttpClient client = new OkHttpClient();
                ConnectivityService con = new ConnectivityService();

                if (con.stateConnection(this)) {
                        Map<String, Object> finalJson = new HashMap<>();

                        MdCuelloBotella cb = new MdCuelloBotella();
                        cb.setAccion(1); // Lista los cuellos cerrados en fecha especifica

                        dniUser = dbController.selectDniUser(this);
                        cb.setDniEncargado(dniUser);
                        cb.setFecha(fechaDesde);

                        listaCuelloBotella = new ArrayList<>();
                        listaCuelloBotella.add(cb);

                        finalJson.put("reporte", listaCuelloBotella);  // {"reporte":[{"accion":1,"dniEncargado":"05-0361-0263","fecha":"12/12/2023"}]}

                        String json = new Gson().toJson(finalJson);

                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

                        Request request = new Request.Builder()
                                .url("https://reportes.ciagrolasbrisas.com/cuelloBotellaCos.php")
                                .post(requestBody)
                                .build();

                        // ExecutorService ejecuta la tarea en segundo plano
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                        try {
                                                okhttp3.Response response = client.newCall(request).execute();

                                                if (response.isSuccessful()) {
                                                        final String responseBody = response.body().string();

                                                        // Manejar la respuesta
                                                        mainHandler.post(() -> {
                                                                Gson gson = new Gson();
                                                                Type listType = new TypeToken<List<MdCuelloBotella>>() {
                                                                }.getType();
                                                                listaCuelloBotella = gson.fromJson(responseBody, listType);
                                                                stringArrayList = new ArrayList<>();

                                                                for (MdCuelloBotella cb : listaCuelloBotella) {
                                                                        stringArrayList.add(cb.getMotivo() + "-" + cb.getHora_final());
                                                                }
                                                                llenarListViewReporte(stringArrayList);
                                                        });
                                                } else {
                                                        // Imprimir error en la respuesta
                                                        mainHandler.post(() -> {
                                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + response.message()); // Agregamos el error al archivo Descargas/Logs.txt
                                                                Toast.makeText(VwBuscarReporte.this, "Error en la solicitud: " + response.message(), Toast.LENGTH_SHORT).show();
                                                        });
                                                }
                                        } catch (IOException e) {
                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + e.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                                                e.printStackTrace();
                                        }
                                }
                        });

                        // Apagar el ExecutorService después de su uso
                        executor.shutdown();
                }
        }

        private void llenarSpinnerTiposReporte() {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                String tiposReporte[] = {"1-Cuello Botella Cosecha", "2-Carretas Cosecha", "3-Premaduraciones", "4-Pesos de Cajas"};
                ArrayAdapter adapter;

                try {
                        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tiposReporte);
                        spTipoReporte.setAdapter(adapter);
                } catch (Exception e) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        private void llenarListViewReporte(ArrayList<String> stringArrayList) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                try {
                        //stringArrayList = new ArrayList<>();
//                        switch (opcion) {
//                                case 1:
//                                        for (MdCuelloBotella cb : listacb) {
//                                                stringArrayList.add(cb.getMotivo() + "   " + cb.getHora_inicio() + "   " + cb.getHora_final());
//                                      }
//                                        break;
//
//                                // case 2: break;
//                                // case 3: break;
//
//                                case 4:
//                                        for (MdPesoCaja pc : listaPesoCaja) {
//                                                stringArrayList.add(pc.getFecha() + "   " + pc.getPeso() + "   " + pc.getCliente() + "   " + pc.getCalibre() + "   " + pc.getDni_encargado() + "   " + pc.getObservacion());
//                                        }
//                                        break;
//                        }


                        lvReports = findViewById(R.id.lvDatosReporte);
                        myAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArrayList);
                        lvReports.setAdapter(myAdapter);
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
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
                if (dia < 10) {
                        if (mes < 9) {
                                stringFecha = "0" + dia + "/0" + (mes + 1) + "/" + anio;
                        } else {
                                stringFecha = "0" + dia + "/" + (mes + 1) + "/" + anio;
                        }
                } else {
                        if (mes < 9) {
                                stringFecha = dia + "/0" + (mes + 1) + "/" + anio;
                        } else {
                                stringFecha = dia + "/" + (mes + 1) + "/" + anio;
                        }
                }

                if (btnFechaSeleccionado == 1) {
                        tvtFecha1.setText(stringFecha);
                } else {
                        tvtFecha2.setText(stringFecha);
                }
        }
}