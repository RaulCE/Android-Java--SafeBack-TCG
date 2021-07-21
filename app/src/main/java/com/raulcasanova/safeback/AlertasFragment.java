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
import com.raulcasanova.safeback.Adapters.AlertasAdapter;
import com.raulcasanova.safeback.Adapters.RutasAdapter;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Alerta;
import com.raulcasanova.safeback.Modelos.Ruta;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertasFragment extends Fragment {
    private FloatingActionButton fab;
    private RecyclerView rvAlertas;
    private AlertasAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private SearchView svSearch;
    private ArrayList<Alerta> listaAlertas;
    private FirebaseDatabase database;
    public DatabaseReference referenceAlertas;

    public AlertasFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AlertasFragment newInstance(String param1, String param2) {
        AlertasFragment fragment = new AlertasFragment();

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
        return inflater.inflate(R.layout.fragment_alertas, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        listaAlertas=new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        rvAlertas = getView().findViewById(R.id.recyclerAlertas);
        fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaAlertas.clear();
                referenceAlertas.setValue(listaAlertas);
                adapter.notifyDataSetChanged();
            }
        });
       // svSearch= getView().findViewById(R.id.svSearchRutas);
       //svSearch.setOnQueryTextListener(this);

        adapter=new AlertasAdapter(listaAlertas,R.layout.rcc_alertas,((MainActivity) getActivity()));

        referenceAlertas = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("alertas");
        referenceAlertas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Alerta>> gti = new GenericTypeIndicator<ArrayList<Alerta>>() {};
                    listaAlertas.clear();
                    listaAlertas.addAll(snapshot.getValue(gti));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        linearLayoutManager = new LinearLayoutManager(((MainActivity) getActivity()));
        rvAlertas.setLayoutManager(linearLayoutManager);
        rvAlertas.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
    }


   /* @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //  adapter.filtro(newText);
        return false;
    }*/
}