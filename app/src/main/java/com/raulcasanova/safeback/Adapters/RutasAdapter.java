package com.raulcasanova.safeback.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.AddGuardian;
import com.raulcasanova.safeback.AddRuta;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.MainActivity;
import com.raulcasanova.safeback.MapsActivity;
import com.raulcasanova.safeback.Modelos.Ruta;
import com.raulcasanova.safeback.Modelos.RutaTrayecto;
import com.raulcasanova.safeback.Modelos.Usuario;
import com.raulcasanova.safeback.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RutasAdapter extends RecyclerView.Adapter<RutasAdapter.RutaVH>{


    private Usuario usuariosb;
    private List<Ruta> objects;
    private List<Ruta> originalObjects;
    private int resource;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference referenceRutas,referenceEnTrayecto,referenceTrayecto, referenceNumeroEmergencia;

    public RutasAdapter(List<Ruta> objects, int resource, Context context, Usuario usuario) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        this.originalObjects=new ArrayList<>();
       originalObjects.addAll(objects);
       database=FirebaseDatabase.getInstance();
       referenceRutas = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("rutas");
       this.usuariosb=usuario;
    }

   /* public void filtro(String busqueda){
        if (busqueda.length() == 0 ){
            objects.clear();
            objects.addAll(originalObjects);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                objects.clear();
                List<Ruta> collect= originalObjects.stream().filter(ruta -> ruta.getNombre().toLowerCase().contains(busqueda))
                .collect(Collectors.toList());

                objects.addAll(collect);
            }
        }
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public RutasAdapter.RutaVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementoRuta = LayoutInflater.from(context).inflate(resource, null);
        elementoRuta.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RutaVH rutaVH = new RutaVH(elementoRuta);
        return rutaVH;
    }

    @Override
    public void onBindViewHolder(@NonNull RutasAdapter.RutaVH holder, int position) {

        objects.get(position);
        holder.txtNombreRuta.setText(objects.get(position).getNombre().toString());


        LatLng latLng = new LatLng(objects.get(position).getPuntoOrigen().getLatitude(),objects.get(position).getPuntoOrigen().getLongitude());
        Geocoder geocoder1 = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder1.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!direcciones.isEmpty()){
                Address address = direcciones.get(0);
                String dir = address.getAddressLine(0);
                holder.txtcalleOrigen.setText(dir);
            }
            else {
                holder.txtcalleOrigen.setText("No hay direcciones Disponibles");
            }
        } catch (IOException e) {
            holder.txtcalleOrigen.setText("ERROR: al obtener la dirección");
        }


        LatLng latLng2 = new LatLng(objects.get(position).getPuntoDestino ().getLatitude(),objects.get(position).getPuntoDestino().getLongitude());
        Geocoder geocoder2 = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder2.getFromLocation(latLng2.latitude, latLng2.longitude, 1);
            if (!direcciones.isEmpty()){
                Address address = direcciones.get(0);
                String dir = address.getAddressLine(0);
                holder.txtcalleDestino.setText(dir);
            }
            else {
                holder.txtcalleDestino.setText("No hay direcciones Disponibles");
            }
        } catch (IOException e) {
            holder.txtcalleDestino.setText("ERROR: al obtener la dirección");
        }


        holder.btnopciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Dialog alert = new Dialog(context);
                View contenido = LayoutInflater.from(context).inflate(R.layout.activity_add_ruta, null);
                alert.setContentView(contenido);

                 TextView txtCalleOrig, txtCalleFin;
                 TextView txtNombreRuta,txtTiempoEst,txtTiempoMax, txtTiempoParada;
                 Button btnCancelar,btnGuardar;
                 ImageView btnSelecOrig,btnSelecFin;
                Toolbar toolbar = contenido.findViewById(R.id.toolbarAddRutas);
                toolbar.setVisibility(View.GONE);

                txtNombreRuta = contenido.findViewById(R.id.txtEditNombreRuta);
                txtCalleOrig = contenido.findViewById(R.id.txtRutaOrigen);
                txtCalleFin = contenido.findViewById(R.id.txtRutaDestino);
                txtTiempoEst= contenido.findViewById(R.id.txtTiempoEst);
                txtTiempoMax= contenido.findViewById(R.id.txtTiempoMax);
                txtTiempoParada= contenido.findViewById(R.id.txtTiempoParada);
                btnSelecOrig= contenido.findViewById(R.id.btnMapaOrigen);
                btnSelecFin= contenido.findViewById(R.id.btnMapaDestino);
                btnGuardar= contenido.findViewById(R.id.btnAddRutaGuardar);
                btnCancelar= contenido.findViewById(R.id.btnAddRutaCancelar);

                txtNombreRuta.setText(objects.get(position).getNombre()+"");
                txtTiempoEst.setText(objects.get(position).getTiempoEstimado()+"");
                txtTiempoMax.setText(objects.get(position).getTiempoMax()+"");
                txtTiempoParada.setText(objects.get(position).getTiempoParada()+"");


                LatLng latLng = new LatLng(objects.get(position).getPuntoOrigen().getLatitude(),objects.get(position).getPuntoOrigen().getLongitude());
                Geocoder geocoder1 = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> direcciones = geocoder1.getFromLocation(latLng.latitude, latLng.longitude, 1);
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


                LatLng latLng2 = new LatLng(objects.get(position).getPuntoDestino ().getLatitude(),objects.get(position).getPuntoDestino().getLongitude());
                Geocoder geocoder2 = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> direcciones = geocoder2.getFromLocation(latLng2.latitude, latLng2.longitude, 1);
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


                btnSelecOrig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog dialog = new Dialog(context);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        /////make map clear
                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                        dialog.setContentView(R.layout.dialog_map);////your custom content
                        Button btnDialSelec = dialog.findViewById(R.id.btnSelecMapaAceptar);

                        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
                        MapsInitializer.initialize(context);

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
                                                objects.get(position).getPuntoOrigen().setLatitude(latLng.latitude);
                                                objects.get(position).getPuntoOrigen().setLongitude(latLng.longitude);
                                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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

                        final Dialog dialog = new Dialog(context);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        /////make map clear
                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                        dialog.setContentView(R.layout.dialog_map);////your custom content
                        Button btnDialSelec = dialog.findViewById(R.id.btnSelecMapaAceptar);

                        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
                        MapsInitializer.initialize(context);

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
                                                objects.get(position).getPuntoDestino().setLatitude(latLng.latitude);
                                                objects.get(position).getPuntoDestino().setLongitude(latLng.longitude);
                                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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

                        if (!txtNombreRuta.getText().toString().equals("") &&  objects.get(position).getPuntoOrigen()!=null &&  objects.get(position).getPuntoDestino()!=null){
                            objects.get(position).setNombre(txtNombreRuta.getText().toString());
                            if (!txtTiempoEst.getText().toString().equals("")){
                                objects.get(position).setTiempoEstimado(Float.parseFloat(txtTiempoEst.getText().toString()));
                            }else {
                                objects.get(position).setTiempoEstimado(usuariosb.getTiempoEstimado());
                            }
                            if (!txtTiempoMax.getText().toString().equals("")){
                                objects.get(position).setTiempoMax(Float.parseFloat(txtTiempoMax.getText().toString()));
                            }else {
                                objects.get(position).setTiempoMax(usuariosb.getTiempoMax());
                            }
                            if (!txtTiempoParada.getText().toString().equals("")){
                                objects.get(position).setTiempoParada(Float.parseFloat(txtTiempoParada.getText().toString()));
                            }else {
                                objects.get(position).setTiempoParada(usuariosb.getTiempoParada());
                            }

                            referenceRutas.setValue(objects);
                            notifyDataSetChanged();
                            alert.dismiss();

                        }else {
                            Toast.makeText(context,"La Ruta necesita un nombre, punto de origen y punto de estino",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                alert.show();

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
        referenceRutas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Ruta>> gti = new GenericTypeIndicator<ArrayList<Ruta>>() {};
                    objects.clear();
                    objects.addAll(snapshot.getValue(gti));
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

        holder.btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.remove(position);
                referenceRutas.setValue(objects);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuariosb.getTrayecto().setPuntoOrigen(objects.get(position).getPuntoOrigen());
                usuariosb.getTrayecto().setPuntoDestino(objects.get(position).getPuntoDestino());
                usuariosb.getTrayecto().setPuntoTrayecto(objects.get(position).getPuntoOrigen());
                usuariosb.getTrayecto().setTiempoEstimado(objects.get(position).getTiempoEstimado());
                usuariosb.getTrayecto().setTiempoMax(objects.get(position).getTiempoMax());
                usuariosb.getTrayecto().setTiempoParada(objects.get(position).getTiempoParada());


                referenceTrayecto.setValue(usuariosb.getTrayecto());

                usuariosb.setEnTrayecto(true);
                referenceEnTrayecto.setValue(usuariosb.getEnTrayecto());
                Bundle bundle = new Bundle();
                bundle.putParcelable("USUARIOLOG", usuariosb);
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class RutaVH extends RecyclerView.ViewHolder {

        TextView txtNombreRuta,txtcalleOrigen,txtcalleDestino;
        Button btnopciones,btneliminar;

        public RutaVH(@NonNull View itemView) {
            super(itemView);

            txtNombreRuta=itemView.findViewById(R.id.txtNombreRuta);
            txtcalleOrigen = itemView.findViewById(R.id.txtCalleOrigen);
            txtcalleDestino = itemView.findViewById(R.id.txtCalleDestino);
            btnopciones = itemView.findViewById(R.id.btnEditarRuta);
            btneliminar = itemView.findViewById(R.id.btnEliminarRuta);
        }
    }
}
