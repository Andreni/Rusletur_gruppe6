package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.User.User;


/**
 * A trip object
 */
public class Trip implements Parcelable {


    private String id;
    private String navn;
    private String tag;
    private String gradering;
    private String tilbyder;
    private String fylke;
    private String kommune;
    private String beskrivelse;
    private String lisens;
    private String url;
    private String tidsbruk;
    private ArrayList<LatLng> coordinates;
    public static ArrayList<Trip> trips;
    private static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();


    public Trip(String id, String navn, String tag, String gradering, String tilbyder, String fylke, String kommune, String beskrivelse, String lisens, String url, ArrayList<LatLng> coordinates, String tidsbruk) {
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
        this.tidsbruk = tidsbruk;
    }
    public static void addTrip(String tripname, ArrayList<LatLng> coords, FirebaseUser user, String difficulty, String fylke, String kommune, String beskrivelse) {
        if (user != null) {
            User.addTrip(tripname);
            myRef.child("trip").child(tripname).child("Created by").setValue(user.getEmail());
        }
        int count = 0;
        for (LatLng i : coords) {
            myRef.child("trip").child(tripname).child("LatLng").child("Lat").child(String.valueOf(count)).setValue(i.latitude);
            myRef.child("trip").child(tripname).child("LatLng").child("Lon").child(String.valueOf(count)).setValue(i.longitude);
            myRef.child("trip").child(tripname).child("Grad").setValue(difficulty);
            myRef.child("trip").child(tripname).child("Fylke").setValue(fylke);
            myRef.child("trip").child(tripname).child("Kommune").setValue(kommune);
            myRef.child("trip").child(tripname).child("Beskrivelse").setValue(beskrivelse);
            count++;
        }
    }


    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.navn = name;
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
    public String getTidsbruk(){
        return tidsbruk;
    }

    @Override
    public String toString(){
        return beskrivelse;
    }



    protected Trip(Parcel in) {
        id = in.readString();
        navn = in.readString();
        tag = in.readString();
        gradering = in.readString();
        tilbyder = in.readString();
        fylke = in.readString();
        kommune = in.readString();
        beskrivelse = in.readString();
        lisens = in.readString();
        url = in.readString();
        tidsbruk = in.readString();
        if (in.readByte() == 0x01) {
            coordinates = new ArrayList<LatLng>();
            in.readList(coordinates, LatLng.class.getClassLoader());
        } else {
            coordinates = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(navn);
        dest.writeString(tag);
        dest.writeString(gradering);
        dest.writeString(tilbyder);
        dest.writeString(fylke);
        dest.writeString(kommune);
        dest.writeString(beskrivelse);
        dest.writeString(lisens);
        dest.writeString(url);
        dest.writeString(tidsbruk);
        if (coordinates == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(coordinates);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}