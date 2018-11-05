package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
    A class to handle operation like updating current location, force recall of new current location and to get current location.
    Also shortens code, so that we wont have to write 27 lines of code everytime we need to get current location.
 */

public class LocationHandler {
    final static String TAG = "LocationHandler";
    private static GoogleMap mMap;
    private static Location currentLocation;
    public static boolean CURRENT_LOCATION_IS_SET = false;
    private static LatLng currentPosition;
    private static LatLng tripStartLocation = null;
    static String status = "READY";
    public static final String STATUS_READY = "READY";
    public static final String STATUS_RUNNING = "RUNNING";

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


    public static void showPathToTripFromCurrentPosition(GoogleMap gMap, LatLng tripStartLocation) {
        mMap = gMap;
        status = STATUS_RUNNING;
        Log.d(TAG,"********************************************************************START********************************************************************");
        if(CURRENT_LOCATION_IS_SET) {
            Log.d(TAG," [0] Current location is set");
            try {
                currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                String url = getDirectionsUrl(currentPosition, tripStartLocation);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            } catch (NullPointerException e) {
                if(currentPosition == null && tripStartLocation == null) {
                    Log.e(TAG, "currentPosition and tripStartLocation is null \n" + e.toString());
                }
                else if(tripStartLocation == null) {
                    Log.e(TAG, "tripStartLocation is null \n" + e.toString());
                }
                else if(currentPosition == null) {
                    Log.e(TAG, "currentPosition is null \n" + e.toString());
                } else {
                    Log.e(TAG, "Something was null?\n" + e.toString());
                }
            }
        } else {
            Log.e(TAG," [1] Current location is not set. Is GPS turned on?");
        }
        Log.d(TAG,"********************************************************************END********************************************************************");
    }
    private static String getDirectionsUrl(LatLng startPos, LatLng endPos) {
        //Builds an URL compatible with google maps. Send it to google maps and it will return the full trip with all info needed. Remember to parse.
        String str_origin = "origin=" + startPos.latitude + "," + startPos.longitude;
        String str_dest = "destination=" + endPos.latitude + "," + endPos.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String apiKey = "&key=AIzaSyBWjR1IKXkbZKUGNhiI76AQJ1mt1YtRuZY";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + apiKey;
        Log.d(TAG,"URL complete. Result: " + url);
        return url;
    }

    //To download the JSON file
    private static String downloadUrl(String strUrl) throws IOException {
        Log.d(TAG,"Starting downloading of url...");
        String data = null;
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            /*TODO
             * 1. Use https instead of http
             */
            Log.d(TAG,"Trying to use http connection...");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            Log.d(TAG,"url connected, trying to get input stream...");
            iStream = urlConnection.getInputStream();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(iStream));
            Log.d(TAG,"BufferReader created...");
            StringBuffer stringBuff = new StringBuffer();
            Log.d(TAG,"StirngBuffer created. Starting to read all lines form buffer...");
            String line = "";
            int lineCount = 0;
            while ((line = buffReader.readLine()) != null) {
                Log.d(TAG,"Current line " + lineCount + ":  \n" + line);
                stringBuff.append(line);
                lineCount++;
            }
            Log.d(TAG,"Finnished reading all lines. Total lines readed: " + lineCount + "...");
            Log.d(TAG,"Converts StringBuffer to string...");
            data = stringBuff.toString();
            buffReader.close();
            Log.d(TAG,"BufferReader is not closed...");

        } catch (Exception e) {
            Log.d(TAG, "Download failed\n\n" + e.toString());
        } finally {
            //Closes the stream
            iStream.close();
            Log.d(TAG,"InputStream is now closed...");
            //Disconnects from the url
            urlConnection.disconnect();
            Log.d(TAG,"Disconnected from url...");
        }
        return data;
    }

    // Fetches data from url passed
    private static class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            Log.d(TAG,"doInBackground started");
            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                Log.d(TAG,"Creating data...");
                data = downloadUrl(url[0]);
                Log.d(TAG,"Data successfully created");
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    /**
     * A class to parse the Google Places in JSON format, by Google
     */
    private static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Log.e(TAG,"No results from google directions...");
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.BLUE);
            }


            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }
}
