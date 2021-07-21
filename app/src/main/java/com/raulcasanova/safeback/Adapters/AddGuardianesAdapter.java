package com.raulcasanova.safeback.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.AddGuardian;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.MainActivity;
import com.raulcasanova.safeback.Modelos.Guardian;
import com.raulcasanova.safeback.Modelos.Usuario;
import com.raulcasanova.safeback.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddGuardianesAdapter extends RecyclerView.Adapter<AddGuardianesAdapter.GuardianVH>{

    private List<Usuario> objects;
    private List<Usuario> originalObjects;
    private int resource;
    private Context context;
    private Usuario usuariosb;

    private FirebaseDatabase database;
    public DatabaseReference referenceGuardianUsuario;

    public AddGuardianesAdapter(List<Usuario> objects, int resource, Context context, Usuario usuariosb) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        this.originalObjects=new ArrayList<>();
        originalObjects.addAll(objects);
        this.usuariosb=usuariosb;
    }

   /* public void filtro(String busqueda){
        if (busqueda.length() == 0 ){
            objects.clear();
            objects.addAll(originalObjects);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                objects.clear();
                List<Usuario> collect= originalObjects.stream().filter(usuario -> usuario.getEmailUsuario().toLowerCase().contains(busqueda))
                        .collect(Collectors.toList());
                objects.addAll(collect);
            }
        }
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public AddGuardianesAdapter.GuardianVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementoGuardian = LayoutInflater.from(context).inflate(resource, null);
        elementoGuardian.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        GuardianVH GuardianVH = new GuardianVH(elementoGuardian);
        return GuardianVH;
    }

    @Override
    public void onBindViewHolder(@NonNull AddGuardianesAdapter.GuardianVH holder, int position) {


        Usuario usuario=objects.get(position);
        Usuario usuarioaux = new Usuario(usuariosb.getNombreUsuario(),usuariosb.getEmailUsuario(),usuariosb.getId());

        holder.txtNombreGuardian.setText(usuario.getNombreUsuario());
        holder.txtEmailGuardian.setText(usuario.getEmailUsuario());

        database = FirebaseDatabase.getInstance();
        referenceGuardianUsuario =  database.getReference("usuarios").child(usuario.getId()).child("listaPeticionesSupervisados");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.getListaPeticionesSupervisados().add(usuarioaux);
                referenceGuardianUsuario.setValue(usuario.getListaPeticionesSupervisados());

                objects.remove(position);
                notifyDataSetChanged();

                Toast.makeText(context, "Peticion enviada ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class GuardianVH extends RecyclerView.ViewHolder {

        TextView txtNombreGuardian,txtEmailGuardian;

        public GuardianVH(@NonNull View itemView) {
            super(itemView);

            txtNombreGuardian=itemView.findViewById(R.id.txtNombreGuardian);
            txtEmailGuardian = itemView.findViewById(R.id.txtEmailGuardian);
        }
    }
}

