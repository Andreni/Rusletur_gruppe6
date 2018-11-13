package no.hiof.informatikk.gruppe6.rusletur.UserManagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.FirebaseHandler;
import no.hiof.informatikk.gruppe6.rusletur.R;

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

    }

    public void myDebugButton(View view) {

    }


}
