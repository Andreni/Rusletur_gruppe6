package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class TripTracker extends Service {

    private LocationRequest locationRequest;
    public static ArrayList<LatLng> tempLocationArray;
    private LocationCallback locationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        requestLocation();
    }

    //Sett opp metode for å sjekke permissions

    private void requestLocation(){

        //Sett opp

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(30000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        /* IKKJE SIKKER PÅ UM E TRENG DETTA
        LocationSettingsRequest.Builder lBuider = new LocationSettingsRequest.Builder();
        lBuider.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = lBuider.build();
        */

        int trackingGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (trackingGranted == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fClient = LocationServices.getFusedLocationProviderClient(this);
            fClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    locationResult.getLocations();

                    for(Location lastLocation : locationResult.getLocations()){
                        storeLocation(lastLocation);
                    }
                }
            }, null);
        }
    }



    public void storeLocation(Location location){
        tempLocationArray.add(new LatLng(location.getLatitude(), location.getLongitude()));
    }


}
