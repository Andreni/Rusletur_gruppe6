package no.hiof.informatikk.gruppe6.rusletur.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_newuser);

        pref = PreferenceManager.getDefaultSharedPreferences(this);


        mUser = FirebaseAuth.getInstance().getCurrentUser();

        userName = findViewById(R.id.fragment_newuser_username);
        firstName = findViewById(R.id.fragment_newuser_firstname);
        lastName = findViewById(R.id.fragment_newuser_lastname);


        findViewById(R.id.fragment_newuser_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Sjekk om pref er true eller false

                //if true -> intent til mainscreen, false -> intent til mainactivity



                User.setAll(userName.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
                Toast.makeText(CreateNewUser.this, "Velkommen " + firstName.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent goToMainIntent = new Intent(CreateNewUser.this, MainScreen.class);
                goToMainIntent.putExtra("newUser",false);
                FirebaseHandler.getUserInfo(mUser.getUid()); //why?
                startActivity(goToMainIntent);
            }
        });


    }
}
