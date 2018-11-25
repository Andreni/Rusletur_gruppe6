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

    private static final int REQUEST_FINE_LOCATION = 1;
    private static final String TAG = "UserUtility" ;


    /**
     * Checks if user has access fine location enabled. Takes relevant context for ease of use
     * @param context this context.
     * @return true if access_fine_location is enabled, false if not
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

    /**
     * Requests permission. Takes relevant activity for ease of use.
     * @param activity relevant activity.
     */

    public static void requestPermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION);
    }

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
