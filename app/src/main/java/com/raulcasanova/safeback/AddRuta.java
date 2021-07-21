package com.raulcasanova.safeback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Ruta;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddRuta extends AppCompatActivity {


    private TextView txtCalleOrig, txtCalleFin;
    private TextView txtNombreRuta,txtTiempoEst,txtTiempoMax, txtTiempoParada;
    private Button btnCancelar,btnGuardar;
    private ImageView btnSelecOrig,btnSelecFin;
    private Ruta ruta;
    private Usuario usuariosafeback;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ruta);
        Toolbar toolbar = findViewById(R.id.toolbarAddRutas);
        setSupportActionBar(toolbar);

        usuariosafeback=getIntent().getExtras().getParcelable("USUARIOLOG");

        ruta=new Ruta();
        txtNombreRuta = findViewById(R.id.txtEditNombreRuta);
        txtCalleOrig = findViewById(R.id.txtRutaOrigen);
        txtCalleFin = findViewById(R.id.txtRutaDestino);
        txtTiempoEst= findViewById(R.id.txtTiempoEst);
        txtTiempoMax= findViewById(R.id.txtTiempoMax);
        txtTiempoParada= findViewById(R.id.txtTiempoParada);
        btnSelecOrig= findViewById(R.id.btnMapaOrigen);
        btnSelecFin= findViewById(R.id.btnMapaDestino);
        btnGuardar= findViewById(R.id.btnAddRutaGuardar);
        btnCancelar= findViewById(R.id.btnAddRutaCancelar);






        btnSelecOrig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AddRuta.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                /////make map clear
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                dialog.setContentView(R.layout.dialog_map);////your custom content
                Button btnDialSelec = dialog.findViewById(R.id.btnSelecMapaAceptar);

                MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
                MapsInitializer.initialize(AddRuta.this);

                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();




                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {

                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {


                                googleMap.clear();

                                MarkerOptions markerOptionsDes = new MarkerOptions();
                                markerOptionsDes.position(latLng);
                                markerOptionsDes.title("Origen");
                                googleMap.addMarker(markerOptionsDes);

                               btnDialSelec.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ruta.getPuntoOrigen().setLatitude(latLng.latitude);
                                        ruta.getPuntoOrigen().setLongitude(latLng.longitude);
                                        Geocoder geocoder = new Geocoder(AddRuta.this, Locale.getDefault());
                                        try {
                                            List<Address> direcciones = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                            if (!direcciones.isEmpty()){
                                                Address address = direcciones.get(0);
                                                String dir = address.getAddressLine(0);
                                                txtCalleOrig.setText(dir);
                                            }
                                            else {
                                                txtCalleOrig.setText("No hay direcciones Disponibles");
                                            }
                                        } catch (IOException e) {
                                            txtCalleOrig.setText("ERROR: al obtener la dirección");
                                        }
                                        dialog.dismiss();
                                    }
                                });
                                btnDialSelec.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                });
                dialog.show();
            }
        });
        btnSelecFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AddRuta.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                /////make map clear
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                dialog.setContentView(R.layout.dialog_map);////your custom content
                Button btnDialSelec = dialog.findViewById(R.id.btnSelecMapaAceptar);

                MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
                MapsInitializer.initialize(AddRuta.this);

                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();




                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {

                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {


                                googleMap.clear();

                                MarkerOptions markerOptionsDes = new MarkerOptions();
                                markerOptionsDes.position(latLng);
                                markerOptionsDes.title("Destino");
                                googleMap.addMarker(markerOptionsDes);

                               btnDialSelec.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ruta.getPuntoDestino().setLatitude(latLng.latitude);
                                        ruta.getPuntoDestino().setLongitude(latLng.longitude);
                                        Geocoder geocoder = new Geocoder(AddRuta.this, Locale.getDefault());
                                        try {
                                            List<Address> direcciones = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                            if (!direcciones.isEmpty()){
                                                Address address = direcciones.get(0);
                                                String dir = address.getAddressLine(0);
                                                txtCalleFin.setText(dir);
                                            }
                                            else {
                                                txtCalleFin.setText("No hay direcciones Disponibles");
                                            }
                                        } catch (IOException e) {
                                            txtCalleFin.setText("ERROR: al obtener la dirección");
                                        }
                                        dialog.dismiss();
                                    }
                                });
                                btnDialSelec.setVisibility(View.VISIBLE);

                            }
                        });

                    }
                });

                dialog.show();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!txtNombreRuta.getText().toString().equals("") && ruta.getPuntoOrigen()!=null && ruta.getPuntoDestino()!=null){
                    ruta.setNombre(txtNombreRuta.getText().toString());
                    if (!txtTiempoEst.getText().toString().equals("")){
                        ruta.setTiempoEstimado(Float.parseFloat(txtTiempoEst.getText().toString()));
                    }else {
                        ruta.setTiempoEstimado(usuariosafeback.getTiempoEstimado());
                    }
                    if (!txtTiempoMax.getText().toString().equals("")){
                        ruta.setTiempoMax(Float.parseFloat(txtTiempoMax.getText().toString()));
                    }else {
                        ruta.setTiempoMax(usuariosafeback.getTiempoMax());
                    }
                    if (!txtTiempoParada.getText().toString().equals("")){
                        ruta.setTiempoParada(Float.parseFloat(txtTiempoParada.getText().toString()));
                    }else {
                        ruta.setTiempoParada(usuariosafeback.getTiempoParada());
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("RUTA", ruta);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(AddRuta.this,"La Ruta necesita un nombre, punto de origen y punto de estino",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }
}