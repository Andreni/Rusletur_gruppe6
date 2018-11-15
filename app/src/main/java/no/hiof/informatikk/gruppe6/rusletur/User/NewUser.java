package no.hiof.informatikk.gruppe6.rusletur.User;

import android.content.Intent;
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
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;

public class NewUser extends AppCompatActivity {

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
                Toast.makeText(NewUser.this, "Velkommen " + firstName.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent goToMainIntent = new Intent(NewUser.this, MainScreen.class);
                FirebaseHandler.getUserInfo(mUser.getUid()); //why?
                startActivity(goToMainIntent);
            }
        });


    }
}
