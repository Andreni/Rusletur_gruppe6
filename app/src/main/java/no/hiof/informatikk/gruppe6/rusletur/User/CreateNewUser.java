package no.hiof.informatikk.gruppe6.rusletur.User;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.FirebaseHandler;
import no.hiof.informatikk.gruppe6.rusletur.R;

/**
 * If a new user is detected in MainActivity, the user will be sent to this screen
 * where they fill out their credentials. Their username, firstname and lastname
 * will then be set in firebase using the setAll method found in User.
 * @author Bjørnar P.
 */
public class CreateNewUser extends AppCompatActivity {

    private EditText userName;
    private EditText firstName;
    private EditText lastName;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_newuser);


        mUser = FirebaseAuth.getInstance().getCurrentUser();

        userName = findViewById(R.id.fragment_newuser_username);
        firstName = findViewById(R.id.fragment_newuser_firstname);
        lastName = findViewById(R.id.fragment_newuser_lastname);


        findViewById(R.id.fragment_newuser_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.setAll(userName.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
                Toast.makeText(CreateNewUser.this, "Velkommen " + firstName.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent goToMainIntent = new Intent(CreateNewUser.this, MainScreen.class);
                MainActivity.completedRegistrationPage = true;
                FirebaseHandler.getUserInfo(mUser.getUid()); //why?
                startActivity(goToMainIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        Log.d("CreateNewUser","ON BACK PRESSED!");


        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("user");
        /** Adds a listener that will loop through all dataChange */
        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    /** For each trip in Firebase-Trip */
                    HashMap<String, String> hm = new HashMap<>();
                    for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                        Log.d("MainActivity", "Snapshot: " + zoneSnapshot.getKey());
                        if (zoneSnapshot.getKey().equals(mUser.getUid())) {
                            for (DataSnapshot x : zoneSnapshot.getChildren()) {
                                hm.put(x.getKey(), x.getValue().toString());
                            }
                        }

                    }
                    if (hm.get("username") != null || hm.get("firstname") != null || hm.get("lastname") != null) {
                        Intent newUserIntent = new Intent(CreateNewUser.this, MainActivity.class);
                        startActivity(newUserIntent);
                    }
                }
                catch (NullPointerException e) {
                    Log.i("MainActivity","User not logged in");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
