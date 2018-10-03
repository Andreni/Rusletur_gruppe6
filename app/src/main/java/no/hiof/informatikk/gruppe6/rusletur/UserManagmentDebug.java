package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserManagmentDebug extends AppCompatActivity {

    private static final String TAG = "UserManagmentDebug.class";
    FirebaseUser mUser;
    FirebaseAuth mAuth;

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

}
