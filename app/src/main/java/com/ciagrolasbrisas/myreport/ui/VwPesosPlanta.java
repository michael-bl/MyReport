package com.ciagrolasbrisas.myreport.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.database.DatabaseController;

public class VwPesosPlanta extends AppCompatActivity {
        private TextView txtNombreEncargado, tvFechaSistema, tvDniUsuario;
        private Button btnGuardar;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_pesos_planta);

                txtNombreEncargado = findViewById(R.id.tvNombreUsuarioPC);
                tvFechaSistema = findViewById(R.id.tvFechaSistemaPC);
                tvDniUsuario = findViewById(R.id.tvUsuarioPC);
                btnGuardar = findViewById(R.id.btnGuardarPC);

                btnGuardar.setOnClickListener(view -> {
                        // ejecutar almacenamiento de datos
                });

                if (txtNombreEncargado.getText().equals("")) {
                        // nombreUsuario = variable estatica llenada en DatabaseController -> funcion Login
                        txtNombreEncargado.setText(DatabaseController.nombreUsuario);
                }

                // ejecuta las funciones para mostrar datos preestablecidos en la interfaz
                inicializarDatosUi();

        }

        private void inicializarDatosUi(){
                // asignacion de la fecha del sistema
                GetStringDate fecha = new GetStringDate();
                tvFechaSistema .setText(fecha.getFecha());

                //asignacion del codigo de usuario
                tvDniUsuario.setText(VwLogin.dniUsuario);

                // llenar spinner Cliente

                // llenar spinner Calibre

        }
}