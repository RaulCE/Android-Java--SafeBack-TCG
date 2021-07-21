package com.raulcasanova.safeback.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.Modelos.Usuario;
import com.raulcasanova.safeback.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddVigiladosAdapter extends RecyclerView.Adapter<AddVigiladosAdapter.VigiladoVH>{

    private List<Usuario> objects;
    private List<Usuario> originalObjects;
    private int resource;
    private Context context;
    private Usuario usuariosb;

    private FirebaseDatabase database;
    public DatabaseReference referenceGuardianUsuario,referenceListaSupervisados,referenceListaPeticionesSupervisados;

    public AddVigiladosAdapter(List<Usuario> objects, int resource, Context context, Usuario usuariosb) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        this.originalObjects=new ArrayList<>();
        originalObjects.addAll(objects);
        this.usuariosb=usuariosb;
    }

    public void filtro(String busqueda){
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
    }

    @NonNull
    @Override
    public AddVigiladosAdapter.VigiladoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementoVigilado = LayoutInflater.from(context).inflate(resource, null);
        elementoVigilado.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AddVigiladosAdapter.VigiladoVH vigiladoVH = new AddVigiladosAdapter.VigiladoVH(elementoVigilado);
        return vigiladoVH;
    }

    @Override
    public void onBindViewHolder(@NonNull AddVigiladosAdapter.VigiladoVH holder, int position) {


        Usuario usuario=objects.get(position);
        Usuario usuariolocal = new Usuario(usuariosb.getNombreUsuario(),usuariosb.getEmailUsuario(),usuariosb.getId());
        usuariolocal.setListaSupervisados(usuariosb.getListaSupervisados());
        usuariolocal.setListaPeticionesSupervisados(usuariosb.getListaPeticionesSupervisados());


        holder.txtNombreGuardian.setText(usuario.getNombreUsuario());
        holder.txtEmailGuardian.setText(usuario.getEmailUsuario());

        database = FirebaseDatabase.getInstance();
        referenceGuardianUsuario =  database.getReference("usuarios").child(usuario.getId()).child("guardianes");

        referenceListaSupervisados =  database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("supervisados");
        referenceListaPeticionesSupervisados =  database.getReference("usuarios").child(Configuracion.userLoged.getUid()).child("listaPeticionesSupervisados");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Usuario auxlocal= new Usuario(usuariolocal.getNombreUsuario(),usuariolocal.getEmailUsuario(),usuariolocal.getId());
                Usuario aux= new Usuario(usuario.getNombreUsuario(),usuario.getEmailUsuario(),usuario.getId());

                objects.get(position).getListaGuardianes().add(auxlocal);
                referenceGuardianUsuario.setValue(objects.get(position).getListaGuardianes());

                usuariolocal.getListaSupervisados().add(aux);
                referenceListaSupervisados.setValue(usuariolocal.getListaSupervisados());


                for (int i = 0; i < usuariolocal.getListaPeticionesSupervisados().size(); i++) {
                    if (aux.getId().equals(usuariolocal.getListaPeticionesSupervisados().get(i).getId())){
                        usuariolocal.getListaPeticionesSupervisados().remove(i);
                        referenceListaPeticionesSupervisados.setValue(usuariolocal.getListaPeticionesSupervisados());
                        objects.remove(position);
                        notifyDataSetChanged();
                    }
                }
                

                Toast.makeText(context, "Peticion aceptada ", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnEliminarPeticion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.remove(position);
                referenceListaPeticionesSupervisados.setValue(objects);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class VigiladoVH extends RecyclerView.ViewHolder {

        TextView txtNombreGuardian,txtEmailGuardian;
        Button btnEliminarPeticion;

        public VigiladoVH(@NonNull View itemView) {
            super(itemView);

            txtNombreGuardian=itemView.findViewById(R.id.txtEmailVigilado);
            txtEmailGuardian = itemView.findViewById(R.id.txtNombreVigilado);
            btnEliminarPeticion =  itemView.findViewById(R.id.btnEliminarVigilado);
        }
    }

}
