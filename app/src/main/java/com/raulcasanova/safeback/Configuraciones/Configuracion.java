package com.raulcasanova.safeback.Configuraciones;

import com.google.firebase.auth.FirebaseUser;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class Configuracion {
    public static FirebaseUser userLoged;
    public static SimpleDateFormat sdf;
    public static com.google.android.gms.maps.model.LatLng mapsLatLng;
    static {
        sdf = new SimpleDateFormat("dd/MM/YYYY");

    }
}
