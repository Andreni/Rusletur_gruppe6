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
import android.view.View;
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
    public static final String TAG = "MapsActivity";
    private LatLng currentPosition = new LatLng(0,0);
    protected static LatLng tripStartLocation = null;
    private ArrayList<LatLng> markerpoints;
    //Checking if current trip is a new trip.
    private GoogleDirections test = null;
    private boolean AddTrip = false;
    private int count = 0;
    String tripTitleName = null;

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

    private void startTracking(){
        //Context.startService(new Intent(this, TripTracker.class))
        startService(new Intent(this, TripTracker.class));
        Log.i(TAG, "startTracking is called");
    }
    public void showPath(View view) {
        if(count == 0) {
            test = new GoogleDirections(tripStartLocation, tripTitleName);
            count++;
        }
        if(test.getStatus() == test.STATUS_READY) {
            mMap.addPolyline(test.getPolylineOptions());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Method onMapReady() started.");
        mMap = googleMap;

        //Display trip
        String url = getIntent().getStringExtra("url");
        Log.d(TAG, url);
        parseGpx(url);
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
        //new LatLng(59.118741, 11.3865243)

    }
    public void setTripStartLocation(LatLng pos) {
        tripStartLocation = pos;
    }

}
