package com.raulcasanova.safeback.Modelos;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Guardian implements Parcelable {
    private String nombre;
    private String email;
    private String id;

    public Guardian() {
    }

    public Guardian(String nombre, String email, String id) {
        this.nombre = nombre;
        this.email = email;
        this.id = id;
    }

    protected Guardian(Parcel in) {
        nombre = in.readString();
        email = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(email);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Guardian> CREATOR = new Creator<Guardian>() {
        @Override
        public Guardian createFromParcel(Parcel in) {
            return new Guardian(in);
        }

        @Override
        public Guardian[] newArray(int size) {
            return new Guardian[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
