package com.ciagrolasbrisas.myreport.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class VwCuelloBotella extends AppCompatActivity {
        private Button btnHoraInicio, btnHoraFinal, btnGuardarReporte;
        private TextView tvFechaSistema, txtDniEncargado;
        private TextInputEditText txtLote, txtSeccion;
        private TextView tvHoraInicial, tvHoraFinal;
        private MdCuelloBotella objCuelloBotella;
        private DatabaseController dbController;
        private String horaSeleccionada;
        private Spinner spinnerMotivo;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_cuello_botella);

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
                        // Extrayendo el extra de tipo cadena
                        objCuelloBotella = (MdCuelloBotella) bundle.getSerializable("cuellobotella");
                        if (Objects.requireNonNull(objCuelloBotella).getAccion() == 1 || Objects.requireNonNull(objCuelloBotella).getAccion() == 2)
                                mostrarDatosEnInterfaz(objCuelloBotella);
                }

                if (txtDniEncargado.getText().equals("")) {
                        // dniUsuario = variable estatica llenada en VwLogin
                        txtDniEncargado.setText(VwLogin.dniUsuario);
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
                        if(guardarReporteDbLocal()){
                                Intent intent = new Intent(getApplicationContext(), VwMain.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                        }
                });
        }

        private void mostrarDatosEnInterfaz(MdCuelloBotella objCuelloBotella) {
                try {
                        tvFechaSistema.setText(objCuelloBotella.getFecha());
                        txtDniEncargado.setText(objCuelloBotella.getDniEncargado());
                        txtLote.setText(objCuelloBotella.getLote());
                        txtSeccion.setText(objCuelloBotella.getSeccion());
                        tvHoraInicial.setText(objCuelloBotella.getHora_inicio());
                        tvHoraFinal.setText(objCuelloBotella.getHora_final());
                        btnHoraInicio.setEnabled(false);
                } catch (NullPointerException npe) {
                        Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        private boolean guardarReporteDbLocal() {
                try {
                        dbController = new DatabaseController();
                        objCuelloBotella.setFecha(tvFechaSistema.getText().toString());
                        objCuelloBotella.setDniEncargado(txtDniEncargado.getText().toString());
                        objCuelloBotella.setLote(txtLote.getText().toString());
                        objCuelloBotella.setSeccion(txtSeccion.getText().toString());
                        objCuelloBotella.setHora_inicio(tvHoraInicial.getText().toString());

                        if(!objCuelloBotella.getLote().equals("") || !objCuelloBotella.getSeccion().equals("") || !objCuelloBotella.getHora_inicio().equals("")){
                                if (objCuelloBotella.getAccion() == 0 && tvHoraFinal.getText().length()==0) {
                                        objCuelloBotella.setHora_final("00:00:00");
                                }else {
                                        objCuelloBotella.setHora_final(tvHoraFinal.getText().toString());
                                }
                                String limpiarCode = spinnerMotivo.getItemAtPosition(spinnerMotivo.getSelectedItemPosition()).toString().substring(0, 2);
                                if (limpiarCode.contains("-"))
                                        limpiarCode = limpiarCode.substring(0,1);
                                objCuelloBotella.setMotivo(limpiarCode);

                                if (objCuelloBotella.getAccion() == 0) {
                                        if(objCuelloBotella.getMotivo().equals("12")){
                                                if(!dbController.existJornada(this, objCuelloBotella.getFecha(), objCuelloBotella.getDniEncargado(), objCuelloBotella.getMotivo())){
                                                        dbController.nuevoRptCuelloBotella(this, objCuelloBotella);
                                                } else {
                                                        Toast.makeText(this, "Ya existe un reporte de jornada, verifique!" , Toast.LENGTH_LONG).show();
                                                }
                                        } else {
                                                dbController.nuevoRptCuelloBotella(this, objCuelloBotella);
                                        }
                                } else {
                                        dbController.updateCuelloBotella(this, objCuelloBotella);
                                }
                                return true;
                        } else {
                                Toast.makeText(this, "Advertencia: existen campos vacios!" , Toast.LENGTH_LONG).show();
                        }
                } catch (NullPointerException npe) {
                        Toast.makeText(this, "Error: " + npe, Toast.LENGTH_LONG).show();
                }
                return false;
        }

        private void capturarFechaDelSistema(){
                GetStringDate fecha = new GetStringDate();
                tvFechaSistema.setText(fecha.getFecha());
        }

        private void capturarHoraDelSistema() {
                GetStringTime hora = new GetStringTime();
                horaSeleccionada = hora.getHora();
        }

        private void llenarSpinnerMotivo() {
                ArrayList<String> listaMotivos = new ArrayList<>();
                ArrayAdapter adapter;
                try {
                        listaMotivos.add("1-Acomódo de cosechadora");
                        listaMotivos.add("2-Almuerzo");
                        listaMotivos.add("3-Ataque de abejas");
                        listaMotivos.add("4-Auditoría");
                        listaMotivos.add("5-Botar cera");
                        listaMotivos.add("6-Café");
                        listaMotivos.add("7-Campaña de vacunación");
                        listaMotivos.add("8-Capacitación");
                        listaMotivos.add("9-Desayuno");
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

                        if (objCuelloBotella.getAccion() == 0){
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
                        Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }
}