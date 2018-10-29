package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.User.User;


/**
 * Class for trip objects in our format
 * Will be used later
 * UNDER CONSTRUCTION
 */

public class Trip {


    private String name, description, author;
    private ArrayList<LatLng> coordinates;
    public static ArrayList<Trip> trips;
    private static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    public Trip(String name, String author, ArrayList<LatLng> coordinates) {
        this.name = name;
        this.author = author;
        this.coordinates = coordinates;

        trips.add(this);
    }
    public static void addTrip(String tripname, ArrayList<LatLng> coords, FirebaseUser user) {
        if(user != null) {
            User.addTrip(tripname);
            myRef.child("trip").child(tripname).child("Created by").setValue(user.getEmail());
        }
        int count = 0;
        for(LatLng i : coords) {
            myRef.child("trip").child(tripname).child("LatLng").child("Lat").child(String.valueOf(count)).setValue(i.latitude);
            myRef.child("trip").child(tripname).child("LatLng").child("Lon").child(String.valueOf(count)).setValue(i.longitude);
            count++;
        }

    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getAuthor() {
        return author;
    }
    public ArrayList<LatLng> getCoordinates() {
        return coordinates;
    }
}
