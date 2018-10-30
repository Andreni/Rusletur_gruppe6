package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Trip {


    private String id, tag, gradering, tilbyder, fylke, kommune, beskrivelse, navn, lisens, url;
    private ArrayList<LatLng> coordinates;


    //_id, tag, gradering, tidsbruk, tilbyder, fylke, kommune, coordinates, beskrivelse, navn, lisens, url(ut.no)

    public Trip(String id, String navn, String tag, String gradering, String tilbyder, String fylke, String kommune, String beskrivelse, String lisens, String url, ArrayList<LatLng> coordinates) {
        this.id = id;
        this.navn = navn;
        this.tag = tag;
        this.gradering = gradering;
        this.tilbyder = tilbyder;
        this.fylke = fylke;
        this.kommune = kommune;
        this.beskrivelse = beskrivelse;
        this.lisens = lisens;
        this.url = url;
        this.coordinates = coordinates;
    }


    public String getId() {
        return id;
    }
    public String getTag() {
        return tag;
    }
    public String getGradering() {
        return gradering;
    }
    public String getTilbyder() {
        return tilbyder;
    }
    public String getFylke() {
        return fylke;
    }
    public String getKommune() {
        return kommune;
    }
    public String getBeskrivelse() {
        return beskrivelse;
    }
    public String getNavn() {
        return navn;
    }
    public String getLisens() {
        return lisens;
    }
    public String getUrl() {
        return url;
    }
    public ArrayList<LatLng> getCoordinates() {
        return coordinates;
    }
}