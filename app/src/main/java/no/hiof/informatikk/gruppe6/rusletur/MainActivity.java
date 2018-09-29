package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final String logTagUser = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO 1.1 Make simple login screen:
        //TODO 1.2 Make registration page:
        //TODO 1.3 If statement to check if user is logged in
    }

    //Method for signing up user
    public void registerUser(View view){


    }

    public void checkUserInput(Boolean validInput){

    }

    public void writeToastToUser(String messageToUser){

    }
}
