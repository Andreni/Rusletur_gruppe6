package no.hiof.informatikk.gruppe6.rusletur;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edPass;
    EditText edEmail;
    ConstraintLayout loginPage;

    private FirebaseAuth mAuth;
    private FirebaseUser mAUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO 1.1 Make simple login screen:
        //TODO 1.2 Make registration page:
        //TODO 1.3 If statement to check if user is logged in
        //TODO 1.4 Make horizontal layout (Non priority)


    }

    //Click from button
    public void loginUser(View view){

        if(checkForValidUserInput()){
            mAuth.signInWithEmailAndPassword(edEmail.getText().toString()
                    ,edPass.getText().toString());
        }
    }

    //Click from button
    public void registerUser(View view){


    }

    //Check if field values entered are correct:
    private boolean checkForValidUserInput(){

        if ((edEmail.toString()!=null)&&(edPass!=null)){

            if(edPass.toString().length()>5){

                return true;
            }
        }
        return false;
    }
}
