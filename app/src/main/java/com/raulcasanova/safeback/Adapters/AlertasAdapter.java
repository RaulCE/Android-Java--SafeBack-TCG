package com.raulcasanova.safeback.Adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.raulcasanova.safeback.MapsActivity;
import com.raulcasanova.safeback.MapsVerActivity;
import com.raulcasanova.safeback.Modelos.Alerta;
import com.raulcasanova.safeback.R;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlertasAdapter  extends RecyclerView.Adapter<AlertasAdapter.AlertasVH> {
    private List<Alerta> objects;
    private int resource;
    private Context context;

    public AlertasAdapter(List<Alerta> objects, int resource, Context context) {
        Collections.reverse(objects);
        this.objects = objects;

        this.resource = resource;
        this.context = context;

    }

    @NonNull
    @Override
    public AlertasAdapter.AlertasVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementoAlerta = LayoutInflater.from(context).inflate(resource, null);
        elementoAlerta.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AlertasAdapter.AlertasVH alertaVH = new AlertasAdapter.AlertasVH(elementoAlerta);
        return alertaVH;
    }

    @Override
    public void onBindViewHolder(@NonNull AlertasAdapter.AlertasVH holder, int position) {

        Alerta alerta = objects.get(position);
        holder.txtNombreAlerta.setText(alerta.getNombreSupervisado()+" | "+alerta.getEmailSupervisado());
        Date date;
        date=alerta.getFechaAlerta();
        holder.txtFechaAlerta.setText(date.toString());

        LatLng latLng = new LatLng(alerta.getRutaAlerta().getPuntoTrayecto().getLatitude(),alerta.getRutaAlerta().getPuntoTrayecto().getLongitude());
        Geocoder geocoder1 = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder1.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!direcciones.isEmpty()){
                Address address = direcciones.get(0);
                String dir = address.getAddressLine(0);
                holder.txtCalleAlerta.setText(dir);
            }
            else {
                holder.txtCalleAlerta.setText("No hay direcciones Disponibles");
            }
        } catch (IOException e) {
            holder.txtCalleAlerta.setText("ERROR: al obtener la dirección");
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("ALERTA", alerta);
                bundle.putString("DONDEVENGO","Alertas");
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

    public static class AlertasVH extends RecyclerView.ViewHolder {

        ImageView imageViewAlerta;
        TextView txtNombreAlerta,txtFechaAlerta,txtCalleAlerta;

        public AlertasVH(@NonNull View itemView) {
            super(itemView);

            txtNombreAlerta=itemView.findViewById(R.id.txtNombreTrayecto);
            txtFechaAlerta = itemView.findViewById(R.id.txtFechaAlerta);
            txtCalleAlerta = itemView.findViewById(R.id.txtEmailVigilado);
            imageViewAlerta = itemView.findViewById(R.id.imageViewTrayecto);
        }
    }
}
