package com.raulcasanova.safeback;

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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Adapters.AlertasAdapter;
import com.raulcasanova.safeback.Adapters.TrayectosAdapter;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.sql.Struct;
import java.util.ArrayList;

public class TrayectosFragment extends Fragment { private FloatingActionButton fab;
    private RecyclerView rvTrayectos;
    private TrayectosAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private SearchView svSearch;
    private FirebaseDatabase database;
    public DatabaseReference referenceSupervisados,referenceTrayectos,referenceTrayecto;
    private ArrayList<Usuario> listaTrayectos, listaSupervisados;


    public TrayectosFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TrayectosFragment newInstance(String param1, String param2) {
        TrayectosFragment fragment = new TrayectosFragment();

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
        return inflater.inflate(R.layout.fragment_trayectos, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        rvTrayectos = getView().findViewById(R.id.recyclerTrayectos);
        // svSearch= getView().findViewById(R.id.svSearchRutas);
        //svSearch.setOnQueryTextListener(this);


        listaSupervisados=new ArrayList<>();
        listaTrayectos=new ArrayList<>();


        adapter=new TrayectosAdapter(listaTrayectos,R.layout.rcc_trayectos,((MainActivity) getActivity()));

        linearLayoutManager = new LinearLayoutManager(((MainActivity) getActivity()));
        rvTrayectos.setLayoutManager(linearLayoutManager);
        rvTrayectos.setAdapter(adapter);


        database = FirebaseDatabase.getInstance();
        referenceTrayectos = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("trayectos");
        referenceTrayectos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                    listaTrayectos.clear();
                    listaTrayectos.addAll(snapshot.getValue(gti));
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
                    listaSupervisados.clear();
                    listaSupervisados.addAll(snapshot.getValue(gti));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Todo esto lo que haces es añadir los trayectos en curso evitando que se repitan etc etc. Recomiendo no leer XD
        referenceTrayecto = database.getReference().child("usuarios");
        referenceTrayecto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot userSnapshot : snapshot.getChildren()){
                        Usuario usuario = userSnapshot.getValue(Usuario.class);
                        if (!usuario.getId().equals(Configuracion.userLoged.getUid())) {
                            if (listaSupervisados.size()!=0){
                                for (int i = 0; i < listaSupervisados.size(); i++) {
                                    if (usuario.getId().equals(listaSupervisados.get(i).getId())) {
                                        if (listaTrayectos.size() != 0) {
                                            for (int j = 0; j < listaTrayectos.size(); j++) {
                                                if (usuario.getId().equals(listaTrayectos.get(j).getId())) {
                                                    if (usuario.getEnTrayecto()) {
                                                        Usuario usuarioDef = new Usuario();
                                                        usuarioDef.setId(usuario.getId());
                                                        usuarioDef.setEmailUsuario(usuario.getEmailUsuario());
                                                        usuarioDef.setTrayecto(usuario.getTrayecto());
                                                        listaTrayectos.get(j).setTrayecto(usuarioDef.getTrayecto());
                                                        referenceTrayectos.setValue(listaTrayectos);
                                                        adapter.notifyDataSetChanged();
                                                    } else {
                                                        Usuario usuarioDef = new Usuario();
                                                        usuarioDef.setId(usuario.getId());
                                                        usuarioDef.setEmailUsuario(usuario.getEmailUsuario());
                                                        usuarioDef.setTrayecto(usuario.getTrayecto());
                                                      listaTrayectos.remove(j);
                                                        referenceTrayectos.setValue(listaTrayectos);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                } else {
                                                    Boolean existe = false;
                                                    for (int k = 0; k < listaTrayectos.size(); k++) {
                                                        if (usuario.getId().equals(listaTrayectos.get(k).getId())) {
                                                            existe = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!existe) {
                                                        Usuario usuarioDef = new Usuario();
                                                        usuarioDef.setId(usuario.getId());
                                                        usuarioDef.setEmailUsuario(usuario.getEmailUsuario());
                                                        usuarioDef.setTrayecto(usuario.getTrayecto());
                                                       listaTrayectos.add(usuarioDef);
                                                        referenceTrayectos.setValue(listaTrayectos);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        } else {

                                            if (usuario.getEnTrayecto()) {
                                                Usuario usuarioDef = new Usuario();
                                                usuarioDef.setId(usuario.getId());
                                                usuarioDef.setEmailUsuario(usuario.getEmailUsuario());
                                                usuarioDef.setTrayecto(usuario.getTrayecto());
                                                usuarioDef.setNombreUsuario(usuario.getNombreUsuario());
                                                listaTrayectos.add(usuarioDef);
                                                referenceTrayectos.setValue(listaTrayectos);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }
                            }

                        }



                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onStop() {
        super.onStop();
    }




    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}