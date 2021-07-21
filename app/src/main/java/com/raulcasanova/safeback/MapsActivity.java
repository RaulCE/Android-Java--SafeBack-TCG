package com.raulcasanova.safeback;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Alerta;
import com.raulcasanova.safeback.Modelos.RutaTrayecto;
import com.raulcasanova.safeback.Modelos.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Usuario usuariosafeback;
    private Button btnFinalizar,btnAyuda;
    private FirebaseDatabase database;
    public DatabaseReference referenceEnTrayecto,referenceTrayecto,referenceGuardianesEnviar,referenceGuardianes;
    private MarkerOptions markerOptionsCurrent = new MarkerOptions();

    private int contadorParada=0,contadorEstimado=0,contadorMaximo=0;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            for(Location location: locationResult.getLocations()){

                contadorDialog(location,usuariosafeback.getTrayecto() );


                com.raulcasanova.safeback.Modelos.LatLng puntoCurrent = new com.raulcasanova.safeback.Modelos.LatLng(location.getLatitude(),location.getLongitude());
                usuariosafeback.getTrayecto().setPuntoTrayecto(puntoCurrent);

                referenceTrayecto.setValue(usuariosafeback.getTrayecto());


                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());


                markerOptionsCurrent.position(latLng);



            }
        }
    };

    private void toaster() {
        Toast.makeText(this," " , Toast.LENGTH_SHORT).show();
    }

    private void contadorDialog(Location location, RutaTrayecto trayecto){
        contadorEstimado+=4000;
        contadorMaximo+=4000;
        if (contadorEstimado==usuariosafeback.getTiempoEstimado()){
            ayudaDialog();
        }
        if (contadorMaximo==usuariosafeback.getTiempoMax()){
            crearYEnviarAlerta();
            llamadaAction();
            finish();
        }
        com.raulcasanova.safeback.Modelos.LatLng latLngLoc= new com.raulcasanova.safeback.Modelos.LatLng(location.getLatitude(),location.getLongitude());
        if (latLngLoc.getLatitude().equals(trayecto.getPuntoTrayecto().getLatitude()) && latLngLoc.getLongitude().equals(trayecto.getPuntoTrayecto().getLongitude())){
            contadorParada+=4000;
            if (contadorParada==usuariosafeback.getTiempoParada()){
                ayudaDialog();
            }
        }else {
            contadorParada=0;
        }

    }
    private void llamadaAction(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: "+usuariosafeback.getNumeroEmergencia()));
        startActivity(intent);
    }

    private void ayudaDialog() {


        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /////make map clear
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.setContentView(R.layout.dialog_ayuda);
        Button btnAyuda = dialog.findViewById(R.id.btnAyuda);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearYEnviarAlerta();
                llamadaAction();
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


        dialog.show();
     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                if (dialog.isShowing()){
                    dialog.dismiss();
                    crearYEnviarAlerta();
                    llamadaAction();
                    finish();

                }
            }
        }, 30000);*/


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        usuariosafeback=getIntent().getExtras().getParcelable("USUARIOLOG");
        usuariosafeback.setTiempoEstimado(usuariosafeback.getTiempoEstimado()*60*1000);
        usuariosafeback.setTiempoMax(usuariosafeback.getTiempoMax()*60*1000);
        usuariosafeback.setTiempoParada(usuariosafeback.getTiempoParada()*60*1000);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest= LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        btnFinalizar =findViewById(R.id.btnFinalizarMapa);
        btnAyuda = findViewById(R.id.btnAtrasVerMaps);



        database=FirebaseDatabase.getInstance();
        markerOptionsCurrent = new MarkerOptions();

        referenceTrayecto = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("trayecto");
        referenceTrayecto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    usuariosafeback.setTrayecto(snapshot.getValue(RutaTrayecto.class));
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
                    usuariosafeback.getListaGuardianes().clear();
                    usuariosafeback.getListaGuardianes().addAll(snapshot.getValue(gti));
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
                    usuariosafeback.setEnTrayecto(snapshot.getValue(Boolean.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        for (int i = 0; i <usuariosafeback.getListaGuardianes().size() ; i++) {
            referenceGuardianesEnviar = database.getReference("usuarios").child(usuariosafeback.getListaGuardianes().get(i).getId()).child("alertas");
            int finalI = i;
            referenceGuardianesEnviar.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        GenericTypeIndicator<ArrayList<Alerta>> gti = new GenericTypeIndicator<ArrayList<Alerta>>() {};
                        usuariosafeback.getListaGuardianes().get(finalI).setListaAlertas(snapshot.getValue(gti));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
       mapFragment.getMapAsync(this);

       btnFinalizar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               usuariosafeback.setEnTrayecto(false);
               referenceEnTrayecto.setValue(usuariosafeback.getEnTrayecto());
               finish();
           }
       });
       btnAyuda.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              ayudaDialog();
           }
       });
    }

    private void crearYEnviarAlerta() {
        Alerta alerta = new Alerta();
        alerta.setRutaAlerta(usuariosafeback.getTrayecto());
        alerta.setNombreSupervisado(Configuracion.userLoged.getDisplayName());
        alerta.setEmailSupervisado(Configuracion.userLoged.getEmail());

        for (int i = 0; i < usuariosafeback.getListaGuardianes().size(); i++) {

            referenceGuardianesEnviar = database.getReference("usuarios").child(usuariosafeback.getListaGuardianes().get(i).getId()).child("alertas");


            usuariosafeback.getListaGuardianes().get(i).getListaAlertas().add(alerta);
            referenceGuardianesEnviar.setValue( usuariosafeback.getListaGuardianes().get(i).getListaAlertas());

        }
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.clear();

        LatLng latLngOri= new LatLng(usuariosafeback.getTrayecto().getPuntoOrigen().getLatitude(),usuariosafeback.getTrayecto().getPuntoOrigen().getLongitude());
        LatLng latLng= new LatLng(usuariosafeback.getTrayecto().getPuntoDestino().getLatitude(),usuariosafeback.getTrayecto().getPuntoDestino().getLongitude());



        LatLng latLngCurrent=new LatLng(usuariosafeback.getTrayecto().getPuntoTrayecto().getLatitude(),usuariosafeback.getTrayecto().getPuntoTrayecto().getLongitude());


        MarkerOptions markerOptionsDes = new MarkerOptions();
        markerOptionsDes.position(latLng);
        markerOptionsDes.title("Destino");

        MarkerOptions markerOptionsOri = new MarkerOptions();
        markerOptionsOri.position(latLngOri);
        markerOptionsOri.title("Origen");


        markerOptionsCurrent.position(latLngCurrent);
        markerOptionsCurrent.title("Posicion Actual");



        googleMap.clear();
        googleMap.addMarker(markerOptionsOri);
        googleMap.addMarker(markerOptionsDes);
        googleMap.addMarker(markerOptionsCurrent);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCurrent));


    }

    @Override
    protected void onStart() {
        super.onStart();
        configurarYEmpezar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuariosafeback.setEnTrayecto(false);
        referenceEnTrayecto.setValue(usuariosafeback.getEnTrayecto());
        pararActualizacionesDeUbicacion();
    }

    private void configurarYEmpezar() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build() ;
        SettingsClient client= LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask=client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                empezarActualizacionesDeUbicacion();
            }
        });
    }

    private void empezarActualizacionesDeUbicacion() {

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
    }

    private void pararActualizacionesDeUbicacion(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    };
}