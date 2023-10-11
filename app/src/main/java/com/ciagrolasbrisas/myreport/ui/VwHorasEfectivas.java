package com.ciagrolasbrisas.myreport.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.MyDatePicker;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.database.ExistSqliteDatabase;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VwHorasEfectivas extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

        private DatabaseController dbController;
        private String jornada, stringFecha, tiempo_efectivo, hora_inicio, hora_final;
        private Button btnFecha;
        private Button btnBuscar;
        private TextView tvFechaHsEfectivas, tvJornadaHsEfectivas, tvHorasEfectivas, tvCuelloBotellaTotal;
        private long total_hs = 0, total_min = 0, total_seg = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_hs_efectivas);

                btnFecha = findViewById(R.id.btnFechaHsEfectivas);
                btnBuscar = findViewById(R.id.btnBuscarHsEfectivas);
                tvFechaHsEfectivas = findViewById(R.id.tvFechaHsEfectivas);
                tvJornadaHsEfectivas = findViewById(R.id.tvJornadaHorasEfectivas);
                tvCuelloBotellaTotal = findViewById(R.id.tvTiempoMuerto);
                tvHorasEfectivas = findViewById(R.id.tvHorasEfectivas);

                btnFecha.setOnClickListener(v-> showDatePickerDialog());

                btnBuscar.setOnClickListener(v -> onClick(v));

        }

        private void showDatePickerDialog() {
                MyDatePicker datePickerFragment = new MyDatePicker();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        }

        @Override
        public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                if (dia < 10) {
                        if (mes <9) {
                                stringFecha = "0" + dia + "/0" + (mes+1) + "/" + anio;
                        } else {
                                stringFecha = "0" + dia + "/" + (mes+1) + "/" + anio;
                        }
                } else {
                        if (mes <9) {
                                stringFecha = dia + "/0" + (mes+1) + "/" + anio;
                        } else {
                                stringFecha = dia + "/" + (mes+1) + "/" + anio;
                        }
                }

                tvFechaHsEfectivas.setText(stringFecha);
        }

        @SuppressLint("SetTextI18n")
        private void onClick(View v) {
                ExistSqliteDatabase existdb = new ExistSqliteDatabase();

                if (existdb.ExistSqliteDatabase()) {
                        try {
                                dbController = new DatabaseController();

                                ArrayList listacb = dbController.selectCuelloBotella(this, stringFecha, null, true);

                                totalCuelloBotella(listacb);

                                jornada = dbController.horasEfectivas(this, stringFecha, VwLogin.dniUsuario);

                                if (!jornada.equals("")) {
                                        DateTimeFormatter formato_hora = DateTimeFormatter.ofPattern("HH:mm:ss");

                                        String[] separador = jornada.split("/", 0);
                                        hora_inicio = separador[0];
                                        hora_final = separador[1];

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
                                        Toast.makeText(this, "Sin datos que mostrar!", Toast.LENGTH_LONG).show();
                                }
                                total_hs = 0;
                                total_min  = 0;
                                total_seg = 0;
                        } catch (NumberFormatException nfe) {
                                Log.i("MyReport", "Error: " + nfe.getMessage());
                        }
                }
        }

        private void totalCuelloBotella(@NonNull ArrayList<MdCuelloBotella> listacb) {
                Duration lapso;
                long horas, minutos, segundos;

                for (MdCuelloBotella cb: listacb) {
                        if(!cb.getMotivo().equals("Jornada")){
                                lapso = Duration.between(LocalTime.parse(cb.getHora_inicio()), LocalTime.parse(cb.getHora_final()));
                                horas = lapso.toHours();
                                minutos = lapso.toMinutes() % 60;
                                segundos = lapso.getSeconds() % 60;
                                total_hs += horas;
                                total_min += minutos;
                                total_seg += segundos;
                        }
                }
        }
}
