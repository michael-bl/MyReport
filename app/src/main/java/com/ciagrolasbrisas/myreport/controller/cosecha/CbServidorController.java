package com.ciagrolasbrisas.myreport.controller.cosecha;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.model.MdCuelloBotella;
import com.ciagrolasbrisas.myreport.model.MdWarning;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CbServidorController {
        private ArrayList<MdCuelloBotella> listCuelloBotella;
        private String date, time, clase;
        private LogGenerator logGenerator;
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        public CbServidorController() {
        }

        public ArrayList<MdCuelloBotella> crudCuelloBotella(Context context, String json, int opcion) {

                clase = this.getClass().getSimpleName();

                GetStringDate stringDate = new GetStringDate();
                GetStringTime stringTime = new GetStringTime();
                date = stringDate.getFecha();
                time = stringTime.getHora();

                logGenerator = new LogGenerator();
                OkHttpClient client = new OkHttpClient();
                ConnectivityService con = new ConnectivityService();

                if (con.stateConnection(context)) {

                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

                        Request request = new Request.Builder()
                                .url("https://reportes.ciagrolasbrisas.com/cuelloBotellaCos.php")
                                .post(requestBody)
                                .build();

                        // ExecutorService ejecuta la tarea en segundo plano
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                        try {
                                                okhttp3.Response response = client.newCall(request).execute();

                                                if (response.isSuccessful()) {
                                                        final String responseBody = response.body().string();

                                                        // Manejar la respuesta
                                                        mainHandler.post(() -> {
                                                                Gson gson = new Gson();

                                                                if (opcion == 0 || opcion == 2 || opcion == 3) { // Devuelven estado y mesaje de error

                                                                        Type listType = new TypeToken<MdWarning>() {
                                                                        }.getType();

                                                                        MdWarning mensaje = gson.fromJson(responseBody, listType);
                                                                        if (!mensaje.getStatus().equals("1")) {
                                                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + mensaje.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                                                                        }
                                                                        Toast.makeText(context, mensaje.getMessage(), Toast.LENGTH_SHORT).show();

                                                                } else {
                                                                        // opciones 1-listar, 4-listarPendientes y 5-horasEfectivas, devuelven lista de reportes
                                                                        Type listType = new TypeToken<List<MdCuelloBotella>>() {
                                                                        }.getType();
                                                                        listCuelloBotella = gson.fromJson(responseBody, listType);
                                                                }
                                                        });
                                                } else {
                                                        // Imprimir error en la respuesta
                                                        mainHandler.post(() -> {
                                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + response.message()); // Agregamos el error al archivo Descargas/Logs.txt
                                                        });
                                                }
                                        } catch (IOException e) {
                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName() + ": " + e.getMessage()); // Agregamos el error al archivo Descargas/Logs.txt
                                        }
                                }
                        });

                        // Apagar el ExecutorService después de su uso
                        executor.shutdown();
                }
                return listCuelloBotella;
        }
}