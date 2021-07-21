package com.raulcasanova.safeback.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class RutaTrayecto implements Parcelable {
    private com.raulcasanova.safeback.Modelos.LatLng puntoTrayecto;
    private com.raulcasanova.safeback.Modelos.LatLng  puntoOrigen;
    private com.raulcasanova.safeback.Modelos.LatLng  puntoDestino;
    private float tiempoEstimado;
    private float tiempoMax;
    private float tiempoParada;

    public RutaTrayecto() {
    }

    public RutaTrayecto(LatLng puntoTrayecto, LatLng puntoOrigen, LatLng puntoDestino, float tiempoEstimado, float tiempoMax, float tiempoParada) {
        this.puntoTrayecto = puntoTrayecto;
        this.puntoOrigen = puntoOrigen;
        this.puntoDestino = puntoDestino;
        this.tiempoEstimado = tiempoEstimado;
        this.tiempoMax = tiempoMax;
        this.tiempoParada = tiempoParada;
    }

    protected RutaTrayecto(Parcel in) {
        puntoTrayecto = in.readParcelable(LatLng.class.getClassLoader());
        puntoOrigen = in.readParcelable(LatLng.class.getClassLoader());
        puntoDestino = in.readParcelable(LatLng.class.getClassLoader());
        tiempoEstimado = in.readFloat();
        tiempoMax = in.readFloat();
        tiempoParada = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(puntoTrayecto, flags);
        dest.writeParcelable(puntoOrigen, flags);
        dest.writeParcelable(puntoDestino, flags);
        dest.writeFloat(tiempoEstimado);
        dest.writeFloat(tiempoMax);
        dest.writeFloat(tiempoParada);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RutaTrayecto> CREATOR = new Creator<RutaTrayecto>() {
        @Override
        public RutaTrayecto createFromParcel(Parcel in) {
            return new RutaTrayecto(in);
        }

        @Override
        public RutaTrayecto[] newArray(int size) {
            return new RutaTrayecto[size];
        }
    };

    public LatLng getPuntoTrayecto() {
        return puntoTrayecto;
    }

    public void setPuntoTrayecto(LatLng puntoTrayecto) {
        this.puntoTrayecto = puntoTrayecto;
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
}
