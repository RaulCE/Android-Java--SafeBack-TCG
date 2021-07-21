package com.raulcasanova.safeback;

import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Adapters.GuardianesAdapter;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuardianesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuardianesFragment extends Fragment {

    private final int ADD_GUARDIAN = 0 ;
    private FloatingActionButton fab;
    private RecyclerView rvGuardianes;
    private GuardianesAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseDatabase database;
    public DatabaseReference referenceGuardianes,referenceUsuarios,referenceGuardianUsuario;
    private ArrayList<Usuario> listaGuardianes;
    private ArrayList<Usuario> listaUsuarios;

    public GuardianesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GuardianesFragment newInstance(String param1, String param2) {
        GuardianesFragment fragment = new GuardianesFragment();

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
        return inflater.inflate(R.layout.fragment_guardianes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        listaGuardianes=new ArrayList<>();
        listaUsuarios=new ArrayList<>();
        rvGuardianes =(RecyclerView) getView().findViewById(R.id.recyclerGuardianes);
        fab = getView().findViewById(R.id.fab);

        adapter=new GuardianesAdapter(listaGuardianes,R.layout.rcc_guardianes,((MainActivity) getActivity()));

        linearLayoutManager = new LinearLayoutManager(((MainActivity) getActivity()));
        rvGuardianes.setLayoutManager(linearLayoutManager);
        rvGuardianes.setAdapter(adapter);

       database = FirebaseDatabase.getInstance();
        referenceGuardianes = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("guardianes");
        referenceGuardianes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                    listaGuardianes.clear();
                    listaGuardianes.addAll(snapshot.getValue(gti));
                    adapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        referenceUsuarios = database.getReference().child("usuarios");
        referenceUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot userSnapshot : snapshot.getChildren()){
                        Usuario usuario = userSnapshot.getValue(Usuario.class);
                        listaUsuarios.add(usuario);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Bundle bundle = new Bundle();
                bundle.putParcelable("USUARIOLOG", ((MainActivity) getActivity()).usuariosafeBack);
                Intent intent = new Intent(getActivity(), AddGuardian.class);
                intent.putExtras(bundle);
                startActivity(intent);*/
                Dialog dialog = new Dialog(getActivity());

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                /////make map clear
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                dialog.setContentView(R.layout.dialog_addguardian);
                Button btnAgregar = dialog.findViewById(R.id.btnAddGuardianDialogAgregar);
                Button btnCancelar = dialog.findViewById(R.id.btnAddGuardianDialogCancelar);
                TextView txtEmailUsuario=dialog.findViewById(R.id.txtEmailGuardianAddDialog);
                btnAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < listaUsuarios.size(); i++) {
                            if (listaUsuarios.get(i).getEmailUsuario().equals(txtEmailUsuario.getText().toString()) && !listaUsuarios.get(i).getEmailUsuario().equals(Configuracion.userLoged.getEmail())){
                               Boolean existe=false;
                                for (int j = 0; j < listaUsuarios.get(i).getListaPeticionesSupervisados().size(); j++) {
                                    if (listaUsuarios.get(i).getListaPeticionesSupervisados().get(j).getEmailUsuario().equals(Configuracion.userLoged.getEmail())){
                                        existe=true;
                                        break;
                                    }
                                }
                                if (!existe){
                                    Usuario usuarioaux = new Usuario(Configuracion.userLoged.getDisplayName(),Configuracion.userLoged.getEmail(),Configuracion.userLoged.getUid());
                                    referenceGuardianUsuario =  database.getReference("usuarios").child(listaUsuarios.get(i).getId()).child("listaPeticionesSupervisados");
                                    listaUsuarios.get(i).getListaPeticionesSupervisados().add(usuarioaux);
                                    referenceGuardianUsuario.setValue(listaUsuarios.get(i).getListaPeticionesSupervisados());
                                    Toast.makeText(getActivity(), "Peticion enviada ", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(getActivity(), "Error al enviar la peticion ", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }else {
                                Toast.makeText(getActivity(), "Error al enviar la peticion ", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                    }
                });
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });


                dialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}