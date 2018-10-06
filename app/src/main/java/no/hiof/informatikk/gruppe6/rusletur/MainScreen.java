package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class MainScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    EditText testText;
    Button testButton;

    EditText userEmail;
    Button signOut;

    /*
    REMOVE ME!!!
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        userEmail = findViewById(R.id.mainScreen_emialField_editText);
        signOut = findViewById(R.id.mainScreen_logout_Button);
        //TODO Remove Activity and class when done testing
        mAuth = FirebaseAuth.getInstance();
        //Get user currently signed in
        mUser = mAuth.getCurrentUser();
        userEmail.setText(mUser.getEmail());
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log out user from app
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainScreen.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void goToMaps(View view) {
        startActivity(new Intent(MainScreen.this, MapsActivity.class));
    }

    public void sendToDebugUserManagement(View view) {
        startActivity(new Intent(this, UserManagmentDebug.class));
    }
    public void goToUserRegistration(View view) {
        startActivity(new Intent(this, UserRegistration.class));
    }

    public void testUIDRetrival(View view) {
        /*
        Method for getting userIDTokens. IDTokens must be used to reference backend, not UID.

         */

        mUser = mAuth.getInstance().getCurrentUser();

        testButton = findViewById(R.id.testUIDButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.getUid();
                mUser.getEmail();
                testText.setText(mUser.getDisplayName());

                //Get the current users ID.
                mUser.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if(task.isSuccessful()){

                                }
                                else {
                                    //Handle error
                                }
                            }
                        });
            }
        });




    }

    public void openRegistrerView(View view) {
        Intent intent = new Intent(MainScreen.this, UserManagement.class);
        startActivity(intent);
    }
    /*
    REMOVE ME!!!
     */
}
