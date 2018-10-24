package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;
import io.ticofab.androidgpxparser.parser.task.GpxFetchedAndParsed;
import no.hiof.informatikk.gruppe6.rusletur.Manifest;
import no.hiof.informatikk.gruppe6.rusletur.R;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final String GPXLOG = "GPXLOG";
    static final String TAG = "MapsActivity";


    GPXParser mParser = new GPXParser(); // consider injection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


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
            startTracking();
        }
        else{
            Toast.makeText(this, "plz gimme access", Toast.LENGTH_SHORT).show();
        }

    }


    private void startTracking(){
        startService(new Intent(this, TripTracker.class));
        Log.i(TAG, "startTracking is called");
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
                LatLng tripStartLocation = null;
                String tripTitleName = null;
                if (gpx == null) {
                    // error parsing track
                    Log.d(TAG, "onGpxFetchedAndParsed: Error parsing GPX");
                }
                else {
                    if (mParser != null) {

                        Track track = gpx.getTracks().get(0);
                        tripTitleName = track.getTrackName();
                        List<TrackSegment> segments = track.getTrackSegments();
                        for (int j = 0; j < segments.size(); j++) {
                            TrackSegment segment = segments.get(j);

                            //Registers the first polylines at the start of the trip, this will be used in the addMarker.
                            tripStartLocation = new LatLng(segment.getTrackPoints().get(0).getLatitude(), segment.getTrackPoints().get(0).getLongitude());
                            for (TrackPoint trackPoint : segment.getTrackPoints()) {
                                options.add(new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude()));

                            }
                        }
                        //Place a marker on the map
                        mMap.addMarker(new MarkerOptions().position(tripStartLocation).title(tripTitleName));
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(tripStartLocation, 15, 0, 0)));


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
    }
}
