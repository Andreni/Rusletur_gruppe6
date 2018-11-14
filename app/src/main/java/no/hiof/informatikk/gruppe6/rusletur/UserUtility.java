package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;

public class UserUtility {

    /*
    * Activity: checkIfUserHasPermissionsEnabled(Activity.this)
    * Fragment: checkIfUserHasPermissionsEnabled(getActivity())
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

    public static boolean checkIfUserHasMobileNetworkEnabled(Context context){
        //Returns true if user has an active connection, else returns false.
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean userHasConnection = networkInfo.isConnected();

        return userHasConnection;
    }
}
