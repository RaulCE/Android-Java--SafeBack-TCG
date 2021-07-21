package com.raulcasanova.safeback.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raulcasanova.safeback.MapsVerActivity;
import com.raulcasanova.safeback.Modelos.Usuario;
import com.raulcasanova.safeback.R;

import java.util.List;

public class TrayectosAdapter  extends RecyclerView.Adapter<TrayectosAdapter.TrayectosVH> {

    private List<Usuario> objects;
    private int resource;
    private Context context;

    public TrayectosAdapter(List<Usuario> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public TrayectosAdapter.TrayectosVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementoTrayecto = LayoutInflater.from(context).inflate(resource, null);
        elementoTrayecto.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TrayectosAdapter.TrayectosVH trayectoVH = new TrayectosAdapter.TrayectosVH(elementoTrayecto);
        return trayectoVH;
    }

    @Override
    public void onBindViewHolder(@NonNull TrayectosAdapter.TrayectosVH holder, int position) {

        Usuario usuario = objects.get(position);
        holder.txtEmailTrayecto.setText(usuario.getEmailUsuario());
        holder.txtNombreTrayecto.setText(usuario.getNombreUsuario());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("TRAYECTO", objects.get(position).getTrayecto());
                bundle.putString("EMAIL",objects.get(position).getEmailUsuario());
                bundle.putString("IDSUPERVISADO",objects.get(position).getId());
                bundle.putString("DONDEVENGO","Trayectos");
                Intent intent = new Intent(context, MapsVerActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class TrayectosVH extends RecyclerView.ViewHolder {

        ImageView imageViewTrayecto;
        TextView txtNombreTrayecto,txtEmailTrayecto;

        public TrayectosVH(@NonNull View itemView) {
            super(itemView);

            txtNombreTrayecto=itemView.findViewById(R.id.txtNombreTrayecto);
            txtEmailTrayecto = itemView.findViewById(R.id.txtEmailVigilado);
            imageViewTrayecto = itemView.findViewById(R.id.imageViewTrayecto);
        }
    }
}
