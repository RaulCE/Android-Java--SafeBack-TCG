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
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Adapters.AddGuardianesAdapter;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Guardian;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.util.ArrayList;

public class AddGuardian extends AppCompatActivity {

    private FirebaseDatabase database;
    public DatabaseReference referenceGuardianUsuario;
    private ArrayList<Usuario>listaUsuarios;
    private Usuario usuariosafeback;

    private RecyclerView rvGuardianes;
    private AddGuardianesAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private SearchView svSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guardian);

        usuariosafeback=getIntent().getExtras().getParcelable("USUARIOLOG");

        listaUsuarios=new ArrayList<>();
        database = FirebaseDatabase.getInstance();



        rvGuardianes =findViewById(R.id.recyclerAddGuardianes);
        svSearch= findViewById(R.id.svSearchAddGuardianes);
       // svSearch.setOnQueryTextListener(this);

        adapter=new AddGuardianesAdapter(listaUsuarios,R.layout.rcc_guardianes,this,usuariosafeback);

        linearLayoutManager = new LinearLayoutManager(this);
        rvGuardianes.setLayoutManager(linearLayoutManager);
        rvGuardianes.setAdapter(adapter);

        referenceGuardianUsuario = database.getReference().child("usuarios");
        referenceGuardianUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot userSnapshot : snapshot.getChildren()){
                        Usuario usuario = userSnapshot.getValue(Usuario.class);
                        if (!usuario.getId().equals(Configuracion.userLoged.getUid())){
                            Boolean existe=false;
                            for (int i = 0; i < usuariosafeback.getListaGuardianes().size(); i++) {
                                if (usuario.getId().equals(usuariosafeback.getListaGuardianes().get(i).getId())){

                                    existe=true;
                                    break;
                                }
                            }
                            if (!existe){
                                Boolean existe2=false;
                                for (int i = 0; i < listaUsuarios.size(); i++) {
                                    if (usuario.getId().equals(listaUsuarios.get(i).getId())){
                                        existe2=true;
                                        break;
                                    }
                                }
                                if (!existe2){
                                    Boolean existe3=false;
                                    for (int i = 0; i < usuario.getListaPeticionesSupervisados().size(); i++) {
                                        if (Configuracion.userLoged.getUid().equals(usuario.getListaPeticionesSupervisados().get(i).getId())){
                                            existe3=true;
                                        }
                                    }
                                    if (!existe3){
                                        listaUsuarios.add(usuario);
                                        adapter.notifyDataSetChanged();
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


   /* @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtro(newText);
        return false;
    }*/
}