package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;

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

import javax.net.ssl.HttpsURLConnection;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;
import io.ticofab.androidgpxparser.parser.task.GpxFetchedAndParsed;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.User.User;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final String GPXLOG = "GPXLOG";
    private Trip aTrip;
    public static final String TAG = "MapsActivity";
    private LatLng currentPosition = new LatLng(0,0);
    protected static LatLng tripStartLocation = null;
    private ArrayList<LatLng> markerpoints;
    //Checking if current trip is a new trip.
    private boolean AddTrip = false;

    
    GPXParser mParser = new GPXParser(); // consider injection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.d(TAG,"MapsActivity has been initiated");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //SJEKK OM GPS STÅR PÅ
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "plz turn on", Toast.LENGTH_SHORT).show();
        }

        //SJEKK OM ACCESS FINE LOCATION STÅR PÅ, HVIS PÅ KJØR STARTTRACKING
        int trackingGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (trackingGranted == PackageManager.PERMISSION_GRANTED){
            //startTracking();
        }
        else{
            Toast.makeText(this, "plz gimme access", Toast.LENGTH_SHORT).show();
        }
        //Log.d(TAG,"BEFORE showPathToTripFromCurrentPosition, startLocation: " +  tripStartLocation.latitude);

    }


    public void showPathToTripFromCurrentPosition() {

        Log.d(TAG,"********************************************************************START********************************************************************");

        if(LocationHandler.CURRENT_LOCATION_IS_SET) {
            Log.d(TAG," [0] Current location is set");
            currentPosition = new LatLng(LocationHandler.getCurrentLocation().getLatitude(), LocationHandler.getCurrentLocation().getLongitude());
            try {
                String url = getDirectionsUrl(currentPosition, tripStartLocation);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            } catch (NullPointerException e) {
                Log.e(TAG, "A LatLng was null \n" + e.toString());
            }

        } else {
            Log.e(TAG," [1] Current location is not set. Is GPS turned on?");
        }
        Log.d(TAG,"********************************************************************END********************************************************************");

    }

    private String getDirectionsUrl(LatLng startPos, LatLng endPos) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Method onMapReady() started.");
        mMap = googleMap;

        //Display trip from url
        if(getIntent().hasExtra("url")){
            String url = getIntent().getStringExtra("url");
            Log.d(TAG, url);
            parseGpx(url);
        }
        if(getIntent().hasExtra("object")){
            aTrip = getIntent().getParcelableExtra("object");
            parseObject(aTrip);
        }

    }




    public void parseGpx(String urlToGpx){
        Log.i(TAG, "Method urlToGpx() started.");
        //Standard GPX parser....
        final GPXParser mParser = new GPXParser();
        final PolylineOptions options = new PolylineOptions();
        final ArrayList<LatLng> listOfLatLng = null;


        mParser.parse(urlToGpx, new GpxFetchedAndParsed() {
            @Override
            public void onGpxFetchedAndParsed(Gpx gpx) {
                Log.i(TAG, "Method onGpxGetchedAndParsed started.");
                String tripTitleName = null;
                if (gpx == null) {
                    // error parsing track
                    Log.e(TAG, "onGpxFetchedAndParsed: Error parsing GPX");
                }
                else {
                    if (mParser != null) {
                        Log.d(TAG, "mParser is not null, parsing instantiated ");
                        Track track = gpx.getTracks().get(0);
                        tripTitleName = track.getTrackName();
                        ArrayList<LatLng> saveList = new ArrayList<>();
                        List<TrackSegment> segments = track.getTrackSegments();
                        for (int j = 0; j < segments.size(); j++) {
                            TrackSegment segment = segments.get(j);
                            //Registers the first polylines at the start of the trip, this will be used in the addMarker.
                            if(j == 0) {
                                setTripStartLocation( new LatLng(segments.get(j).getTrackPoints().get(0).getLatitude(), segments.get(j).getTrackPoints().get(0).getLongitude()));
                            }
                            for (TrackPoint trackPoint : segment.getTrackPoints()) {
                                options.add(new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude()));
                                if(!FirebaseHandler.isTripInFirebaseDatabase(tripTitleName)) {
                                    saveList.add(new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude()));
                                }

                            }
                        }
                        if(!FirebaseHandler.isTripInFirebaseDatabase(tripTitleName)) {
                            Trip.addTrip(tripTitleName, saveList, FirebaseAuth.getInstance().getCurrentUser(), "Normal", "", "", "Dette er en test tur");
                            Log.i(TAG,"Trip created!");
                        }
                        //Place a marker on the map
                        mMap.addMarker(new MarkerOptions().position(tripStartLocation).title(tripTitleName));
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(tripStartLocation, 15, 0, 0)));
                        Log.d(TAG, "Start location is set to: " + tripStartLocation.toString());
                        Log.d(TAG,"Lat: " + tripStartLocation.latitude + " Lon: " + tripStartLocation.longitude);

                        //Polylines options
                        options.color(Color.GREEN);
                        options.width(10);
                        mMap.addPolyline(options);

                    } else {
                        Log.e(TAG, "Error parsing gpx track!");
                    }
                }
            }

        });
        try {
            Log.d(TAG, "LatLng: " + tripStartLocation.toString());
        } catch (NullPointerException e) {
            Log.e(TAG,"tripStartLocation is NULL \n" + e.toString());
        }
        showPathToTripFromCurrentPosition();
    }

    private void parseObject(Trip trip){
        PolylineOptions options = new PolylineOptions();
        //Start position
        LatLng tripStartPos = null;
        tripStartPos = new LatLng(trip.getCoordinates().get(0).longitude, trip.getCoordinates().get(0).latitude);

        for(int i = 0; i < trip.getCoordinates().size(); i++){
            options.add(new LatLng(trip.getCoordinates().get(i).longitude, trip.getCoordinates().get(i).latitude));
        }

        mMap.addMarker(new MarkerOptions().position(tripStartPos).title(trip.getNavn()));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(tripStartPos, 11,0,0)));

        options.color(Color.BLUE);
        options.width(10);
        mMap.addPolyline(options);

    }


    public void setTripStartLocation(LatLng pos) {
        tripStartLocation = pos;
    }

    //To download the JSON file
    private String downloadUrl(String strUrl) throws IOException {
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
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
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
