package com.raulcasanova.safeback.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.MainActivity;
import com.raulcasanova.safeback.Modelos.Guardian;
import com.raulcasanova.safeback.Modelos.Usuario;
import com.raulcasanova.safeback.R;

import java.util.ArrayList;
import java.util.List;

public class GuardianesAdapter extends RecyclerView.Adapter<GuardianesAdapter.GuardianesVH>{
    private List<Usuario> objects;
    private int resource;
    private Context context;
    private FirebaseDatabase database;
    private ArrayList<Usuario>listaSuperGuards;
    public DatabaseReference referenceGuardianes,referenceListaSupervisadosGuardian;

    public GuardianesAdapter(List<Usuario> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        listaSuperGuards=new ArrayList<>();
    }

    @NonNull
    @Override
    public GuardianesAdapter.GuardianesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementoGuardian = LayoutInflater.from(context).inflate(resource, null);
        elementoGuardian.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        GuardianesAdapter.GuardianesVH guardianVH = new GuardianesAdapter.GuardianesVH(elementoGuardian);
        return guardianVH;
    }

    @Override
    public void onBindViewHolder(@NonNull GuardianesAdapter.GuardianesVH holder, int position) {



        database = FirebaseDatabase.getInstance();
        referenceGuardianes = database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("guardianes");
        referenceListaSupervisadosGuardian= database.getReference("usuarios").child(objects.get(position).getId()).child("supervisados");


        Usuario usuario = objects.get(position);
        holder.txtNombreGuardian.setText(objects.get(position).getNombreUsuario().toString());
        holder.txtEmailGuardian.setText(objects.get(position).getEmailUsuario()+"");
        holder.btnEliminarGuardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <listaSuperGuards.size() ; i++) {
                    if (Configuracion.userLoged.getUid().equals(listaSuperGuards.get(i).getId())){
                        listaSuperGuards.remove(i);
                    }
                }
                referenceListaSupervisadosGuardian.setValue(listaSuperGuards);
                objects.remove(position);
                referenceGuardianes.setValue(objects);
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "FUNCA PINCHAR :V ", Toast.LENGTH_SHORT).show();
            }
        });
        referenceListaSupervisadosGuardian.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                    listaSuperGuards.clear();
                    listaSuperGuards.addAll(snapshot.getValue(gti));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class GuardianesVH extends RecyclerView.ViewHolder {

        ImageView imagenGuardian;
        TextView txtNombreGuardian,txtEmailGuardian;
        Button btnEliminarGuardian;

        public GuardianesVH(@NonNull View itemView) {
            super(itemView);

            txtNombreGuardian=itemView.findViewById(R.id.txtNombreGuardian);
            txtEmailGuardian = itemView.findViewById(R.id.txtEmailGuardian);
            btnEliminarGuardian= itemView.findViewById(R.id.btnEliminarGuardian);
            imagenGuardian = itemView.findViewById(R.id.imgFotoGuardian);
        }
    }
}
