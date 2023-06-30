package com.ciagrolasbrisas.myreport.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.drawerlayout.widget.DrawerLayout;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.model.MdMuestra;
import com.ciagrolasbrisas.myreport.model.MdUsuario;

public class DialogNewUpdate {

        private AlertDialog.Builder builder;
        private DrawerLayout drawerLayout;
        private LayoutInflater inflater;
        private Context context;
        private Intent intent;
        private Bundle bundle;
        private MdUsuario mdUsuario;
        private View view;
        private MdMuestra mdMuestra;
        private DialogTipoReporte dialogTipoReporte;

        public DialogNewUpdate() {
        }

        public DialogNewUpdate(Context con) {
                context = con;
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.dialog_new_update, null);
                drawerLayout = view.findViewById(R.id.drawer_layout);
                builder = new AlertDialog.Builder(context);
        }

        public AlertDialog dialogNewUpdate() {

                view = inflater.inflate(R.layout.dialog_new_update, null);
                final Button btnNuevo = view.findViewById(R.id.btnNuevo);
                final Button btnActualizar = view.findViewById(R.id.btnMasOpciones);
                dialogTipoReporte = new DialogTipoReporte(context);
                // Accion 0=nuevo y 1=actualizar
                btnNuevo.setOnClickListener(v -> {
                        dialogTipoReporte.selectTipoReporte(0).show();
                });
                btnActualizar.setOnClickListener(v -> {
                        dialogTipoReporte.selectTipoReporte(1).show();
                });

                builder.setView(view).setTitle("Elija una opción!");
                return builder.create();
        }
}
