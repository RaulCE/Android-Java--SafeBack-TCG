package com.raulcasanova.safeback;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Adapters.RutasAdapter;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Ruta;
import com.raulcasanova.safeback.Modelos.RutaTrayecto;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class RutasFragment extends Fragment{


    private final int ADD_RUTA =0 ;
    private FloatingActionButton fab;
    private RecyclerView rvRutas;
    private RutasAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private SearchView svSearch;
    private FirebaseDatabase database;
    public DatabaseReference referenceTiempoEstimado
            ,referenceTiempoMax
            ,referenceTiempoParada
            ,referenceNumeroEmergencia,
            referenceRutas;
    private Usuario usuariosb;

    public RutasFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RutasFragment newInstance(String param1, String param2) {
        RutasFragment fragment = new RutasFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rutas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


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
        referenceRutas = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("rutas");
        referenceRutas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Ruta>> gti = new GenericTypeIndicator<ArrayList<Ruta>>() {};
                    usuariosb.getListaRutas().clear();
                    usuariosb.getListaRutas().addAll(snapshot.getValue(gti));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        rvRutas = getView().findViewById(R.id.recyclerRutas);
        fab = getView().findViewById(R.id.fab);


        adapter=new RutasAdapter(usuariosb.getListaRutas(),R.layout.rcc_rutas,((MainActivity) getActivity()),usuariosb);

        linearLayoutManager = new LinearLayoutManager(((MainActivity) getActivity()));
        rvRutas.setLayoutManager(linearLayoutManager);
        rvRutas.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("USUARIOLOG", usuariosb);
                Intent intent = new Intent(getActivity(), AddRuta.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,ADD_RUTA);
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== RESULT_OK){
            if (requestCode==ADD_RUTA){
                if (data != null && data.getExtras() != null){
                    Ruta ruta = data.getExtras().getParcelable("RUTA");
                    if (ruta != null) {
                        usuariosb.getListaRutas().add(ruta);
                        referenceRutas.setValue(usuariosb.getListaRutas());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}