package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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

import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;

/**
 * @author Magnus P.
 *  An object containing the distance, duration and polylines from
 *  users current location to start position of the trip selected.
 *
 *      Tutorial used to create this class:
 *              https://www.journaldev.com/13373/android-google-map-drawing-route-two-points
 */
public class GoogleDirections {
    private static final String TAG = "GoogleDirections";
    private LatLng currentPosition;
    private LatLng tripStartLocation = null;
    private String status = "READY";
    private PolylineOptions polylineOptions = null;
    private String distance;
    private int distanceRaw;
    private String duration;
    private int durationRaw;
    private String tripName; 
    private Trip trip;
    public static ArrayList<GoogleDirections> googleDirectionsArrayList = new ArrayList<>();

    public GoogleDirections(Trip trip) {
        this.trip = trip;
        this.tripName = trip.getNavn();
        this.tripStartLocation = trip.getStartLatLng();
        try {
            currentPosition = new LatLng(LocationHandler.getCurrentLocation().getLatitude(), LocationHandler.getCurrentLocation().getLongitude());
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

        googleDirectionsArrayList.add(this);
    }

    /*****************************************
     * Settere and gettere for all variables.
     ****************************************/
    public String getTripName() {
        return tripName;
    }
    public String getDistance() {
        return distance;
    }
    public int getDurationRaw() {
        return durationRaw;
    }
    public String getStatus() {
        return status;
    }
    public String getDuration() {
        return duration;
    }
    public int getDistanceRaw() {
        return distanceRaw;
    }
    public PolylineOptions getPolylineOptions() {
        if(polylineOptions != null) {
            return polylineOptions;
        }
        else {
            Log.e(TAG,"No polylines in googleDirection object.");
            return null;
        }
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public void setDistanceRaw(int distanceRaw) {
        this.distanceRaw = distanceRaw;
    }
    public void setDurationRaw(int durationRaw) {
        this.durationRaw = durationRaw;
    }
    public void addGoogleDirectionsToTrip() {
        trip.setGoogleDirections(this);
    }
    private void setStatus(String currentStatus) {
        status = currentStatus;
    }

    /**
     * Builds a url compatible with google maps directions. Used to download directions json from google directions.
     * @param startPos start location of trip selected.
     * @param endPos end location of trip selected
     * @return custom url for current trip selected.
     */
    private String getDirectionsUrl(LatLng startPos, LatLng endPos) {
        //Builds an URL compatible with google maps. Send it to google maps and it will return the full trip with all info needed. Remember to parse.
        String str_origin = "origin=" + startPos.latitude + "," + startPos.longitude;
        String str_dest = "destination=" + endPos.latitude + "," + endPos.longitude;
        String apiKey = "&key=AIzaSyBWjR1IKXkbZKUGNhiI76AQJ1mt1YtRuZY";
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + str_origin + "&" + str_dest + "&sensor=false&mode=walking" + apiKey;
        Log.d(TAG,"URL complete. Result: " + url);
        return url;
    }

    /**
     *  Downloads the json file from google directions. adds each line to strin data.
     * @param strUrl pre-made google directions url
     * @return String containing all data from google directions
     * @throws IOException Could not download file
     */
    private String downloadUrl(String strUrl) throws IOException {
        Log.d(TAG,"Starting downloading of url...");
        String data = null;
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer stringBuff = new StringBuffer();
            String line = "";
            while ((line = buffReader.readLine()) != null) {
                stringBuff.append(line);
            }
            data = stringBuff.toString();
            buffReader.close();

        } catch (Exception e) {
            Log.d(TAG, "Download failed\n\n" + e.toString());
        } finally {
            //Closes the stream
            iStream.close();
            //Disconnects from the url
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * Paring Trip object with the GoogleDirection object that was created by @param trip.
     * @param trip trip that has googleDirection as null.
     * @return the googleDirections that belong to trip.
     */
    public static GoogleDirections findTripsGoogleDirection(Trip trip) {
        for(GoogleDirections i : GoogleDirections.googleDirectionsArrayList) {
            if(i.getTripName().equals(trip.getNavn())) {
                return i;
            }
        }
        Log.e(TAG, "Did not find a googleDirections connected to trip...");
        return null;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

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
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * Uses the data from Google directions and extracts the data we need.
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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

                    if(j == 0) {
                        setDistance(point.get("distance"));
                        setDuration(point.get("duration"));
                        setDurationRaw(Integer.parseInt(point.get("durationRaw")));
                        setDistanceRaw(Integer.parseInt(point.get("distanceRaw")));
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
            polylineOptions = lineOptions;
            Log.d(TAG, "Distance: " + distance + " Duration: " + duration);
            addGoogleDirectionsToTrip();
        }
    }
}
