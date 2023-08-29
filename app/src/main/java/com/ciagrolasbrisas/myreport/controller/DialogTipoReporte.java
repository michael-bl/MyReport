package com.ciagrolasbrisas.myreport.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.ui.VwCuelloBotella;
import com.ciagrolasbrisas.myreport.ui.VwHorasEfectivas;
import com.ciagrolasbrisas.myreport.ui.VwListarCuelloBotella;
import com.ciagrolasbrisas.myreport.ui.VwPesosPlanta;
import com.ciagrolasbrisas.myreport.ui.VwPremas1;
import com.ciagrolasbrisas.myreport.ui.VwTicketCosecha;

public class DialogTipoReporte {
    private AlertDialog.Builder builder;
    private DrawerLayout drawerLayout;
    private LayoutInflater inflater;
    private Context context;
    private Intent intent;
    private Bundle bundle;
    private View view;

    public DialogTipoReporte(){}

    public DialogTipoReporte(Context con){
        context = con;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog_new_update, null);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        builder = new AlertDialog.Builder(context);

    }

    public AlertDialog selectTipoReporte(int accionCrud){
        view = inflater.inflate(R.layout.dialog_tipo_reporte, null);
        final Button btnRptHorasEfectivas = view.findViewById(R.id.btnRptHorasEfectivas);
        final Button btnRptPremas = view.findViewById(R.id.btnRptPremas);
        final Button btnRptCuelloBotella = view.findViewById(R.id.btnRptCuelloBotella);
        final Button btnRptTikectCosecha = view.findViewById(R.id.btnRptTicketCosecha);
        final Button btnRptPesoCaja = view.findViewById(R.id.btnRptPesoCaja);
        MdCuelloBotella mdCuelloBotella = new MdCuelloBotella();

        btnRptHorasEfectivas.setOnClickListener(v -> {
            //Toast.makeText(context, "Esta función está en desarrollo!",  Toast.LENGTH_LONG).show();
            intent = new Intent(context, VwHorasEfectivas.class);
            bundle = new Bundle();
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        btnRptPremas.setOnClickListener(v -> {
            intent = new Intent(context, VwPremas1.class);
            bundle = new Bundle();
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        btnRptCuelloBotella.setOnClickListener(v ->{
            switch (accionCrud){
                case 0:
                    intent = new Intent(context, VwCuelloBotella.class);
                    bundle = new Bundle();
                    mdCuelloBotella.setAccion(0);
                    bundle.putSerializable("cuellobotella", mdCuelloBotella);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(context, VwListarCuelloBotella.class);
                    bundle = new Bundle();
                    mdCuelloBotella.setAccion(1);
                    bundle.putSerializable("cuellobotella", mdCuelloBotella);
                    intent.putExtras(bundle);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
            }

        });

        btnRptPesoCaja.setOnClickListener(v -> {
            intent = new Intent(context, VwPesosPlanta.class);
            bundle = new Bundle();
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        btnRptTikectCosecha.setOnClickListener(v->
        {
            intent = new Intent(context, VwTicketCosecha.class);
            bundle = new Bundle();
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        builder.setView(view).setTitle("Elija una opción!");
        return builder.create();
    }
}
