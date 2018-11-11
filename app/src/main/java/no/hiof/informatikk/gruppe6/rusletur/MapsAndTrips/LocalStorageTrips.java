package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import no.hiof.informatikk.gruppe6.rusletur.Model.LocalStorage;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.MainTripRecyclerViewAdapter;

import static no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip.trips;

/**
 * Class for displaying stored Trip objects
 * SQLite
 * When the user either wants to store or load objects, the static methods are called upon
 */
public class LocalStorageTrips extends AppCompatActivity {
    Button btnBack;
    private MainTripRecyclerViewAdapter mainTripAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_storage_trips);
        btnBack = findViewById(R.id.localStorage_goBack_button);

        //TODO Make prepared statements


        // Initialize recyclerview and set adapter
        //LocalStorage localStorage = LocalStorage.getInstance(this);

        
        RecyclerView recyclerView = findViewById(R.id.local_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LocalStorage localStorage = LocalStorage.getInstance(this);



        mainTripAdapter = new MainTripRecyclerViewAdapter(this, localStorage.getAllTrips());
        recyclerView.setAdapter(mainTripAdapter);


    }



    public void goBack(View view){
        super.onBackPressed();
    }
}



