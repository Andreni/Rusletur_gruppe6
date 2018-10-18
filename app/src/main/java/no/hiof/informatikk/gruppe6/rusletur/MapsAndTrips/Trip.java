package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


/**
 * Class for trip objects in our format
 * Will be used later
 * UNDER CONSTRUCTION
 */

public class Trip {


    private String name, description, author;
    private ArrayList<LatLng> coordinates;
    public static ArrayList<Trip> trips;

    public Trip(String name, String author, ArrayList<LatLng> coordinates) {
        this.name = name;
        this.author = author;
        this.coordinates = coordinates;

        trips.add(this);
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
