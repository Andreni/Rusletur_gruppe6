package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.SaveTripFragment;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.TripTracker.LocalTrackBinder;

public class ShowProgressOfTrip extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private ArrayList<LatLng> receievedList;
    private LatLng startLocation;
    public  final String TAG = "ShowProgressOfTrip";

    private TripTracker tripTracker;
    private boolean isBound = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progressmap);

        Intent bindToTrackerIntent = new Intent(this, TripTracker.class);
        bindService(bindToTrackerIntent,serviceConnection, Context.BIND_AUTO_CREATE );

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.progress_map_gmap);
        supportMapFragment.getMapAsync(this);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        tripTracker.getArray();
        Log.i(MapsActivity.TAG, "Size of array : " + String.valueOf(tripTracker.getArray().size()));


        PolylineOptions poly = new PolylineOptions();
        startLocation = new LatLng(tripTracker.getArray().get(0).longitude, tripTracker.getArray().get(0).latitude);

        //dRAW POLYLINES.
        for(int i=0; i < tripTracker.getArray().size();i++){
            poly.add(new LatLng(tripTracker.getArray().get(i).longitude, tripTracker.getArray().get(i).latitude));
        }

        //Fløtt kamera
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(startLocation, 12, 0, 0)));

        //Fargefaen
        poly.color(Color.GREEN);
        poly.width(10);
        gMap.addPolyline(poly);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalTrackBinder binder = (LocalTrackBinder) service;
            tripTracker = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };


}
