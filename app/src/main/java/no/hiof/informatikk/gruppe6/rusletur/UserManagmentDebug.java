package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserManagmentDebug extends AppCompatActivity {

    private static final String TAG = "UserManagmentDebug";
    FirebaseUser mUser;
    FirebaseAuth mAuth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("id");

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_managment_debug);

        try {
            mUser = mAuth.getInstance().getCurrentUser();
            TextView username = findViewById(R.id.usernameDebug);
            username.setText(mUser.getEmail());
        }
        catch(NullPointerException e) {
            Log.e(null, "Finner ikke bruker");
            Log.e(null, mUser.getEmail());
        }

    }

    public void sendInfoToDatabase(View view) {
        Log.d(TAG, "sendInfoToDatabase exectued");
        EditText textToSend = findViewById(R.id.debugInputToSendInToDatabase);
        if(counter == 1) {
            myRef.setValue(String.valueOf(textToSend.getText()));
            counter = 0;
        }
        else {
            try {
                Log.i(TAG, String.valueOf(textToSend.getText()));
            }
            catch(NullPointerException e) {
                Log.e(TAG, "Text from input was NULL");
            }
            counter++;
        }

    }

}
