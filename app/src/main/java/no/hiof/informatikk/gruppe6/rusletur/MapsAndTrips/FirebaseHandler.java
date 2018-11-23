package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;

import static no.hiof.informatikk.gruppe6.rusletur.MainScreen.TAG2;
import static no.hiof.informatikk.gruppe6.rusletur.MainScreen.getAllUserInfo;

/**
 * A class used for checking data of the firebasedatabase.
 * @author Magnus P.
 * @author Bjørnar P.
 */
public class FirebaseHandler {

    private final static String TAG = FirebaseHandler.class.toString();
    private static boolean found = false;
    protected static LatLng startPos = null;

    /**
     * Checks if a trip already exists within the databsase.
     * @param tripName is the name of the trip that needs to be scanned up to all trips withing the database.
     * @return true if trip is database. False if trip was not found in database.
     */
    public static boolean isTripInFirebaseDatabase(final String tripName) {
        /** Sets the reference zone in the Realtime database to Trip */
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("trip");
        /** Adds a listener that will loop through all dataChange */
        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /** For each trip in Firebase-Trip */
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Tripname: " + (String) zoneSnapshot.child("name").getValue());
                    /** If currentTripname equals the tripname beeing checked, set  */
                    String currentTripname = (String)zoneSnapshot.child("name").getValue();
                    if(tripName == currentTripname) {
                        found = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(found) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method for downloading the {@link Trip} objects from Firebase
     */
    public static void downloadAllCustomTrips() {
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("trip");
        zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Trip.allCustomTrips.clear();
                /* For each Child in Trip */
                for(DataSnapshot aShot : dataSnapshot.getChildren()) {
                    /* HashMap that will store the String tripname and a Hashmap with all of the childs. */
                    HashMap<String, HashMap<String, String>> outer = new HashMap<>();
                    /* For storing the LatLng of current trip */
                    ArrayList<LatLng> coords = new ArrayList<>();
                    /* HashMap for every childnode in the DataSnapshot */
                    HashMap<String, String> inner = new HashMap<>();
                    Log.d(TAG, "Obj: " + aShot.getKey());
                    /* For each Child of current trip */
                    for(DataSnapshot bShot : aShot.getChildren() ) {
                        Log.d(TAG, "LatLng == " + bShot.getKey());
                        /* For each child of current child(Tag and LatLng) */
                        for(DataSnapshot latLngTag : bShot.getChildren()) {
                            if(bShot.getKey().equals("Tag")) {
                                continue;
                            }
                            /* Splits the LatLng... Data[0] == Lat, Data[1] == Lon */
                            String[] data = ((String)latLngTag.getValue()).split("¤");
                            /* Creates a new LatLng and adds it to the coords ArrayList */
                            coords.add(new LatLng(Double.parseDouble(data[0]),Double.parseDouble(data[1])));

                        }
                        /* If a Key does not have a value, set value to "Not set..." to prevent nullPointerException. */
                        if(TextUtils.isEmpty(bShot.getValue().toString())) {
                            inner.put(bShot.getKey(), "Not set...");
                            continue;
                        }
                        /* Adds the Key and Value to the hashmap */
                        inner.put(bShot.getKey(), bShot.getValue().toString());
                    }
                    /* Adds all the childnodes to the outer HashMap */
                    outer.put(aShot.getKey(), inner);

                    /* For current trip */
                    for(Map.Entry<String, HashMap<String, String>> i : outer.entrySet()) {
                        /* Create variables for each parameter to create a trip */
                        Log.d(TAG, "Navn: " + i.getKey());
                        Log.d(TAG,"Id: " + i.getValue().get("Id"));
                        String id = i.getValue().get("Id");
                        String navn = i.getKey();
                        String gradering = i.getValue().get("Grad");
                        String tag = i.getValue().get("Tag");
                        String tilbyder = i.getValue().get("Tilbyder");
                        String fylke = i.getValue().get("Fylke");
                        String kommune = i.getValue().get("Kommune");
                        String beskrivelse = i.getValue().get("Beskrivelse");
                        String lisens = i.getValue().get("Lisens");
                        String url = i.getValue().get("URL");
                        String tidsbruk = i.getValue().get("Tidsbruk");
                        /* Add trip to public static allCustomTrip. */
                        Trip.allCustomTrips.add(new Trip(id, navn, tag, gradering, tilbyder, fylke, kommune, beskrivelse, lisens, url, coords, tidsbruk));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Takes UID from User in Auth, checks for corresponding UID in Database and retrieves username, firstname and lastname.

    /**
     * Retrieving username, firstname and lastname og the user from firebase.
     * @param userUid The users id from {@link no.hiof.informatikk.gruppe6.rusletur.User.User}
     */
    public static void getUserInfo(final String userUid){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child(userUid).child("username").getValue();
                String firstname = (String) dataSnapshot.child(userUid).child("firstname").getValue();
                String lastname = (String) dataSnapshot.child(userUid).child("lastname").getValue();
                Log.i(TAG2, "Fra FirebaseHandler: " + username);
                Log.i(TAG2, "Fra FirebaseHandler: " + firstname);
                Log.i(TAG2, "Fra FirebaseHandler: " + lastname);
                getAllUserInfo(username, firstname, lastname);
        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
