package com.ciagrolasbrisas.myreport.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.model.MdLote;
import com.ciagrolasbrisas.myreport.model.MdMuestra;

import java.util.ArrayList;

public class VwPremas2 extends AppCompatActivity {

    private MdLote mdLote;
    private String data;
    private Spinner spIBD;
    private Spinner spTipo;
    private MdMuestra mdMuestra;
    private Spinner spTamano;
    private Spinner spTraslucidez;
    private TextView txtDatosPrevios;
    private Button btnAgregarCalibre;
    private Button btnGuardarReporte;
    private int contadorFrutasAgregadas = 12;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_premas2);

        txtDatosPrevios = findViewById(R.id.textView5);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            mdMuestra = (MdMuestra) bundle.getSerializable("mdMuestra");
            mdLote = mdMuestra.getLote();
            setContadorSegunSemanasCosecha(mdMuestra);
            setInfoEnTextView5();
        }

        this.spTipo = findViewById(R.id.spTipo);
        this.spTamano = findViewById(R.id.spTamano);
        this.spIBD = findViewById(R.id.spIbd);
        this.spTraslucidez = findViewById(R.id.spTraslucidez);

        btnAgregarCalibre = findViewById(R.id.btnGuardarCalibre);
        btnAgregarCalibre.setOnClickListener(view-> {
            if(contadorFrutasAgregadas==1) {
                btnAgregarCalibre.setEnabled(false);
                Toast.makeText(this, "No puedes agregar más muestras!", Toast.LENGTH_LONG);
            }
            else {contadorFrutasAgregadas--;
                setInfoEnTextView5();
            }

        });

        btnGuardarReporte = findViewById(R.id.btnGuardar);
        btnGuardarReporte.setOnClickListener(view-> recopilarYguardar());

        llenarSpinnerTipo();
        llenarSpinnerTamano();
        llenarSpinnerIbd();
        llenarSpinnerTraslucidez();
    }

    private void setInfoEnTextView5(){
        String tipo="";
        if(mdMuestra.getTipoMuestreo().equals(1)) tipo="Normal";
        else tipo="Ensayo";

        data = "Tipo Muestreo: " + mdMuestra.getTipoMuestreo() +
                "\nFecha muestreo: " + mdMuestra.getFecha() +
                "\nGrupo de Forza: " + mdLote.getGrupoForza() +
                "\nMdLote: " + mdLote.getLote() +
                "\nSección: " + mdLote.getSeccion() +
                "\nFrutas X Agregar: " + contadorFrutasAgregadas;
        txtDatosPrevios.setText(data);
    }

    private void setContadorSegunSemanasCosecha(MdMuestra mdMuestra1) {
        if(mdMuestra1.getSemanasCosecha()==4) contadorFrutasAgregadas = 6;
        else if(mdMuestra1.getSemanasCosecha()<=3 && mdMuestra1.getSemanasCosecha()>=1) contadorFrutasAgregadas = 12;
    }

    private void llenarSpinnerTipo() {
        ArrayList<String> listaTipo = new ArrayList<>();
        try {
            listaTipo.add("Bordes");
            listaTipo.add("Centros");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTipo);
            this.spTipo.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerTamano() {
        ArrayList<String> listaTamano = new ArrayList<>();
        try {
            listaTamano.add("5");
            listaTamano.add("6");
            listaTamano.add("7");
            listaTamano.add("8");
            listaTamano.add("9");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTamano);
            this.spTamano.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerIbd() {
        ArrayList<String> listaIbd = new ArrayList<>();
        try {
            listaIbd.add("0-Sano");
            listaIbd.add("1-Leve");
            listaIbd.add("2-Moderado");
            listaIbd.add("3-Severo");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaIbd);
            this.spIBD.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerTraslucidez() {
        ArrayList<String> listaTraslucidez = new ArrayList<>();
        try {
            listaTraslucidez.add("0");
            listaTraslucidez.add("0.25");
            listaTraslucidez.add("0.5");
            listaTraslucidez.add("0.75");
            listaTraslucidez.add("1");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTraslucidez);
            this.spTraslucidez.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void recopilarYguardar() {
        //
    }
}