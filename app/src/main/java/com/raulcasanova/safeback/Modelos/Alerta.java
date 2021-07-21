package com.raulcasanova.safeback.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Alerta implements Parcelable {
    private String nombreSupervisado;
    private String emailSupervisado;
    private RutaTrayecto rutaAlerta;
    private Date fechaAlerta;

    public Alerta() {
        this.fechaAlerta=new Date();
    }

    public Alerta(String nombreSupervisado, String emailSupervisado, RutaTrayecto rutaAlerta, Date fechaAlerta) {
        this.nombreSupervisado = nombreSupervisado;
        this.emailSupervisado = emailSupervisado;
        this.rutaAlerta = rutaAlerta;
        this.fechaAlerta = fechaAlerta;
    }



    protected Alerta(Parcel in) {
        nombreSupervisado = in.readString();
        emailSupervisado = in.readString();
        rutaAlerta = in.readParcelable(RutaTrayecto.class.getClassLoader());
        this.fechaAlerta=new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreSupervisado);
        dest.writeString(emailSupervisado);
        dest.writeParcelable(rutaAlerta, flags);
        dest.writeLong(fechaAlerta.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alerta> CREATOR = new Creator<Alerta>() {
        @Override
        public Alerta createFromParcel(Parcel in) {
            return new Alerta(in);
        }

        @Override
        public Alerta[] newArray(int size) {
            return new Alerta[size];
        }
    };

    public String getNombreSupervisado() {
        return nombreSupervisado;
    }

    public void setNombreSupervisado(String nombreSupervisado) {
        this.nombreSupervisado = nombreSupervisado;
    }

    public String getEmailSupervisado() {
        return emailSupervisado;
    }

    public void setEmailSupervisado(String emailSupervisado) {
        this.emailSupervisado = emailSupervisado;
    }

    public RutaTrayecto getRutaAlerta() {
        return rutaAlerta;
    }

    public void setRutaAlerta(RutaTrayecto rutaAlerta) {
        this.rutaAlerta = rutaAlerta;
    }

    public Date getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(Date fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }
}
