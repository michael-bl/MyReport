package com.ciagrolasbrisas.myreport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.DialogNewUpdate;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.databinding.VwMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class VwMain extends AppCompatActivity {

        private AppBarConfiguration mAppBarConfiguration;
        private DialogNewUpdate dialogNewOrUpdate;
        private VwMainBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                binding = VwMainBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                setSupportActionBar(binding.appBarMain.toolbar);
                binding.appBarMain.fab.setOnClickListener(view ->
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show());
                DrawerLayout drawer = binding.drawerLayout;
                NavigationView navigationView = binding.navView;
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                        .setOpenableLayout(drawer)
                        .build();
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
                navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

        @Override
        public boolean onSupportNavigateUp() {
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                        || super.onSupportNavigateUp();
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                DatabaseController dbController;
                Intent intent;
                int id = item.getItemId();
                dialogNewOrUpdate = new DialogNewUpdate(this);
                if (id == R.id.nav_usuario) {
                        //dialogNewOrUpdate.dialogNewUpdate().show();
                        intent = new Intent(this, VwUsuario.class);
                        startActivity(intent);
                }
                if (id == R.id.nav_reporte) {
                        dialogNewOrUpdate.dialogNewUpdate().show();
                }
                if (id == R.id.nav_sincronizar) {
                        //dialogNewOrUpdate.dialogNewUpdate().show();
                }
                if (id == R.id.nav_compartir) {
                         intent = new Intent(this, VwBuscarReporte.class);
                         startActivity(intent);
                }
                if (id == R.id.nav_configuracion) {

                }
                return super.onOptionsItemSelected(item);
        }
}