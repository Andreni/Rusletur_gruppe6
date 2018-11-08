package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import no.hiof.informatikk.gruppe6.rusletur.MainScreen;

import static no.hiof.informatikk.gruppe6.rusletur.MainScreen.TAG2;
import static no.hiof.informatikk.gruppe6.rusletur.MainScreen.getAllUserInfo;

/**
 * A class used for checking data of the firebasedatabase.
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
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("trip");
        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentTripname = "";
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Tripname: " + (String) zoneSnapshot.child("name").getValue());
                    currentTripname = (String)zoneSnapshot.child("name").getValue();
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

    //Takes UID from User in Auth, checks for corresponding UID in Database and retrieves username, firstname and lastname.
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







    private static void createStartLocationOfTrip(final String tripName) {
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("trip");
        zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double lat = (double) dataSnapshot.child(tripName).child("LatLng").child("Lat").child("0").getValue();
                double lon = (double) dataSnapshot.child(tripName).child("LatLng").child("Lon").child("0").getValue();
                startPos = new LatLng(lat, lon);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static LatLng getStartLocationOfTrip(final String tripName) {
        createStartLocationOfTrip(tripName);
        try {
            TimeUnit.MILLISECONDS.sleep(200);
            Log.d(TAG,"Waited 200ms");
        }
        catch (Exception e) {
            Log.e(TAG, "Could not sleep... \n" + e.toString());
        } finally {
            Log.d(TAG,"Returning: " + startPos.toString());
            return startPos;
        }

    }

}
