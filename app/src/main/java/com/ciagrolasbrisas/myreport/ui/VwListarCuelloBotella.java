package com.ciagrolasbrisas.myreport.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.controller.SelectionAdapter;
import com.ciagrolasbrisas.myreport.controller.cosecha.CbServidorController;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.database.ExistSqliteDatabase;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class VwListarCuelloBotella extends AppCompatActivity {
        private ArrayList<MdCuelloBotella> listCuelloBotella;
        private DatabaseController dbController;
        private ArrayList<String> stringListCB;
        private MdCuelloBotella mdCuelloBotella;
        private AlertDialog.Builder builder;
        private ExistSqliteDatabase existDb;
        // MultiSelect list adapter
        private ListView lvListaCuelloBotella;
        private SelectionAdapter myAdapter;
        private TextView tvBuscarReporteCB;
        private DrawerLayout drawerLayout;
        private LayoutInflater inflater;
        private Bundle bundle;
        private Intent intent;
        private View view;
        private String date, time, clase, dniUser;
        private boolean localMode;
        private LogGenerator logGenerator;
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_listar_cuellobotella);

                clase = this.getClass().getSimpleName();

                logGenerator = new LogGenerator();

                GetStringDate stringDate = new GetStringDate();
                GetStringTime stringTime = new GetStringTime();
                date = stringDate.getFecha();
                time = stringTime.getHora();

                dbController = new DatabaseController();
                localMode = dbController.selectLocalMode(this);

                mdCuelloBotella = new MdCuelloBotella();
                Button btnSiguente = findViewById(R.id.btnSiguienteCB);
                inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_new_update, null);
                builder = new AlertDialog.Builder(this);
                bundle = new Bundle();
                intent = new Intent(this, VwCuelloBotella.class);

                getCuellosPendientes();

                btnSiguente.setOnClickListener(v -> {
                        try {
                                if (mdCuelloBotella.getCode() != null) {
                                        dialogDeleteUpdateCuelloBotella().show();
                                } else {
                                        Toast.makeText(this, "Advertencia: debes seleccionar un reporte para poder continuar!", Toast.LENGTH_LONG).show();
                                }
                        } catch (Exception e) {
                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + e.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                                Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
                        }
                });
        }

        private void getCuellosPendientes() {

                try {
                        if (!localMode) {
                                Map<String, Object> finalJson = new HashMap<>();

                                MdCuelloBotella cb = new MdCuelloBotella();
                                cb.setAccion(4); // Para listar los cuellos pendientes de cierre

                                dniUser = dbController.selectDniUser(this);
                                cb.setDniEncargado(dniUser);
                                cb.setFecha(date);

                                listCuelloBotella = new ArrayList<>();
                                listCuelloBotella.add(cb);

                                finalJson.put("reporte", listCuelloBotella);  // {"reporte":[{"accion":4,"dniEncargado":"05-0361-0263","fecha":"12/12/2023"}]}

                                String json = new Gson().toJson(finalJson); // Enviar por parametro esta variable a CbServidorController.crudCuelloBotella

                                //getCBSinCerrarCosDbRemota();
                                CbServidorController cbServidorController = new CbServidorController();
                                listCuelloBotella = cbServidorController.crudCuelloBotella(this, json);

                                if (listCuelloBotella.size()>=0){
                                        for (MdCuelloBotella cbs : listCuelloBotella) {
                                                stringListCB.add(cbs.getMotivo() + "-" + cbs.getHora_final());
                                        }
                                        llenarListViewCuelloBotella(stringListCB);
                                } else {
                                        Toast.makeText(this, "Sin datos, verifique!", Toast.LENGTH_SHORT).show();
                                }

                        } else {
                                existDb = new ExistSqliteDatabase();
                                if (existDb.ExistSqliteDatabase()) {
                                        getCBSinCerrarCosDbLocal();
                                }
                        }
                } catch (Exception e){
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + e.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                        Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
                }
        }

        private void getCBSinCerrarCosDbLocal() {
                try {
                        dbController = new DatabaseController();
                        listCuelloBotella = dbController.selectCuelloBotellaIncompleto(this);
                        stringListCB = new ArrayList<>();
                        for (MdCuelloBotella cb : listCuelloBotella) {
                                stringListCB.add(cb.getMotivo() + "-" + cb.getHora_final());
                        }
                        llenarListViewCuelloBotella(stringListCB);
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + npe.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                        Toast.makeText(this, "Error: " + npe, Toast.LENGTH_LONG).show();
                }
        }

        private void llenarListViewCuelloBotella(ArrayList<String> listaCB) {
                try {
                        lvListaCuelloBotella = findViewById(R.id.lvCuelloBotella);
                        myAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaCB);
                        lvListaCuelloBotella.setAdapter(myAdapter);
                        onTextChanged();
                        setUpActionBar();
                } catch (NullPointerException npe) {
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + npe.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                        Toast.makeText(this, "Error: " + npe, Toast.LENGTH_LONG).show();
                }
        }

        private void onTextChanged() {
                // Arraylist con datos filtrados
                final ArrayList<String> array_sort = new ArrayList<>();
                myAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringListCB);
                lvListaCuelloBotella = findViewById(R.id.lvCuelloBotella);
                lvListaCuelloBotella.setAdapter(myAdapter);
                // Inicializamos textview de busqueda
                tvBuscarReporteCB = findViewById(R.id.txtBuscarReporteCB);
                tvBuscarReporteCB.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                int textlength = tvBuscarReporteCB.getText().length();
                                tvBuscarReporteCB.setAllCaps(false);
                                array_sort.clear();
                                for (int i = 0; i < stringListCB.size(); i++) {
                                        if (textlength <= stringListCB.get(i).length()) {
                                                if (stringListCB.get(i).contains(tvBuscarReporteCB.getText().toString())) {
                                                        array_sort.add(stringListCB.get(i));
                                                }
                                        }
                                }
                                lvListaCuelloBotella.setAdapter(new SelectionAdapter(VwListarCuelloBotella.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                });
        }

        private void setUpActionBar() {
                lvListaCuelloBotella.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                lvListaCuelloBotella.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                        @Override
                        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                                if (checked) {
                                        mdCuelloBotella = listCuelloBotella.get(position);
                                        myAdapter.setNewSelection(position);
                                } else {
                                        mdCuelloBotella = new MdCuelloBotella();
                                        myAdapter.removeSelection(position);
                                }
                                mode.setTitle(myAdapter.getSelectionCount() + " Item seleccionado");
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                                // CAB menu options
                                if (item.getItemId() == R.id.delete) {
                                        Toast.makeText(VwListarCuelloBotella.this,
                                                myAdapter.getSelectionCount() + " Item eliminado",
                                                Toast.LENGTH_LONG).show();
                                        myAdapter.clearSelection();
                                        mode.finish();
                                        return true;
                                }
                                return false;
                        }

                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                                MenuInflater inflater = mode.getMenuInflater();
                                inflater.inflate(R.menu.main_cab, menu);
                                return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                                myAdapter.clearSelection();
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                                return false;
                        }
                });
        }

        private AlertDialog dialogDeleteUpdateCuelloBotella() {
                view = inflater.inflate(R.layout.dialog_new_update, null);

                final Button btnActualizar = view.findViewById(R.id.btnNuevo);
                btnActualizar.setText("Actualizar");

                final Button btnEliminar = view.findViewById(R.id.btnMasOpciones);
                btnEliminar.setText("Eliminar");

                btnActualizar.setOnClickListener(v -> {
                        //crear metodo para encontrar objeto determinado en un array y asignarlo a mdCuelloBotella (el seleccionado por el usuario)
                        mdCuelloBotella.setAccion(2);
                        bundle.putSerializable("cuellobotella", mdCuelloBotella);
                        intent.putExtras(bundle);
                        startActivity(intent);
                });
                btnEliminar.setOnClickListener(v -> {
                        mdCuelloBotella.setAccion(3);
                        bundle.putSerializable("cuellobotella", mdCuelloBotella);
                        intent.putExtras(bundle);
                        startActivity(intent);
                });

                builder.setView(view).setTitle("Elija una opción!").setPositiveButton("", (dialog, id) -> {
                        ((ViewGroup) drawerLayout.getParent()).removeView(view);
                });
                return builder.create();
        }
}