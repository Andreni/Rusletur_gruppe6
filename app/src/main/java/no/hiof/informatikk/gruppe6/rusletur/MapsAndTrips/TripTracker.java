package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;

import static java.lang.Double.valueOf;


    /*
    * In it's current state, this tracker is mainly for saving and storing trips.
    * All coordinates are sent to <LatLng> array where they will stay until the
    * service is terminated.
    *
    * Todo:
    *
     */


public class TripTracker extends Service {

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    public final ArrayList<LatLng> test = new ArrayList<>();



    @Override
    public IBinder onBind(Intent intent) {
        return null;
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


        return START_STICKY;
    }

    private void testMethod() {
        Log.i(MapsActivity.TAG, "Tracker - testMethod called");

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        int trackingGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (trackingGranted == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fClient = LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    locationResult.getLocations();
                    Log.i(MapsActivity.TAG, "Tracker - onLocationResult resolved");
                    Log.i(MapsActivity.TAG, locationResult.getLocations().toString());
                    Log.i(MapsActivity.TAG, "Size of getLocations" + Integer.toString(locationResult.getLocations().size()));
                    LatLng previousLocation = new LatLng(valueOf(locationResult.getLastLocation().getLongitude()), valueOf(locationResult.getLastLocation().getLatitude()));
                    Log.i(MapsActivity.TAG, previousLocation.toString());
                    test.add(previousLocation);
                    Log.i(MapsActivity.TAG, Integer.toString(test.size()));


                    super.onLocationResult(locationResult);
                }


            };
            fClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }

    }


    @Override
    public void onDestroy() {
        Log.i(MapsActivity.TAG, "onDestroy called");


        /*
        * Array with coordinates will be sent through Intent as shown below. LatLng Arrays already
        * implements Parcelable, which means it can be sent through a bundle.
        * EDIT: Nvm bundles. Send it through LocalBroadcastManager.
        * EDIT2: Cheesy method is cheesy.
         */

        if(MainMenuFragment.saveWasClicked == true) {
            Intent sendArrayIntent = new Intent("SendArrayList").putExtra("LatLngArray", test);
            LocalBroadcastManager.getInstance(TripTracker.this).sendBroadcast(sendArrayIntent);
        }

        //Intent sendArrayIntent = new Intent("SendArrayList").putExtra("LatLngArray", test);
        //LocalBroadcastManager.getInstance(TripTracker.this).sendBroadcast(sendArrayIntent);

        //Intent in = new Intent(TripTracker.this, MainScreen.class);
        //Bundle b = new Bundle();
        //b.putSerializable("Array", test);
        //in.putExtra("bundle", b);

        //Bundle b = new Bundle();
        //b.putParcelableArrayList("coordArrayList", test);
        //sendArrayIntent.putExtra("TestArray", b);


        //stopSelf for terminating service (despite stopService being called, wtf)
        //removeLocationUpdates to cancel location updates.
        this.stopSelf();
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        super.onDestroy();
    }


}

    /*
    * * * * * * * * *
    *   Graveyard   *
    *               *
    * * * * * * * * *
            *
            *
            *
       * * * * * **/


    /*
    private class TestThread extends Thread{
        @Override
        public void run() {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            int trackingGranted = ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (trackingGranted == PackageManager.PERMISSION_GRANTED) {
                FusedLocationProviderClient fClient = LocationServices.getFusedLocationProviderClient(getBaseContext());
                fClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        locationResult.getLocations();
                        Log.i(MapsActivity.TAG, "Tracker - onLocationResult resolved");
                        Log.i(MapsActivity.TAG, locationResult.getLocations().toString());
                        Log.i(MapsActivity.TAG, "Size of getLocations" + Integer.toString(locationResult.getLocations().size()));
                        LatLng previousLocation = new LatLng(valueOf(locationResult.getLastLocation().getLongitude()), valueOf(locationResult.getLastLocation().getLatitude()));
                        Log.i(MapsActivity.TAG, previousLocation.toString());
                        //locationArray.add(previousLocation);
                        //Log.i(MapsActivity.TAG, Integer.toString(locationArray.size()));
                        //receiveAndConvert(previousLocation);
                    }
                }, null);
            }

            super.run();
        }



    }


*/


    /*



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

    /*
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
*/
    /*
    new Thread(new Runnable() {
            @Override
            public void run() {
                int trackingGranted = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (trackingGranted == PackageManager.PERMISSION_GRANTED) {
                    FusedLocationProviderClient fClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                    fClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            locationResult.getLocations();
                            Log.i(MapsActivity.TAG, "Tracker - onLocationResult resolved");
                            Log.i(MapsActivity.TAG, locationResult.getLocations().toString());
                            Log.i(MapsActivity.TAG, "Size of getLocations" + Integer.toString(locationResult.getLocations().size()));
                            LatLng previousLocation = new LatLng(valueOf(locationResult.getLastLocation().getLongitude()), valueOf(locationResult.getLastLocation().getLatitude()));
                            Log.i(MapsActivity.TAG, previousLocation.toString());

                            //locationArray.add(previousLocation);
                            //Log.i(MapsActivity.TAG, Integer.toString(locationArray.size()));
                            //receiveAndConvert(previousLocation);
                        }
                    }, null);
                }
            }
        }).start();*/

