package no.hiof.informatikk.gruppe6.rusletur;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public class UserUtility {

    private static final int REQUEST_FINE_LOCATION = 1;

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
        //Returns true if user has an active connection, else returns false.
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean userHasConnection = networkInfo.isConnected();

        return userHasConnection;
    }
}
