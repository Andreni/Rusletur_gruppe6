package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MainActivity;

import static java.lang.Double.valueOf;


public class TripTracker extends IntentService {

    private LocationRequest locationRequest;
    public static ArrayList<LatLng> tempLocationArray;
    private LocationCallback locationCallback;
    public ArrayList<LatLng> locationArray;

    public TripTracker(String name, LocationRequest locationRequest) {
        super(name);
        this.locationRequest = locationRequest;
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TripTracker(String name) {
        super(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(MapsActivity.TAG, "Tracker - onCreate called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(MapsActivity.TAG, "Tracker - onStartCommmand called");
        testMethod();
        return super.onStartCommand(intent, flags, startId);
    }

    //Sett opp metode for å sjekke permissions

    private void testMethod(){
        Log.i(MapsActivity.TAG, "Tracker - testMethod called");

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        int trackingGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (trackingGranted == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fClient = LocationServices.getFusedLocationProviderClient(this);
            fClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    locationResult.getLocations();
                    Log.i(MapsActivity.TAG, "Tracker - onLocationResult resolved");
                    Log.i(MapsActivity.TAG, locationResult.getLocations().toString());
                    Log.i(MapsActivity.TAG, "Size of getLocations" + Integer.toString(locationResult.getLocations().size()));
                    LatLng previousLocation = new LatLng(valueOf(locationResult.getLastLocation().getLongitude()), valueOf(locationResult.getLastLocation().getLatitude()));
                    Log.i(MapsActivity.TAG, previousLocation.toString());
                    //receiveAndConvert(previousLocation);
                }
            }, null);
        }
    }

    private void receiveAndConvert(LatLng latLng){
        locationArray.add(latLng);
        Log.i(MapsActivity.TAG, locationArray.toString());
        Log.i(MapsActivity.TAG, Integer.toString(locationArray.size()));

    }

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
                    Log.i(MapsActivity.TAG, "FusedClient gets coordinates");
                    for(Location lastLocation : locationResult.getLocations()){
                        Log.i(MapsActivity.TAG, "For loop" + lastLocation);
                    }
                }
            }, null);
        }
    }



    public void storeLocation(Location location){
        tempLocationArray.add(new LatLng(location.getLatitude(), location.getLongitude()));
    }


}
