package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * A class to handle operation like updating current location, force recall of new current location and to get current location.
 * Also shortens code, so that we wont have to write 27 lines of code everytime we need to get current location.
 * @author Magnus P.
 */
public class LocationHandler {
    final static String TAG = "LocationHandler";
    private static Location currentLocation;
    public static boolean CURRENT_LOCATION_IS_SET = false;

    public static void forceUpdateOfCurrentLocation(final Context context) {
        Log.d(TAG,"Starting forceUpdateOfCurrentLocation");
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        int permTrack = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        Log.d(TAG,"permTrack == " + permTrack);
        if (permTrack == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"Permission granted");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 60, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    currentLocation = location;
                    Log.d(TAG,"onLocationChanged finnished, new location has been set to: " + currentLocation.toString());
                    CURRENT_LOCATION_IS_SET = true;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else {
            Log.e(TAG," [99] Permission denied!");
        }
        Log.d(TAG,"forceUpdateOfCurrentLocation has finnished");
    }
    public static Location getCurrentLocation() {
        return currentLocation;
    }



}
