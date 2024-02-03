package com.ciagrolasbrisas.myreport.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.DateConverter;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.controller.MyDatePicker;
import com.ciagrolasbrisas.myreport.database.CbCosechaController;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.database.ExistSqliteDatabase;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class VwHorasEfectivas extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

        private MdCuelloBotella cuelloBotella;
        private DatabaseController dbController;
        private String stringFecha, tiempo_efectivo, hora_inicio, hora_final, cedula, date, time, clase;
        private Button btnFecha;
        private Button btnBuscar;
        private boolean localMode;
        private LogGenerator logGenerator;
        private ArrayList<MdCuelloBotella> listCuelloBotella;
        private final Handler mainHandler = new Handler(Looper.getMainLooper());
        private TextView tvFechaHsEfectivas, tvJornadaHsEfectivas, tvHorasEfectivas, tvCuelloBotellaTotal;
        private long total_hs = 0, total_min = 0, total_seg = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_hs_efectivas);

                cuelloBotella = new MdCuelloBotella();

                GetStringDate stringDate = new GetStringDate();
                GetStringTime stringTime = new GetStringTime();
                date = stringDate.getFecha();
                time = stringTime.getHora();
                clase = this.getClass().getSimpleName();

                btnFecha = findViewById(R.id.btnFechaHsEfectivas);
                btnBuscar = findViewById(R.id.btnBuscarHsEfectivas);
                tvFechaHsEfectivas = findViewById(R.id.tvFechaHsEfectivas);
                tvJornadaHsEfectivas = findViewById(R.id.tvJornadaHorasEfectivas);
                tvCuelloBotellaTotal = findViewById(R.id.tvTiempoMuerto);
                tvHorasEfectivas = findViewById(R.id.tvHorasEfectivas);

                btnFecha.setOnClickListener(v -> showDatePickerDialog());

                btnBuscar.setOnClickListener(v -> {
                        dbController = new DatabaseController();
                        localMode = dbController.selectLocalMode(this);

                        if (localMode) {
                                getDesdeDbLocal();
                        } else {
                                getDesdeServidor();  // guarda el reporte en el servidor remoto
                        }
                });

        }

        private void showDatePickerDialog() {
                MyDatePicker datePickerFragment = new MyDatePicker();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        }

        @Override
        public void onBackPressed(){
                Intent intent = new Intent(getApplicationContext(), VwMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        @Override
        public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
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

                tvFechaHsEfectivas.setText(stringFecha);
        }

        private void getDesdeServidor() {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                OkHttpClient client = new OkHttpClient();
                ConnectivityService con = new ConnectivityService();

                if (con.stateConnection(this)) {
                        Map<String, Object> finalJson = new HashMap<>();

                        MdCuelloBotella cb = new MdCuelloBotella();
                        cb.setAccion(1); // Lista los cuellos de botella del día

                        cedula = dbController.selectCedulaUser(this);
                        cb.setCedula(cedula);
                        cb.setFecha(stringFecha); // La fecha seleccionada en el datepicker, formateada

                        listCuelloBotella = new ArrayList<>();
                        listCuelloBotella.add(cb);

                        finalJson.put("reporte", listCuelloBotella);  // {"reporte":[{"accion":1,"cedula":"05-0361-0263","fecha":"12/12/2023"}]}

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

                                                                listCuelloBotella = gson.fromJson(responseBody, listType);

                                                                for (MdCuelloBotella cb : listCuelloBotella) {
                                                                        if (cb.getMotivo().substring(0, 2).equals( "12")) {
                                                                                cuelloBotella.setHora_inicio(cb.getHora_inicio());
                                                                                cuelloBotella.setHora_final(cb.getHora_final());
                                                                        }
                                                                }

                                                                totalCuelloBotella(listCuelloBotella, cuelloBotella); // Suma el total de horas, minutos y segundos de los cbs excepto la jornada

                                                        });
                                                } else {
                                                        // Imprimir error en la respuesta
                                                        mainHandler.post(() -> {
                                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + response.message()); // Agregamos el error al archivo Descargas/Logs.txt
                                                                Toast.makeText(VwHorasEfectivas.this, "Error en la solicitud: " + response.message(), Toast.LENGTH_SHORT).show();
                                                        });
                                                }
                                        } catch (IOException e) {
                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                                                e.printStackTrace();
                                        }
                                }
                        });

                        // Apagar el ExecutorService después de su uso
                        executor.shutdown();
                }
        }

        private void getDesdeDbLocal() {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                ExistSqliteDatabase existdb = new ExistSqliteDatabase();
                DateConverter dc = new DateConverter();
                logGenerator = new LogGenerator();

                if (existdb.ExistSqliteDatabase()) {
                        try {
                                CbCosechaController cbCosController = new CbCosechaController();
                                dbController = new DatabaseController();
                                cedula = dbController.selectCedulaUser(this);
                                cuelloBotella = cbCosController.horasEfectivas(this, dc.dateFormat(stringFecha), cedula);
                                ArrayList listacb = cbCosController.getCuelloBotella(this, dc.dateFormat(stringFecha), null, true);
                                totalCuelloBotella(listacb, cuelloBotella); // Suma el total de horas, minutos y segundos de los cbs excepto la jornada

                        } catch (NumberFormatException nfe) {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion);
                        }
                }
        }

        private void totalCuelloBotella(@NonNull ArrayList<MdCuelloBotella> listacb, MdCuelloBotella cbcos) {
                String funcion = new Throwable().getStackTrace()[0].getMethodName();
                logGenerator = new LogGenerator();
                Duration lapso;
                long horas, minutos, segundos;

                try {
                        for (MdCuelloBotella cb : listacb) { // Totalizacion del tiempo perdido
                                if (!cb.getMotivo().equals("Jornada")) {
                                        lapso = Duration.between(LocalTime.parse(cb.getHora_inicio()), LocalTime.parse(cb.getHora_final()));
                                        horas = lapso.toHours();
                                        minutos = lapso.toMinutes() % 60;
                                        segundos = lapso.getSeconds() % 60;
                                        total_hs += horas;
                                        total_min += minutos;
                                        total_seg += segundos;
                                }
                        }

                        if (!cbcos.getHora_inicio().equals("")) {
                                DateTimeFormatter formato_hora = DateTimeFormatter.ofPattern("HH:mm:ss");

                                hora_inicio = cbcos.getHora_inicio();
                                hora_final = cbcos.getHora_final();

                                LocalTime hora_ini = LocalTime.parse(hora_inicio, formato_hora);
                                LocalTime hora_fin = LocalTime.parse(hora_final, formato_hora);
                                Duration lapso_ini = Duration.between(hora_ini, hora_fin);
                                long horas_ini = lapso_ini.toHours();
                                long minutos_ini = lapso_ini.toMinutes() % 60;
                                long segundos_ini = lapso_ini.getSeconds() % 60;

                                LocalTime hora2 = LocalTime.parse(String.format("%02d:%02d:%02d", horas_ini, minutos_ini, segundos_ini), formato_hora);


                                Duration tiempoMuerto = Duration.ofHours(total_hs).plusMinutes(total_min).plusSeconds(total_seg);
                                long h = tiempoMuerto.toHours();
                                long m = tiempoMuerto.toMinutes() % 60;
                                long s = tiempoMuerto.getSeconds() % 60;

                                tiempo_efectivo = hora2.minus(tiempoMuerto).toString();
                                tvJornadaHsEfectivas.setText("Hora inicio:  " + hora_inicio + "  Hora final:  " + hora_final);
                                tvCuelloBotellaTotal.setText("Tiempo muerto:  " + LocalTime.parse(String.format("%02d:%02d:%02d", h, m, s), formato_hora));
                                tvHorasEfectivas.setText("Tiempo efectivo:  " + tiempo_efectivo);

                        } else {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + "Sin datos que mostrar!"); // Agrega error en Descargas/Logs.txt
                                Toast.makeText(this, "Sin datos que mostrar!", Toast.LENGTH_LONG).show();
                        }
                        total_hs = 0;
                        total_min = 0;
                        total_seg = 0;
                } catch (NumberFormatException nfe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + nfe);
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe);
                }catch (Exception e) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e);
                }
        }
}
