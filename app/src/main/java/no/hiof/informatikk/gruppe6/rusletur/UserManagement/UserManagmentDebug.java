package no.hiof.informatikk.gruppe6.rusletur.UserManagement;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.User.User;

public class UserManagmentDebug extends AppCompatActivity {

    private static final String TAG = "UserManagmentDebug";
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = myRef.child("user").child(uid);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_managment_debug);
        /*+++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    }

    public void myDebugButton(View view) {
        Log.e(TAG,"---------------------------------------------------------");
        EditText debugInput = (EditText)findViewById(R.id.debugInputToSendInToDatabase);
        String password = debugInput.getText().toString();

        User user = new User(mUser);
        user.setEmail("alpha3@hotmail.com", password);
        Log.e(TAG,"---------------------------------------------------------");

    }

}
