package com.raulcasanova.safeback.Modelos;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Usuario implements Parcelable {
    //Datos usuario
    private ArrayList<Ruta> listaRutas;
    private ArrayList<Usuario>listaGuardianes;
    private ArrayList<Usuario>listaSupervisados;
    private ArrayList<Usuario>listaPeticionesSupervisados;
    private ArrayList<Usuario>listaTrayectos;
    private ArrayList<Alerta>listaAlertas;
    //lista de rutatrayecto de los usuarios
    private RutaTrayecto trayecto;
    private Boolean enTrayecto;
    //ajustes
    private float tiempoEstimado=15;
    private float tiempoMax=20;
    private float tiempoParada=5;
    private String nombreUsuario;
    private String emailUsuario;
    private String id;
    private int numeroEmergencia=112;


    public Usuario() {
        listaRutas=new ArrayList<>();
        listaGuardianes=new ArrayList<>();
        listaSupervisados=new ArrayList<>();
        listaPeticionesSupervisados=new ArrayList<>();
        listaTrayectos=new ArrayList<>();
        listaAlertas=new ArrayList<>();
        trayecto=new RutaTrayecto();
    }

    public Usuario(String nombreUsuario, String emailUsuario, String id) {
        this.nombreUsuario = nombreUsuario;
        this.emailUsuario = emailUsuario;
        this.id = id;
    }

    public Usuario(ArrayList<Ruta> listaRutas, ArrayList<Usuario> listaGuardianes, ArrayList<Usuario> listaSupervisados, ArrayList<Usuario> listaPeticionesSupervisados, ArrayList<Usuario> listaTrayectos, ArrayList<Alerta> listaAlertas, RutaTrayecto trayecto, Boolean enTrayecto, float tiempoEstimado, float tiempoMax, float tiempoParada, String nombreUsuario, String emailUsuario, String id, int numeroEmergencia) {
        this.listaRutas = listaRutas;
        this.listaGuardianes = listaGuardianes;
        this.listaSupervisados = listaSupervisados;
        this.listaPeticionesSupervisados = listaPeticionesSupervisados;
        this.listaTrayectos = listaTrayectos;
        this.listaAlertas = listaAlertas;
        this.trayecto = trayecto;
        this.enTrayecto = enTrayecto;
        this.tiempoEstimado = tiempoEstimado;
        this.tiempoMax = tiempoMax;
        this.tiempoParada = tiempoParada;
        this.nombreUsuario = nombreUsuario;
        this.emailUsuario = emailUsuario;
        this.id = id;
        this.numeroEmergencia = numeroEmergencia;
    }

    protected Usuario(Parcel in) {
        listaRutas = in.createTypedArrayList(Ruta.CREATOR);
        listaGuardianes = in.createTypedArrayList(Usuario.CREATOR);
        listaSupervisados = in.createTypedArrayList(Usuario.CREATOR);
        listaPeticionesSupervisados = in.createTypedArrayList(Usuario.CREATOR);
        listaTrayectos = in.createTypedArrayList(Usuario.CREATOR);
        listaAlertas = in.createTypedArrayList(Alerta.CREATOR);
        trayecto = in.readParcelable(RutaTrayecto.class.getClassLoader());
        byte tmpEnTrayecto = in.readByte();
        enTrayecto = tmpEnTrayecto == 0 ? null : tmpEnTrayecto == 1;
        tiempoEstimado = in.readFloat();
        tiempoMax = in.readFloat();
        tiempoParada = in.readFloat();
        nombreUsuario = in.readString();
        emailUsuario = in.readString();
        id = in.readString();
        numeroEmergencia = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listaRutas);
        dest.writeTypedList(listaGuardianes);
        dest.writeTypedList(listaSupervisados);
        dest.writeTypedList(listaPeticionesSupervisados);
        dest.writeTypedList(listaTrayectos);
        dest.writeTypedList(listaAlertas);
        dest.writeParcelable(trayecto, flags);
        dest.writeByte((byte) (enTrayecto == null ? 0 : enTrayecto ? 1 : 2));
        dest.writeFloat(tiempoEstimado);
        dest.writeFloat(tiempoMax);
        dest.writeFloat(tiempoParada);
        dest.writeString(nombreUsuario);
        dest.writeString(emailUsuario);
        dest.writeString(id);
        dest.writeInt(numeroEmergencia);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public ArrayList<Ruta> getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(ArrayList<Ruta> listaRutas) {
        this.listaRutas = listaRutas;
    }

    public ArrayList<Usuario> getListaGuardianes() {
        return listaGuardianes;
    }

    public void setListaGuardianes(ArrayList<Usuario> listaGuardianes) {
        this.listaGuardianes = listaGuardianes;
    }

    public ArrayList<Usuario> getListaSupervisados() {
        return listaSupervisados;
    }

    public void setListaSupervisados(ArrayList<Usuario> listaSupervisados) {
        this.listaSupervisados = listaSupervisados;
    }

    public ArrayList<Usuario> getListaPeticionesSupervisados() {
        return listaPeticionesSupervisados;
    }

    public void setListaPeticionesSupervisados(ArrayList<Usuario> listaPeticionesSupervisados) {
        this.listaPeticionesSupervisados = listaPeticionesSupervisados;
    }

    public ArrayList<Usuario> getListaTrayectos() {
        return listaTrayectos;
    }

    public void setListaTrayectos(ArrayList<Usuario> listaTrayectos) {
        this.listaTrayectos = listaTrayectos;
    }

    public ArrayList<Alerta> getListaAlertas() {
        return listaAlertas;
    }

    public void setListaAlertas(ArrayList<Alerta> listaAlertas) {
        this.listaAlertas = listaAlertas;
    }

    public RutaTrayecto getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(RutaTrayecto trayecto) {
        this.trayecto = trayecto;
    }

    public Boolean getEnTrayecto() {
        return enTrayecto;
    }

    public void setEnTrayecto(Boolean enTrayecto) {
        this.enTrayecto = enTrayecto;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumeroEmergencia() {
        return numeroEmergencia;
    }

    public void setNumeroEmergencia(int numeroEmergencia) {
        this.numeroEmergencia = numeroEmergencia;
    }
}
