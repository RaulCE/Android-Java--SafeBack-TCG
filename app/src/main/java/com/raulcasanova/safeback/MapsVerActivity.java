package com.raulcasanova.safeback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Alerta;
import com.raulcasanova.safeback.Modelos.RutaTrayecto;

public class MapsVerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Alerta alerta;
    private Button btnAtras;
    private RutaTrayecto rutaVer;
    private FirebaseDatabase database;
    public DatabaseReference referencePuntoSupervisado;
    private MarkerOptions markerOptionsCurrent = new MarkerOptions();
    private String idSupervisado="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_ver);
        Toolbar toolbar = findViewById(R.id.toolbarVerMaps);
        btnAtras=findViewById(R.id.btnAtrasVerMaps);

        if (getIntent().getExtras().getString("DONDEVENGO").equals("Alertas")){
            alerta=getIntent().getExtras().getParcelable("ALERTA");
           toolbar.setTitle(alerta.getEmailSupervisado());
            setSupportActionBar(toolbar);
            rutaVer = alerta.getRutaAlerta();
        }else {
            toolbar.setTitle(getIntent().getExtras().getString("EMAIL"));
            setSupportActionBar(toolbar);
            rutaVer = getIntent().getExtras().getParcelable("TRAYECTO");
            idSupervisado=getIntent().getExtras().getString("IDSUPERVISADO");
        }

        database=FirebaseDatabase.getInstance();
        referencePuntoSupervisado = database.getReference("usuarios").child(idSupervisado).child("trayecto");
        referencePuntoSupervisado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    rutaVer=snapshot.getValue(RutaTrayecto.class);
                    LatLng latLng=new LatLng(rutaVer.getPuntoTrayecto().getLatitude(),rutaVer.getPuntoTrayecto().getLongitude());
                    markerOptionsCurrent.position(latLng);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        markerOptionsCurrent = new MarkerOptions();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        LatLng latLngOri= new LatLng(rutaVer.getPuntoOrigen().getLatitude(),rutaVer.getPuntoOrigen().getLongitude());
        LatLng latLngDes= new LatLng(rutaVer.getPuntoDestino().getLatitude(),rutaVer.getPuntoDestino().getLongitude());

        LatLng latLngCurrent=new LatLng(rutaVer.getPuntoTrayecto().getLatitude(),rutaVer.getPuntoTrayecto().getLongitude());


        MarkerOptions markerOptionsDes = new MarkerOptions();
        markerOptionsDes.position(latLngDes);
        markerOptionsDes.title("Destino");

        MarkerOptions markerOptionsOri = new MarkerOptions();
        markerOptionsOri.position(latLngOri);
        markerOptionsOri.title("Origen");

        markerOptionsCurrent.position(latLngCurrent);
        markerOptionsCurrent.title("Supervisado");

        googleMap.clear();
        googleMap.addMarker(markerOptionsOri);
        googleMap.addMarker(markerOptionsDes);
        googleMap.addMarker(markerOptionsCurrent);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCurrent));


    }
}