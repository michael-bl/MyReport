package com.ciagrolasbrisas.myreport.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.MyDatePicker;
import com.ciagrolasbrisas.myreport.model.MdLote;
import com.ciagrolasbrisas.myreport.model.MdMuestra;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class VwPremas1 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Bundle bundle;
    private Intent intent;
    private MdMuestra mdMuestra;
    private String stringDate;
    private Calendar calendar;
    private Spinner spinnerLote;
    private Spinner spinnerCiclo;
    private Spinner spinnerSeccion;
    private TextView txtFechaSeleccionada;
    private Spinner spinnerTipoMuestreo;
    private Spinner spinnerSemanasAcosecha;
    private Button btnActivityPremas2, btnDatePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_premas1);

        spinnerCiclo = findViewById(R.id.spCiclo);
        spinnerLote= findViewById(R.id.spLote);
        spinnerSeccion= findViewById(R.id.spSeccion);
        spinnerTipoMuestreo= findViewById(R.id.spTipoMuestreo);
        spinnerSemanasAcosecha = findViewById(R.id.spSemanasAcosecha);
        txtFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);

        btnActivityPremas2 = findViewById(R.id.btnSiguiente);
        btnActivityPremas2.setOnClickListener(view -> {
            intent = new Intent(this, VwPremas2.class);
            bundle = new Bundle();
            mdMuestra = new MdMuestra();
            llenarVariablesDeMuestreo();
            bundle.putSerializable("mdMuestra", mdMuestra);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnDatePicker = findViewById(R.id.btnFecha);
        btnDatePicker.setOnClickListener(view -> {
            MyDatePicker myDatePicker = new MyDatePicker();
            myDatePicker.show(this.getSupportFragmentManager(),"Date Picker");
        });

        llenarSpinnerCiclo();
        llenarSpinnerLote();
        llenarSpinnerSeccion();
        llenarSpinnerTipoMuestreo();
        llenarSpinnerSemanasAcosecha();
    }


    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        try {
            month++;
            calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            if (day<10) if (month < 10) stringDate = "0" + day + "/" + "0" + month + "/" + year;
            else stringDate = "0" + day + "/" + month + "/" + year;
            else if (month < 10) stringDate = day + "/" + "0" + month + "/" + year;
            else stringDate = day + "/" + month + "/" + year;

            txtFechaSeleccionada.setText(stringDate);
        } catch(NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarVariablesDeMuestreo() {
        try {
            MdLote mdLote = new MdLote();
            Spinner spLote = findViewById(R.id.spLote);
            Spinner spCiclo = findViewById(R.id.spCiclo);
            Spinner spSeccion = findViewById(R.id.spSeccion);
            Spinner spTipoMuestreo = findViewById(R.id.spTipoMuestreo);
            TextInputEditText txtGrupoForza = findViewById(R.id.txtGrupoForza);

            mdLote.setGrupoForza(txtGrupoForza.getText().toString());
            mdLote.setCiclo(Integer.parseInt(spCiclo.getItemAtPosition(spCiclo.getSelectedItemPosition()).toString().substring(0,1)));
            mdLote.setLote(spLote.getItemAtPosition(spLote.getSelectedItemPosition()).toString());
            mdLote.setSeccion(spSeccion.getItemAtPosition(spSeccion.getSelectedItemPosition()).toString());
            mdMuestra.setTipoMuestreo(spTipoMuestreo.getItemAtPosition(spTipoMuestreo.getSelectedItemPosition()).toString().substring(0,1));
            mdMuestra.setSemanasCosecha(Integer.parseInt(spinnerSemanasAcosecha.getItemAtPosition(spinnerSemanasAcosecha.getSelectedItemPosition()).toString().substring(0,1)));
            //stringDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            mdMuestra.setFecha(stringDate);
            mdMuestra.setLote(mdLote);
        } catch (NullPointerException npe){
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerCiclo() {
        ArrayList<String> listaCiclo = new ArrayList<>();
        try {
            listaCiclo.add("1-Primeras");
            listaCiclo.add("2-Segundas");
            listaCiclo.add("3-Terceras");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaCiclo);
            this.spinnerCiclo.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerLote() {
        ArrayList<String> listaLote = new ArrayList<>();
        try {
            listaLote.add("1020");
            listaLote.add("2030");
            listaLote.add("3001");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaLote);
            this.spinnerLote.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerSeccion() {
        ArrayList<String> listaSeccion = new ArrayList<>();
            try {
                listaSeccion.add("1");
                listaSeccion.add("2");
                listaSeccion.add("3");
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaSeccion);
                this.spinnerSeccion.setAdapter(adapter);
            } catch (NullPointerException npe) {
                Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
            }
    }

    private void llenarSpinnerSemanasAcosecha() {
        ArrayList<String> listaSeccion = new ArrayList<>();
        try {
            listaSeccion.add("1");
            listaSeccion.add("2");
            listaSeccion.add("3");
            listaSeccion.add("4");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaSeccion);
            this.spinnerSemanasAcosecha.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerTipoMuestreo() {
        ArrayList<String> listaTipoMuestreo = new ArrayList<>();
        try {
            listaTipoMuestreo.add("1-Normal");
            listaTipoMuestreo.add("2-Ensayo");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTipoMuestreo);
            this.spinnerTipoMuestreo.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}