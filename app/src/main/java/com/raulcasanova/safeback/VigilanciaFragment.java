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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Adapters.VigiladosAdapter;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VigilanciaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VigilanciaFragment extends Fragment {

    private final int ADD_GUARDIAN = 0 ;
    private FloatingActionButton fab;
    private RecyclerView rvVigilados;
    private VigiladosAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseDatabase database;
    public DatabaseReference referenceSupervisados;
    private ArrayList<Usuario> listaSupervisados;

    public VigilanciaFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VigilanciaFragment newInstance(String param1, String param2) {
        VigilanciaFragment fragment = new VigilanciaFragment();

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
        return inflater.inflate(R.layout.fragment_vigilancia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        listaSupervisados= new ArrayList<>();
        rvVigilados =(RecyclerView) getView().findViewById(R.id.recyclerVigilados);
        fab = getView().findViewById(R.id.fab);

        adapter=new VigiladosAdapter(listaSupervisados,R.layout.rcc_vigilados,((MainActivity) getActivity()));

        linearLayoutManager = new LinearLayoutManager(((MainActivity) getActivity()));
        rvVigilados.setLayoutManager(linearLayoutManager);
        rvVigilados.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("USUARIOLOG", ((MainActivity) getActivity()).usuariosafeBack);
                Intent intent = new Intent(getActivity(), AddVigilado.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        database = FirebaseDatabase.getInstance();
        referenceSupervisados = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("supervisados");
        referenceSupervisados.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                   listaSupervisados.clear();
                   listaSupervisados.addAll(snapshot.getValue(gti));
                    adapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}