package com.raulcasanova.safeback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Usuario;

import java.sql.SQLException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjustesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjustesFragment extends Fragment {


    private EditText txtTEst;
    private EditText txtTMax;
    private EditText txtTPar;
    private EditText txtTelefono;
    private Button btnLogout;

    private float tiempoEstimado,tiempoMaximo,tiempoParada;
    private int numeroEmergencia;

    private FirebaseDatabase database;
    public DatabaseReference referenceTiempoEstimado
            ,referenceTiempoMax
            ,referenceTiempoParada
            ,referenceNumeroEmergencia;

    public AjustesFragment() {
        // Required empty public constructor
    }


    public static AjustesFragment newInstance(String param1, String param2) {
        AjustesFragment fragment = new AjustesFragment();
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
        return inflater.inflate(R.layout.fragment_ajustes, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        txtTEst = (EditText) getView().findViewById(R.id.txtAjustesEditTiempoEstimado);
        txtTMax = (EditText) getView().findViewById(R.id.txtAjustesEditTiempoMaximo);
        txtTPar = (EditText) getView().findViewById(R.id.txtAjustesEditTiempoParada);
        txtTelefono = (EditText) getView().findViewById(R.id.txtAjustesEditTelefono);
        btnLogout = (Button) getView().findViewById(R.id.btnAjustesLogOut);
        database = FirebaseDatabase.getInstance();
        referenceTiempoEstimado = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("tiempoEstimado");
        referenceTiempoEstimado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    tiempoEstimado=snapshot.getValue(Float.class);
                    txtTEst.setText(tiempoEstimado+"");
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
                    tiempoMaximo=snapshot.getValue(Float.class);
                    txtTMax.setText(tiempoMaximo+"");
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
                    tiempoParada=snapshot.getValue(Float.class);
                    txtTPar.setText(tiempoParada+"");
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
                    numeroEmergencia=snapshot.getValue(Integer.class);
                    txtTelefono.setText(numeroEmergencia+"");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("¿Desea cerrar sesión?");
                builder.setNegativeButton("CANCELAR", null);
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogOut();
                    }
                });
                builder.create().show();

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (!txtTelefono.getText().toString().equals("")){
           numeroEmergencia=Integer.parseInt(txtTelefono.getText().toString());
           referenceNumeroEmergencia.setValue(numeroEmergencia);

        }
        if (!txtTEst.getText().toString().equals("")){
            tiempoEstimado=Float.parseFloat(txtTEst.getText().toString());
            referenceTiempoEstimado.setValue(tiempoEstimado);


        }
        if (!txtTMax.getText().toString().equals("")){
            tiempoMaximo=Float.parseFloat(txtTMax.getText().toString());
            referenceTiempoMax.setValue(tiempoMaximo);


        }
        if (!txtTPar.getText().toString().equals("")){
            tiempoParada=Float.parseFloat(txtTPar.getText().toString());
            referenceTiempoParada.setValue(tiempoParada);
        }
    }

    private void LogOut() {
        ((MainActivity) getActivity()).mAuth.signOut();
        Configuracion.userLoged=  null;
        startActivity(new Intent(((MainActivity) getActivity()), LogActivity.class));
        ((MainActivity) getActivity()).finish();
    }
}