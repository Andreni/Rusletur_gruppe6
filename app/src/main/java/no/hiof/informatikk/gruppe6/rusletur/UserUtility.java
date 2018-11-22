package no.hiof.informatikk.gruppe6.rusletur;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Bjørnar P
 * @author Andreas N
 * @version 1.0
 */
public class UserUtility  {

    private static final int REQUEST_FINE_LOCATION = 1;
    private static final String TAG = "UserUtility" ;

    /*
    * Activity: checkIfUserHasPermissionsEnabled(Activity.this)
    * Fragment: checkIfUserHasPermissionsEnabled(getActivity())
    *
     */

    public static boolean checkIfUserHasPermissionsEnabled(Context context){
        //Check if User has ACCESS_FINE_LOCATION permitted
        if(ContextCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            return false;
        }
    }

    public static void requestPermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION);
    }

    public static boolean checkIfUserHasMobileNetworkEnabled(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        return isConnected;
    }


    public static boolean checkIfUserHasGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }
}
