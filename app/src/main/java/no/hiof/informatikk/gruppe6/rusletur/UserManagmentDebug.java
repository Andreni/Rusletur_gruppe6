package no.hiof.informatikk.gruppe6.rusletur;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.util.JsonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class UserManagmentDebug extends AppCompatActivity {

    private static final String TAG = "UserManagmentDebug";
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = myRef.child("user").child(uid);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_managment_debug);
        /*+++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    }

    public void sendInfoToDatabase(View view) {
        Log.d(TAG, "sendInfoToDatabase exectued");
        EditText textToSend = findViewById(R.id.debugInputToSendInToDatabase);


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User("Magnus","Codemaster","93939393",mUser.getEmail());
        Gson g = new Gson();
        String string = g.toJson(user);

        uidRef.setValue(string);

        Log.e(TAG,"---------------------------------------------------------");
        Log.i(TAG,string);
        Log.e(TAG,"---------------------------------------------------------");

    }

}
