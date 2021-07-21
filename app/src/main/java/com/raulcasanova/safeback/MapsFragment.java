package com.raulcasanova.safeback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.raulcasanova.safeback.Modelos.RutaTrayecto;
import com.raulcasanova.safeback.Modelos.Usuario;

public class MapsFragment extends Fragment {


    private static final int OPEN_COARSELOACTION_PERMISION = 12;
    private static final int OPEN_FINELOCATION_PERMISION =13 ;
    private Button btnIniciar;
    private FirebaseDatabase database;
    public DatabaseReference referenceEnTrayecto, referenceTrayecto,referenceTiempoEstimado
            ,referenceTiempoMax
            ,referenceTiempoParada
            ,referenceNumeroEmergencia;
    private Usuario usuariosb;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            googleMap.clear();
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    //punto origen




                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location==null){
                            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }

                    LatLng latLngOri=new LatLng(location.getLatitude(),location.getLongitude());
                    com.raulcasanova.safeback.Modelos.LatLng latLngOriPers= new com.raulcasanova.safeback.Modelos.LatLng(latLngOri.latitude,latLngOri.longitude);


                    com.raulcasanova.safeback.Modelos.LatLng latLngDesPers= new com.raulcasanova.safeback.Modelos.LatLng(latLng.latitude,latLng.longitude);



                    RutaTrayecto trayecto= new RutaTrayecto();
                    trayecto.setPuntoOrigen(latLngOriPers);
                    trayecto.setPuntoDestino(latLngDesPers);
                    trayecto.setPuntoTrayecto(latLngOriPers);

                    trayecto.setTiempoEstimado(usuariosb.getTiempoEstimado());
                    trayecto.setTiempoMax(usuariosb.getTiempoMax());
                    trayecto.setTiempoParada(usuariosb.getTiempoParada());


                    usuariosb.setTrayecto(trayecto);


                    MarkerOptions markerOptionsDes = new MarkerOptions();
                    markerOptionsDes.position(latLng);
                    markerOptionsDes.title("Destino");

                    MarkerOptions markerOptionsOri = new MarkerOptions();
                    markerOptionsOri.position(latLngOri);
                    markerOptionsOri.title("Origen");



                    //limpiar mapa, establecer marcadores y enseñar botones
                    googleMap.clear();
                    googleMap.addMarker(markerOptionsOri);
                    googleMap.addMarker(markerOptionsDes);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    btnIniciar.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    @Nullable


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        usuariosb=new Usuario();
        database = FirebaseDatabase.getInstance();
        referenceTiempoEstimado = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("tiempoEstimado");
        referenceTiempoEstimado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   usuariosb.setTiempoEstimado(snapshot.getValue(Float.class));
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
                    usuariosb.setTiempoMax(snapshot.getValue(Float.class));
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
                    usuariosb.setTiempoParada(snapshot.getValue(Float.class));
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
                  usuariosb.setNumeroEmergencia(snapshot.getValue(Integer.class));
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
                    usuariosb.setTrayecto(snapshot.getValue(RutaTrayecto.class));
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
                    usuariosb.setEnTrayecto(snapshot.getValue(Boolean.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnIniciar = (Button) getView().findViewById(R.id.btnAtrasVerMaps);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuariosb.setEnTrayecto(true);
                referenceEnTrayecto.setValue(usuariosb.getEnTrayecto());
                referenceTrayecto.setValue(usuariosb.getTrayecto());

                Bundle bundle = new Bundle();
                bundle.putParcelable("USUARIOLOG", ((MainActivity) getActivity()).usuariosafeBack);
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        btnIniciar.setVisibility(View.GONE);
    }
}