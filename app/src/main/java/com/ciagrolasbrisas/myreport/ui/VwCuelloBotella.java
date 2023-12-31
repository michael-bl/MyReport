package com.ciagrolasbrisas.myreport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.model.MdWarning;
import com.google.android.material.textfield.TextInputEditText;
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

public class VwCuelloBotella extends AppCompatActivity {
    private Button btnHoraInicio, btnHoraFinal, btnGuardarReporte;
    private TextView tvFechaSistema, txtDniEncargado;
    private TextInputEditText txtLote, txtSeccion;
    private TextView tvHoraInicial, tvHoraFinal;
    private MdCuelloBotella objCuelloBotella;
    private List<MdCuelloBotella> listCuelloBotella;
    private DatabaseController dbController;
    private String horaSeleccionada;
    private Spinner spinnerMotivo;
    private LogGenerator logGenerator;
    private String date, time, clase, dniUser;
    private boolean localMode;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_cuello_botella);

        GetStringDate stringDate = new GetStringDate();
        GetStringTime stringTime = new GetStringTime();
        date = stringDate.getFecha();
        time = stringTime.getHora();
        clase = this.getClass().getSimpleName();

        logGenerator = new LogGenerator();

        txtDniEncargado = findViewById(R.id.tvDniEncargado);

        txtLote = findViewById(R.id.txtLoteCB);
        txtSeccion = findViewById(R.id.txtSeccionCB);
        tvFechaSistema = findViewById(R.id.tvFechaSistemaCb);
        spinnerMotivo = findViewById(R.id.spMotivoCB);
        tvHoraInicial = findViewById(R.id.tvHoraInicio);
        tvHoraFinal = findViewById(R.id.tvHoraFinal);
        objCuelloBotella = new MdCuelloBotella();
        btnHoraInicio = findViewById(R.id.btnHoraInicioCB);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            objCuelloBotella = (MdCuelloBotella) bundle.getSerializable("cuellobotella");  // Extrayendo el extra de tipo cadena
            if (Objects.requireNonNull(objCuelloBotella).getAccion() == 1 || Objects.requireNonNull(objCuelloBotella).getAccion() == 2) // Actualizar o eliminar reporte
                mostrarDatosEnInterfaz(objCuelloBotella);
        }

        if (txtDniEncargado.getText().equals("")) {
            DatabaseController db = new DatabaseController();
            dniUser = db.selectDniUser(this);
            txtDniEncargado.setText(dniUser);
        }

        capturarFechaDelSistema();

        btnHoraInicio.setOnClickListener(view -> {
            capturarHoraDelSistema();
            tvHoraInicial.setText(horaSeleccionada);
        });

        btnHoraFinal = findViewById(R.id.btnHoraFinalCB);
        btnHoraFinal.setOnClickListener(view -> {
            capturarHoraDelSistema();
            tvHoraFinal.setText(horaSeleccionada);
        });

        llenarSpinnerMotivo();

        btnGuardarReporte = findViewById(R.id.btnGuardarCB);
        btnGuardarReporte.setOnClickListener(view -> {

            dbController = new DatabaseController();
            localMode = dbController.selectLocalMode(this);

            if (localMode) {
                if (guardarRptEnDbLocal()) {
                    lanzarActividad();
                }
            } else {
                guardarEnServidor();  // guarda el reporte en el servidor remoto
                lanzarActividad();
            }
        });
    }

    private void lanzarActividad() {
        Intent intent = new Intent(getApplicationContext(), VwMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void mostrarDatosEnInterfaz(MdCuelloBotella objCuelloBotella) {
        String funcion = new Throwable().getStackTrace()[0].getMethodName();
        try {
            tvFechaSistema.setText(objCuelloBotella.getFecha());
            txtDniEncargado.setText(objCuelloBotella.getDniEncargado());
            txtLote.setText(objCuelloBotella.getLote());
            txtSeccion.setText(objCuelloBotella.getSeccion());
            tvHoraInicial.setText(objCuelloBotella.getHora_inicio());
            tvHoraFinal.setText(objCuelloBotella.getHora_final());
            btnHoraInicio.setEnabled(false);
        } catch (NullPointerException npe) {
            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void guardarEnServidor() {
        String funcion = new Throwable().getStackTrace()[0].getMethodName();
        OkHttpClient client = new OkHttpClient();
        llenarReporteCb();

        if (!objCuelloBotella.getLote().equals("") || !objCuelloBotella.getSeccion().equals("") || !objCuelloBotella.getHora_inicio().equals("")) {
            if (objCuelloBotella.getAccion() == 0 && tvHoraFinal.getText().length() == 0) {
                objCuelloBotella.setHora_final("00:00:00");
            } else {
                objCuelloBotella.setHora_final(tvHoraFinal.getText().toString());
            }
            String limpiarCode = spinnerMotivo.getItemAtPosition(spinnerMotivo.getSelectedItemPosition()).toString().substring(0, 2);
            if (limpiarCode.contains("-"))
                limpiarCode = limpiarCode.substring(0, 1);
            objCuelloBotella.setMotivo(limpiarCode);

            ConnectivityService con = new ConnectivityService();
            if (con.stateConnection(this)) {

                listCuelloBotella = new ArrayList<>();
                listCuelloBotella.add(objCuelloBotella);
                Map<String, Object> finalJson = new HashMap<>();
                finalJson.put("reporte", listCuelloBotella);

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
                                    Type listType = new TypeToken<MdWarning>() {
                                    }.getType();

                                    MdWarning mensaje = gson.fromJson(responseBody, listType);

                                    Toast.makeText(VwCuelloBotella.this, mensaje.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                // Imprimir error en la respuesta
                                mainHandler.post(() -> {
                                    logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + response.message()); // Agregamos el error al archivo Descargas/Logs.txt
                                    Toast.makeText(VwCuelloBotella.this, "Error en la solicitud: " + response.message(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch (IOException e) {
                            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + e.getMessage());
                        }
                    }
                });

                // Apagar el ExecutorService después de su uso
                executor.shutdown();

            } else {
                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "El dispositivo no puede accesar a la red en este momento!"); // Agregamos el error al archivo Descargas/Logs.txt
                Toast.makeText(this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
            }
        } else {
            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Advertencia: existen campos vacios!");
            Toast.makeText(this, "Advertencia: existen campos vacios!", Toast.LENGTH_LONG).show();
        }

    }

    private boolean guardarRptEnDbLocal() {
        String funcion = new Throwable().getStackTrace()[0].getMethodName();
        try {

            llenarReporteCb();  // Cargamos la informacion en el objeto

            if (!objCuelloBotella.getLote().equals("") || !objCuelloBotella.getSeccion().equals("") || !objCuelloBotella.getHora_inicio().equals("")) {
                if (objCuelloBotella.getAccion() == 0 && tvHoraFinal.getText().length() == 0) {
                    objCuelloBotella.setHora_final("00:00:00");
                } else {
                    objCuelloBotella.setHora_final(tvHoraFinal.getText().toString());
                }
                String limpiarCode = spinnerMotivo.getItemAtPosition(spinnerMotivo.getSelectedItemPosition()).toString().substring(0, 2);
                if (limpiarCode.contains("-"))
                    limpiarCode = limpiarCode.substring(0, 1);
                objCuelloBotella.setMotivo(limpiarCode);

                if (objCuelloBotella.getAccion() == 0) {
                    if (objCuelloBotella.getMotivo().equals("12")) {
                        if (!dbController.existJornada(this, objCuelloBotella.getFecha(), objCuelloBotella.getDniEncargado(), objCuelloBotella.getMotivo())) {
                            dbController.nuevoRptCuelloBotellaCos(this, objCuelloBotella);
                        } else {
                            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Ya existe un reporte de jornada, verifique!"); // Agregamos el error al archivo Descargas/Logs.txt
                            Toast.makeText(this, "Ya existe un reporte de jornada, verifique!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        dbController.nuevoRptCuelloBotellaCos(this, objCuelloBotella);
                    }
                } else {
                    dbController.updateCuelloBotella(this, objCuelloBotella);
                }
                return true;
            } else {
                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "Advertencia: existen campos vacios!");
                Toast.makeText(this, "Advertencia: existen campos vacios!", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException npe) {
            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe.getMessage());
            Toast.makeText(this, "Error: " + npe, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void llenarReporteCb() {
        dbController = new DatabaseController();
        objCuelloBotella.setFecha(tvFechaSistema.getText().toString());
        objCuelloBotella.setDniEncargado(txtDniEncargado.getText().toString());
        objCuelloBotella.setLote(txtLote.getText().toString());
        objCuelloBotella.setSeccion(txtSeccion.getText().toString());
        objCuelloBotella.setHora_inicio(tvHoraInicial.getText().toString());
    }

    private void capturarFechaDelSistema() {
        GetStringDate fecha = new GetStringDate();
        tvFechaSistema.setText(fecha.getFecha());
    }

    private void capturarHoraDelSistema() {
        GetStringTime hora = new GetStringTime();
        horaSeleccionada = hora.getHora();
    }

    private void llenarSpinnerMotivo() {
        String funcion = new Throwable().getStackTrace()[0].getMethodName();
        ArrayList<String> listaMotivos = new ArrayList<>();
        ArrayAdapter adapter;
        try {
            listaMotivos.add("01-Acomódo de cosechadora");
            listaMotivos.add("02-Almuerzo");
            listaMotivos.add("03-Ataque de abejas");
            listaMotivos.add("04-Auditoría");
            listaMotivos.add("05-Botar cera");
            listaMotivos.add("06-Café");
            listaMotivos.add("07-Campaña de vacunación");
            listaMotivos.add("08-Capacitación");
            listaMotivos.add("09-Desayuno");
            listaMotivos.add("10-Esperando carretas");
            listaMotivos.add("11-Exámen médico");
            listaMotivos.add("12-Jornada");
            listaMotivos.add("13-Pegaderos");
            listaMotivos.add("14-Respuesta taller");
            listaMotivos.add("15-Reunión");
            listaMotivos.add("16-Traslado");
            listaMotivos.add("17-Cambio de tractor");
            listaMotivos.add("18-Tormenta eléctrica");
            listaMotivos.add("19-Problemas de tracción");
            listaMotivos.add("20-Atraso por planta");
            listaMotivos.add("21-Otros");

            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaMotivos);
            int position = 0;

            if (objCuelloBotella.getAccion() == 0) {
                this.spinnerMotivo.setAdapter(adapter);
            } else {
                String motivo = objCuelloBotella.getMotivo().substring(0, 2);
                for (int i = 0; i < listaMotivos.size(); i++) {
                    if (listaMotivos.get(i).substring(0, 2).equals(motivo)) {
                        position = i;
                    }
                }
                spinnerMotivo.setAdapter(adapter);
                spinnerMotivo.setSelection(position);
            }

        } catch (NullPointerException npe) {
            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + npe.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}