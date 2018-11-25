package no.hiof.informatikk.gruppe6.rusletur;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Used for checking if the device has internet and GPS enabled
 *  @author Bjørnar P
 *  @author Andreas N
 *  * @version 1.0
 */
public class UserUtility  {

    private static final String TAG = "UserUtility" ;
    
    /**
     * Uses ConnectivityManager and NetworkInfo to check if an active connection is established or in progress.
     * @param context class context
     * @return true if connected/connecting, false if not
     */

    public static boolean checkIfUserHasMobileNetworkEnabled(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        return isConnected;
    }

    /**
     * Uses LocationManager to check for GPS provider
     * @param context relevant context for ease of use
     * @return returns true if gps_provider is enabled
     */

    public static boolean checkIfUserHasGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }
}
