package com.raulcasanova.safeback.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class Ruta implements Parcelable {
    private String nombre;
    private com.raulcasanova.safeback.Modelos.LatLng puntoOrigen;
    private com.raulcasanova.safeback.Modelos.LatLng puntoDestino;
    private float tiempoEstimado;
    private float tiempoMax;
    private float tiempoParada;



    public Ruta() {
        this.puntoOrigen=new LatLng();
        this.puntoDestino=new LatLng();
    }


    public Ruta(String nombre, LatLng puntoOrigen, LatLng puntoDestino, float tiempoEstimado, float tiempoMax, float tiempoParada) {
        this.nombre = nombre;
        this.puntoOrigen = puntoOrigen;
        this.puntoDestino = puntoDestino;
        this.tiempoEstimado = tiempoEstimado;
        this.tiempoMax = tiempoMax;
        this.tiempoParada = tiempoParada;
    }


    protected Ruta(Parcel in) {
        nombre = in.readString();
        puntoOrigen = in.readParcelable(LatLng.class.getClassLoader());
        puntoDestino = in.readParcelable(LatLng.class.getClassLoader());
        tiempoEstimado = in.readFloat();
        tiempoMax = in.readFloat();
        tiempoParada = in.readFloat();
    }

    public static final Creator<Ruta> CREATOR = new Creator<Ruta>() {
        @Override
        public Ruta createFromParcel(Parcel in) {
            return new Ruta(in);
        }

        @Override
        public Ruta[] newArray(int size) {
            return new Ruta[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LatLng getPuntoOrigen() {
        return puntoOrigen;
    }

    public void setPuntoOrigen(LatLng puntoOrigen) {
        this.puntoOrigen = puntoOrigen;
    }



    public LatLng getPuntoDestino() {
        return puntoDestino;
    }

    public void setPuntoDestino(LatLng puntoDestino) {
        this.puntoDestino = puntoDestino;
    }

    public float getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(float tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public float getTiempoMax() {
        return tiempoMax;
    }

    public void setTiempoMax(float tiempoMax) {
        this.tiempoMax = tiempoMax;
    }

    public float getTiempoParada() {
        return tiempoParada;
    }

    public void setTiempoParada(float tiempoParada) {
        this.tiempoParada = tiempoParada;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeParcelable(puntoOrigen, flags);
        dest.writeParcelable(puntoDestino, flags);
        dest.writeFloat(tiempoEstimado);
        dest.writeFloat(tiempoMax);
        dest.writeFloat(tiempoParada);
    }
}
