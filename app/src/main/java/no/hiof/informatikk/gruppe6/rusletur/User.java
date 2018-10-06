package no.hiof.informatikk.gruppe6.rusletur;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *                              User.class
 * It is used as a easier way to collaborate the realtime database
 * with information from user and Authentication database.
 * A user.class will always be created when the user logs in.
 *
 * Treat User.class as the as the user.class will always store
 * its information directly onto the UID branch.
 *
 *      Example:
 *          Anna want to updated her email. After updating the email,
 *          the app should do:
 *              User anna = new User(FirebaseUser);
 *              anna.updateEmail();
 *
 *          Anna wanna change her phone number? Call new User()
 */

public class User {

    private static final String TAG = "User";
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = myRef.child("user").child(uid);
    String firstname;
    String lastname;
    String email;
    String phone;
    String username;
    FirebaseUser user;

    public User(FirebaseUser user) {
        this.user = user;
    }

    public void setAll(String username, String firstname, String lastname, String phone, String email) {

        uidRef.child("username").setValue(username);
        uidRef.child("firstname").setValue(firstname);
        uidRef.child("lastname").setValue(lastname);
        uidRef.child("phone").setValue(phone);
        try {
            uidRef.child("email").setValue(user.getEmail());
            this.email = user.getEmail();
        }
        catch (NullPointerException e) {
            Log.e(TAG,"getEmail() returned NULL. Is anyone logged in?");
        }
    }

    public void setUsername(String username) {
        uidRef.child("username").setValue(username);
        this.username = username;
    }
    public void setFirstname(String firstname) {
        uidRef.child("firstname").setValue(firstname);
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        uidRef.child("lastname").setValue(lastname);
        this.lastname = lastname;
    }
    public void setPhone(String phone) {
        uidRef.child("phone").setValue(phone);
        this.phone = phone;
    }
    public void updateEmail() {
        try {
            uidRef.child("email").setValue(user.getEmail());
            this.email = user.getEmail();
        }
        catch (NullPointerException e) {
            Log.e(TAG,"getEmail() return NULL. Are you logged in?");
        }
    }


}
