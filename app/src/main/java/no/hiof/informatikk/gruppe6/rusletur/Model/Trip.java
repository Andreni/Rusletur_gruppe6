package no.hiof.informatikk.gruppe6.rusletur.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.GoogleDirections;
import no.hiof.informatikk.gruppe6.rusletur.User.User;


/**
 * Trip is a class that produces trip objects which contains all necessary variables used to
 * show the trip in maps. Distinguish each trip form each other and to store difference kind of
 * meta infromation, like coordinates and id.
 * @author Magnus P.
 * @author Andreas M.
 * @author Andreas N.
 *
 */
public class Trip implements Parcelable, Comparable<Trip> {


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
    private GoogleDirections googleDirections;
    private static int idCount = 0;
    public static ArrayList<Trip> trips;
    private static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    public static ArrayList<Trip> allCustomTrips = new ArrayList<>();


    /**
     * Trip constructor.
     * @param id the trip ID. If owned by Rusletur, ID then starts with "rusletur_", else random UUID
     * @param navn name of the trip
     * @param tag tag added to the trip
     * @param gradering Difficulty of trip
     * @param tilbyder which one offers this trip(Rusletur, a user, nasjonalturbase)
     * @param fylke which fylke the trip is in
     * @param kommune which kommune the trip is in
     * @param beskrivelse description of the trip
     * @param lisens What type of license the trip has
     * @param url if it came from and url, show url.
     * @param coordinates An arraylist of LatLng containing the whole trip.
     * @param tidsbruk Estimated time of use from start to finnish of the trip
     */
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
        createGoogleDirections();

    }

    /**
     * A method to add a trip to the Firebase Realtimedatabase. As per default, the trip will be
     * stored in a separated top-layer database named "trip". The key that binds the creator and
     * user is the child "Created by". This will have the users email.
     * @param tripname
     * @param coords
     * @param user
     * @param difficulty
     * @param fylke
     * @param kommune
     * @param beskrivelse
     * @param tag
     * @param lisens
     * @param tidsbruk
     * @param url
     * @param tilbyder
     */
    public static void addTrip(String tripname, ArrayList<LatLng> coords, FirebaseUser user,
                               String difficulty, String fylke, String kommune,
                               String beskrivelse, String tag, String lisens,
                               String tidsbruk, String url, String tilbyder) {
        if (user != null) {
            User.addTrip(tripname);
            myRef.child("trip").child(tripname).child("Created by").setValue(user.getEmail());
        } else {
            myRef.child("trip").child(tripname).child("Created by").setValue("Rusletur");
        }
        myRef.child("trip").child(tripname).child("Id").setValue("rusletur_"+UUID.randomUUID());
        myRef.child("trip").child(tripname).child("Grad").setValue(difficulty);
        myRef.child("trip").child(tripname).child("Lisens").setValue(lisens);
        myRef.child("trip").child(tripname).child("Tidsbruk").setValue(tidsbruk);
        myRef.child("trip").child(tripname).child("URL").setValue(url);
        myRef.child("trip").child(tripname).child("Tilbyder").setValue(tilbyder);
        myRef.child("trip").child(tripname).child("Fylke").setValue(fylke);
        myRef.child("trip").child(tripname).child("Beskrivelse").setValue(beskrivelse);
        myRef.child("trip").child(tripname).child("Kommune").setValue(kommune);
        myRef.child("trip").child(tripname).child("Tag").setValue(tag);
        int count = 0;
        for (LatLng i : coords) {
            myRef.child("trip").child(tripname).child("LatLng").child(String.valueOf(count)).setValue(i.latitude + "¤" + i.longitude);
            count++;
        }
        idCount++;
    }
    private void createGoogleDirections() {
        new GoogleDirections(this);
    }

    /**
     * Sort by the length from user to trip start location
     */
    @Override
    public int compareTo(Trip o) {
        int a = this.getGoogleDirections().getDistanceRaw();
        int b = o.getGoogleDirections().getDistanceRaw();
        return a-b;
    }

    /**
     * Quick-way to get the start location of the trip
     * @return the first LatLng of the trip
     */
    public LatLng getStartLatLng() {
        return getCoordinates().get(0);
    }


    /**
     * Setters and getters for variables
     */
    public void setGoogleDirections(GoogleDirections googleDirections) {
        this.googleDirections = googleDirections;
    }
    public void setName(String name) {
        this.navn = name;
    }
    public GoogleDirections getGoogleDirections() {
        return googleDirections;
    }

    public String getId() {
        return id;
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
    public String getTag() {
        return tag;
    }

    @Override
    public String toString(){
        return beskrivelse;
    }

    /**
     * All code below is for Parcelable
     */
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

    /**
     * Writes the object as a parcable
     * @param dest
     * @param flags
     */
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
