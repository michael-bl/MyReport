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
import com.ciagrolasbrisas.myreport.model.MdPesoCaja;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class VwPesosPlanta extends AppCompatActivity {
        private TextView txtNombreEncargado, tvFechaSistema, tvDniUsuario;
        private TextInputEditText txtPesoCaja, txtDetalle;
        private Spinner spCliente, spCalibre;
        private Button btnGuardar;
        private DatabaseController dbController;
        private String dniUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_pesos_planta);

                dbController = new DatabaseController();

                txtNombreEncargado = findViewById(R.id.tvNombreUsuarioPC);
                txtPesoCaja = findViewById(R.id.txtPesoPC);
                txtDetalle = findViewById(R.id.txtDetallePC);
                tvFechaSistema = findViewById(R.id.tvFechaSistemaPC);
                tvDniUsuario = findViewById(R.id.tvUsuarioPC);
                btnGuardar = findViewById(R.id.btnGuardarPC);

                spCliente = findViewById(R.id.spClientePC);
                spCalibre = findViewById(R.id.spCalibrePC);

                btnGuardar.setOnClickListener(view -> {
                        // capturar datos de la UI para el modelo
                        MdPesoCaja pesoCaja = new MdPesoCaja();
                        pesoCaja.setPeso(txtPesoCaja.getText().toString().replace(".", ","));
                        pesoCaja.setFecha(tvFechaSistema.getText().toString());
                        pesoCaja.setDni_encargado(tvDniUsuario.getText().toString());

                        String limpiarCode = spCliente.getItemAtPosition(spCliente.getSelectedItemPosition()).toString().substring(0, 2);
                        if (limpiarCode.contains("-"))
                                limpiarCode = limpiarCode.substring(0,1);
                        pesoCaja.setCliente(limpiarCode);

                        pesoCaja.setCalibre(spCalibre.getItemAtPosition(spCalibre.getSelectedItemPosition()).toString());
                        pesoCaja.setObservacion(txtDetalle.getText().toString());

                        GetStringTime hora = new GetStringTime();
                        pesoCaja.setHora_captura(hora.getHora());

                        dbController.nuevoPesoCaja(this, pesoCaja);

                });

                if (txtNombreEncargado.getText().equals("")) {
                        // nombreUsuario = variable estatica llenada en DatabaseController -> funcion Login
                        txtNombreEncargado.setText(DatabaseController.nombreUsuario);
                }

                // ejecuta las funciones para mostrar datos preestablecidos en la interfaz
                inicializarDatosUi();

        }

        @Override
        public void onBackPressed() {
                Intent intent = new Intent(getApplicationContext(), VwMain.class);
                // Add the FLAG_ACTIVITY_CLEAR_TOP flag to clear the activity stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        private void inicializarDatosUi(){
                // asignacion de la fecha del sistema
                GetStringDate fecha = new GetStringDate();
                tvFechaSistema .setText(fecha.getFecha());

                dniUser = dbController.selectCedulaUser(this);
                //asignacion del codigo de usuario
                tvDniUsuario.setText(dniUser);

                // llenar spinner Cliente
                llenarSpinnerCliente();
                // llenar spinner Calibre
                llenarSpinnerCalibre();
        }

        private void llenarSpinnerCliente() {
                ArrayList<String> listaMotivos = dbController.selectClientesCajas(this);
                ArrayAdapter adapter;
                try {
                        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaMotivos);
                        spCliente.setAdapter(adapter);

                } catch (NullPointerException npe) {
                        Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

        private void llenarSpinnerCalibre() {
                ArrayList<String> listaCalibres = dbController.selectCalibres(this);
                ArrayAdapter adapter;
                try {
                        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaCalibres);
                        spCalibre.setAdapter(adapter);

                } catch (NullPointerException npe) {
                        Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
                }
        }
}