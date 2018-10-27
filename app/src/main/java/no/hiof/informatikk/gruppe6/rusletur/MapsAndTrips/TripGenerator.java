package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TripGenerator {

    private FirebaseUser user;
    private ArrayList<LatLng> tripCoords;
    private String tripname;

    public TripGenerator(FirebaseUser user, ArrayList<LatLng> tripCoords, String tripname) {
        this.user = user;
        this.tripCoords = tripCoords;
        this.tripname = tripname;
    }
}
