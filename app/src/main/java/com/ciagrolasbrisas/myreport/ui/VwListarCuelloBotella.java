package com.ciagrolasbrisas.myreport.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.controller.SelectionAdapter;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.database.ExistSqliteDatabase;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.google.gson.Gson;

import org.etsi.uri.x01903.v13.impl.IdentifierTypeImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class VwListarCuelloBotella extends AppCompatActivity {
        private ArrayList<MdCuelloBotella> listMdCuelloBotella;
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
        private String date, time;
        private boolean localMode;
        private LogGenerator logGenerator;
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_listar_cuellobotella);

                 localMode = VwLogin.localMode;

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
                                if(mdCuelloBotella.getCode()!=null){
                                        dialogDeleteUpdateCuelloBotella().show();
                                } else{
                                        Toast.makeText(this, "Advertencia: debes seleccionar un reporte para poder continuar!", Toast.LENGTH_LONG).show();
                                }
                        } catch (Exception e){
                                Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
                        }
                });
        }

        private void getCuellosPendientes() {

                if (!localMode) {
                        selectCuelloBotellaSinCerrarDbRemota();
                } else  {
                        existDb = new ExistSqliteDatabase();
                        if (existDb.ExistSqliteDatabase()) {
                                selectCuelloBotellaSinCerrarDbLocal();
                        }
                }
        }

        private void selectCuelloBotellaSinCerrarDbRemota() {
                logGenerator = new LogGenerator();
                OkHttpClient client = new OkHttpClient();
                ConnectivityService con = new ConnectivityService();

                if (con.stateConnection(this)) {
                        String json = new Gson().toJson("encargado:" + VwLogin.dniUser);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

                        Request request = new Request.Builder()
                                .url("https://reportes.ciagrolasbrisas.com/cuelloBotellaCos.php")
                                .post(requestBody)
                                .build();

                        // Usar un ExecutorService para ejecutar la tarea en segundo plano
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                        try {
                                                okhttp3.Response response = client.newCall(request).execute();

                                                if (response.isSuccessful()) {
                                                        final String responseBody = response.body().string();
                                                        // Manejar la respuesta
                                                        mainHandler.post(() -> Toast.makeText(VwListarCuelloBotella.this, responseBody, Toast.LENGTH_SHORT).show());
                                                } else {
                                                        // Imprimir error en la respuesta
                                                        mainHandler.post(() -> {
                                                                logGenerator.generateLogFile(date + ": " + time + ": " + response.message()); // agregamos el error al archivo Logs.txt
                                                                Toast.makeText(VwListarCuelloBotella.this, "Error en la solicitud: " + response.message(), Toast.LENGTH_SHORT).show();
                                                        });
                                                }
                                        } catch (IOException e) {
                                                logGenerator.generateLogFile(date + ": " + time + ": " + e.getMessage()); // agregamos el error al archivo Logs.txt
                                                e.printStackTrace();
                                        }
                                }
                        });

                        // Apagar el ExecutorService después de su uso
                        executor.shutdown();
                }
        }

        private void selectCuelloBotellaSinCerrarDbLocal() {
                try {
                        dbController = new DatabaseController();
                        listMdCuelloBotella = dbController.selectCuelloBotellaIncompleto(this);
                        stringListCB = new ArrayList<>();
                        for (MdCuelloBotella cb : listMdCuelloBotella) {
                                stringListCB.add(cb.getMotivo() + "-" + cb.getHora_final());
                        }
                        llenarListViewCuelloBotella(stringListCB);
                } catch (NullPointerException npe) {
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
                                        mdCuelloBotella = listMdCuelloBotella.get(position);
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
                        //crear metodo para encontrar objeto determinado en un array y asignarlo a mdCuelloBotella (el seleccionado por el usaurio)
                        mdCuelloBotella.setAccion(1);
                        bundle.putSerializable("cuellobotella", mdCuelloBotella);
                        intent.putExtras(bundle);
                        startActivity(intent);
                });
                btnEliminar.setOnClickListener(v -> {
                        mdCuelloBotella.setAccion(2);
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