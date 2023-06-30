package com.ciagrolasbrisas.myreport.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VwTicketCosecha extends AppCompatActivity {
        private String fecha;
        private String hora;
        private int consecutivo_carreta;
        private String programa; // Tipo de cosecha  Oficial, Limpieza, NDF, Dirigido **Listview**
        private String tipo_pase; // By D, Centros, Limpieza, Barridas **Listview**
        private String gp_forza;
        private String ciclo;
        private String lote;
        private String seccion;
        private String digitador; // el bandero
        private int frutas; // El total de frutas cosechadas


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_ticket_cosecha);

                FloatingActionButton guardarTicketCos = findViewById(R.id.fabGuardarTicketCosecha);
                guardarTicketCos.setOnClickListener(view -> {
                        crearInforme();
                });
        }

        private void crearInforme(){
                GetStringDate gDate = new GetStringDate();
                GetStringTime gtime = new GetStringTime();
                fecha = gDate.getFecha();
                hora = gtime.getHora();

        }

        private void getConsecutivoCarreta(){
                // consulta a la bd local de SQLite por la ultima carreta ingresada

        }
}