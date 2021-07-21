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
import com.raulcasanova.safeback.Modelos.Usuario;
import com.raulcasanova.safeback.R;

import java.util.ArrayList;
import java.util.List;

public class VigiladosAdapter extends RecyclerView.Adapter<VigiladosAdapter.VigiladosVH> {
    private List<Usuario> objects;
    private int resource;
    private Context context;
    private FirebaseDatabase database;
    public DatabaseReference referenceGuardianUsuario,referenceListaSupervisados;
    private ArrayList<Usuario> listaGuardianesSupervisado;

    public VigiladosAdapter(List<Usuario> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        this.listaGuardianesSupervisado=new ArrayList();

    }

    @NonNull
    @Override
    public VigiladosAdapter.VigiladosVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementoVigilado = LayoutInflater.from(context).inflate(resource, null);
        elementoVigilado.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        VigiladosAdapter.VigiladosVH vigiladoVH = new VigiladosAdapter.VigiladosVH(elementoVigilado);
        return vigiladoVH;
    }

    @Override
    public void onBindViewHolder(@NonNull VigiladosAdapter.VigiladosVH holder, int position) {

        Usuario usuario = objects.get(position);
        holder.txtNombreGuardian.setText(usuario.getNombreUsuario());
        holder.txtEmailGuardian.setText(usuario.getEmailUsuario()+"");

        database = FirebaseDatabase.getInstance();
        referenceListaSupervisados =  database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("supervisados");
        referenceGuardianUsuario =  database.getReference("usuarios").child(usuario.getId()).child("guardianes");
        referenceGuardianUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Usuario>> gti = new GenericTypeIndicator<ArrayList<Usuario>>() {};
                    listaGuardianesSupervisado.clear();
                    listaGuardianesSupervisado.addAll(snapshot.getValue(gti));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.btnEliminarGuardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i <listaGuardianesSupervisado.size() ; i++) {
                    Toast.makeText(context,"ID SUPERV1 :"+ objects.get(position).getId(), Toast.LENGTH_LONG).show();
                    Toast.makeText(context,"ID SUPERV :"+ listaGuardianesSupervisado.get(i).getId(), Toast.LENGTH_LONG).show();
                    if (Configuracion.userLoged.getUid().equals(listaGuardianesSupervisado.get(i).getId())){
                        listaGuardianesSupervisado.remove(i);
                    }
                }
                referenceGuardianUsuario.setValue(listaGuardianesSupervisado);
                objects.remove(position);
                referenceListaSupervisados.setValue(objects);
                notifyDataSetChanged();
            }
        });



        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "FUNCA PINCHAR :V ", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class VigiladosVH extends RecyclerView.ViewHolder {

        ImageView imagenGuardian;
        TextView txtNombreGuardian,txtEmailGuardian;
        Button btnEliminarGuardian;

        public VigiladosVH(@NonNull View itemView) {
            super(itemView);

            txtNombreGuardian=itemView.findViewById(R.id.txtEmailVigilado);
            txtEmailGuardian = itemView.findViewById(R.id.txtNombreVigilado);
            btnEliminarGuardian= itemView.findViewById(R.id.btnEliminarVigilado);
            imagenGuardian = itemView.findViewById(R.id.imgFotoVigilado);
        }
    }
}
