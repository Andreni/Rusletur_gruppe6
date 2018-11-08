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
 * It is used as an easier way to collaborate the realtime database
 * with information from user and Authentication database.
 *
 * Treat User.class as the logged in user, as the user.class will always store
 * its information directly onto the UID branch, and also call from the UID branch.
 *
 *      Example:
 *          Anna want to change her email,
 *          the app should do:
 *              User.setEmail(FirebaserUser, "AnnasNewEmail@Mail.com", "password");
 *          This will update the email in the auth DB, and the RTDB.
 *
 */

public class User {

    private static final String TAG = "User";
    private static String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference uidRef = myRef.child("user").child(uid);
    private static FirebaseUser mUser = null;

    //Method to set all basic information about user. Preferable used in Registration of user.
    public static void setAll(String username, String firstname, String lastname) {
        uidRef.child("username").setValue(username);
        uidRef.child("firstname").setValue(firstname);
        uidRef.child("lastname").setValue(lastname);
    }
    public static String getUsername(FirebaseUser user) {

        return uidRef.child("username").getKey();
    }
    public static void setUsername(String username) {
        uidRef.child("username").setValue(username);
    }
    public static void setFirstname(String firstname) {
        uidRef.child("firstname").setValue(firstname);
    }
    public static void setLastname(String lastname) {
        uidRef.child("lastname").setValue(lastname);
    }
    public static void setEmail(FirebaseUser user, String email, String password) {
        mUser = user;
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
                Log.e(TAG,"Creditentials are invalid");
            }
        });
    }
    public static void addTrip(String tripName) {
        uidRef.child("trip").child(tripName);
    }

    public static DatabaseReference getUidRef() {
        return uidRef;
    }

}
