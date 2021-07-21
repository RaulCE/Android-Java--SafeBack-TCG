package com.raulcasanova.safeback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Adapters.AddGuardianesAdapter;
import com.raulcasanova.safeback.Adapters.AddVigiladosAdapter;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.util.ArrayList;

public class AddVigilado extends AppCompatActivity  {



    private FirebaseDatabase database;
    public DatabaseReference referenceGuardianUsuario;
    private ArrayList<Usuario>listaUsuarios;
    private Usuario usuariosafeback;

    private RecyclerView rvGuardianes;
    private AddVigiladosAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private SearchView svSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vigilado);

        usuariosafeback=getIntent().getExtras().getParcelable("USUARIOLOG");

        listaUsuarios=new ArrayList<>();
        database = FirebaseDatabase.getInstance();



        rvGuardianes =findViewById(R.id.recyclerAddViglados);

        adapter=new AddVigiladosAdapter(listaUsuarios,R.layout.rcc_vigilados,this,usuariosafeback);

        linearLayoutManager = new LinearLayoutManager(this);
        rvGuardianes.setLayoutManager(linearLayoutManager);
        rvGuardianes.setAdapter(adapter);


        referenceGuardianUsuario = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("listaPeticionesSupervisados");
        referenceGuardianUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                    listaUsuarios.clear();
                    listaUsuarios.addAll(snapshot.getValue(gti));
                    adapter.notifyDataSetChanged();


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}