package no.hiof.informatikk.gruppe6.rusletur.User;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.R;

/**
 *                              User.class
 * It is used as a easier way to collaborate the realtime database
 * with information from user and Authentication database.
 * A user.class will always be created when the user logs in.
 *
 * Treat User.class as the logged in user, as the user.class will always store
 * its information directly onto the UID branch, and also call from the UID branch.
 *
 *      Example:
 *          Anna want to change her email,
 *          the app should do:
 *              User anna = new User(FirebaseUser);
 *              anna.setEmail("AnnasNewEmail@Mail.com");
 *          This will update the email in the auth DB, and the RTDB.
 *
 *
 *          Anna wanna change her phone number? Call new User()
 */

public class User {

    private static final String TAG = "User";
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference uidRef = myRef.child("user").child(uid);
    private FirebaseUser mUser;

    public User(FirebaseUser user) {
        this.mUser = user;
    }


    //Method to set all basic information about user. Preferable used in Registration of user.
    public void setAll(String username, String firstname, String lastname) {
        uidRef.child("username").setValue(username);
        uidRef.child("firstname").setValue(firstname);
        uidRef.child("lastname").setValue(lastname);
    }
    public String getUsername() {
        return uidRef.child("username").getKey();
    }
    public void setUsername(String username) {
        uidRef.child("username").setValue(username);
    }
    public void setFirstname(String firstname) {
        uidRef.child("firstname").setValue(firstname);
    }
    public void setLastname(String lastname) {
        uidRef.child("lastname").setValue(lastname);
    }
    public void setEmail(String email, String password) {
        //Change out password and NEW_EMAIL with real inputs
        final String NEW_EMAIL = email;
        // Ask user to provide the password for authentication.
        AuthCredential credential = EmailAuthProvider.getCredential(mUser.getEmail(), password);
        // Runs re-authentication.
        mUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Creditentials are valid");
                //Updates Email in RTDB.
                uidRef.child("email").setValue(NEW_EMAIL);
                //Updates Email in Auth-DB.
                mUser.updateEmail(NEW_EMAIL).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG,"Email is now changed to " + NEW_EMAIL);
                    }
                });
            }
            //If authentication fails, do this:
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG,"Creditentials are invalid");
            }
        });
    }
    /*
        Saves the trip like this:
        user
           |-Trip
               |-Tripname
                    |-LatLng1
                    |-LatLng2
                    |-LatLng3

     */
    public void addTrip(ArrayList<LatLng> trip, String tripName) {
        for(LatLng coords : trip) {
            uidRef.child("trip").child(tripName).setValue(coords);
        }
    }
    //TODO
    // 1. Change return value to array<LatLng>. Get tab length.
    public String getTrip(String tripName) {
        return uidRef.child("trip").child(tripName).getKey();
    }

}
