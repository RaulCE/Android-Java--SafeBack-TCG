package com.raulcasanova.safeback;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Adapters.AlertasAdapter;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Alerta;
import com.raulcasanova.safeback.Modelos.Guardian;
import com.raulcasanova.safeback.Modelos.Ruta;
import com.raulcasanova.safeback.Modelos.RutaTrayecto;
import com.raulcasanova.safeback.Modelos.Supervisado;
import com.raulcasanova.safeback.Modelos.Usuario;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //Datos usuario
     Usuario usuariosafeBack;
     FirebaseAuth mAuth;


    //FireBaseRTDB
    private FirebaseDatabase database;
    public DatabaseReference referenceTrayectos,
            referenceAlertas
            ,referenceRutas
            ,referenceGuardianes
            ,referenceSupervisados
            ,referencePeticiones
            ,referenceTrayecto
            ,referenceEnTrayecto
            ,referenceNombreUsuario
            ,referenceEmailUsuario
            ,referenceTiempoEstimado
            ,referenceTiempoMax
            ,referenceTiempoParada
            ,referenceNumeroEmergencia;




    private AppBarConfiguration mAppBarConfiguration;

    //Perms
    private final int OPEN_INTERNET_PERMISION=0;
    private final int OPEN_ESTORAGE_PERMISION=1;
    private final int OPEN_GSERVICES_PERMISION=2;
    private final int OPEN_COARSELOACTION_PERMISION=3;
    private final int OPEN_FINELOCATION_PERMISION=4;
    private final int OPEN_CALLPHONE_PERMISION=5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_ajustes,/*R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,*/R.id.nav_rutas,R.id.nav_maps,R.id.nav_guardianes,R.id.nav_vigilancia,R.id.nav_alertas,R.id.nav_trayectos)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        pedirPermisos();

        usuariosafeBack=new Usuario();

        usuariosafeBack.setId(Configuracion.userLoged.getUid());
        referenceEmailUsuario = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("id");
        referenceEmailUsuario.setValue(usuariosafeBack.getId());

        usuariosafeBack.setEmailUsuario(Configuracion.userLoged.getEmail());
        referenceEmailUsuario = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("emailUsuario");
        referenceEmailUsuario.setValue(usuariosafeBack.getEmailUsuario());

        usuariosafeBack.setNombreUsuario(Configuracion.userLoged.getDisplayName());
        referenceNombreUsuario = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("nombreUsuario");
        referenceNombreUsuario.setValue(usuariosafeBack.getNombreUsuario());

        referenceRutas = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("rutas");
        referenceRutas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   GenericTypeIndicator<ArrayList<Ruta>> gti = new GenericTypeIndicator<ArrayList<Ruta>>() {};
                   usuariosafeBack.getListaRutas().clear();
                   usuariosafeBack.getListaRutas().addAll(snapshot.getValue(gti));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referenceAlertas = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("alertas");
        referenceAlertas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Alerta>> gti = new GenericTypeIndicator<ArrayList<Alerta>>() {};
                    usuariosafeBack.getListaAlertas().clear();
                    usuariosafeBack.getListaAlertas().addAll(snapshot.getValue(gti));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        referenceTrayectos = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("trayectos");
        referenceTrayectos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                    usuariosafeBack.getListaTrayectos().clear();
                    usuariosafeBack.getListaTrayectos().addAll(snapshot.getValue(gti));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        referenceGuardianes = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("guardianes");
        referenceGuardianes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                    usuariosafeBack.getListaGuardianes().clear();
                    usuariosafeBack.getListaGuardianes().addAll(snapshot.getValue(gti));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        referenceSupervisados = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("supervisados");
        referenceSupervisados.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                    usuariosafeBack.getListaSupervisados().clear();
                    usuariosafeBack.getListaSupervisados().addAll(snapshot.getValue(gti));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        referencePeticiones = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("listaPeticionesSupervisados");
        referencePeticiones.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                   usuariosafeBack.getListaPeticionesSupervisados().clear();
                   usuariosafeBack.getListaPeticionesSupervisados().addAll(snapshot.getValue(gti));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        referenceTrayecto = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("trayecto");
        referenceTrayecto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   usuariosafeBack.setTrayecto(snapshot.getValue(RutaTrayecto.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        referenceEnTrayecto = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("enTrayecto");
        referenceEnTrayecto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   usuariosafeBack.setEnTrayecto(snapshot.getValue(Boolean.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        referenceTiempoEstimado = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("tiempoEstimado");
        referenceTiempoEstimado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    usuariosafeBack.setTiempoEstimado(snapshot.getValue(Float.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        referenceTiempoMax = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("tiempoMaximo");
        referenceTiempoMax.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    usuariosafeBack.setTiempoMax(snapshot.getValue(Float.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        referenceTiempoParada = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("tiempoParada");
        referenceTiempoParada.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    usuariosafeBack.setTiempoParada(snapshot.getValue(Float.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        referenceNumeroEmergencia = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("numeroEmergencia");
        referenceNumeroEmergencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    usuariosafeBack.setNumeroEmergencia(snapshot.getValue(Integer.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        referenceEnTrayecto.setValue(false);
    }

    private void pedirPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, OPEN_COARSELOACTION_PERMISION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, OPEN_FINELOCATION_PERMISION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, OPEN_CALLPHONE_PERMISION);
        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        referenceEnTrayecto.setValue(false);
    }
}