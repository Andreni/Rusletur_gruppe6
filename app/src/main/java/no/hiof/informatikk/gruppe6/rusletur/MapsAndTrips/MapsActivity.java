package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Method onMapReady() started.");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //Display trip
        String url = getIntent().getStringExtra("url");
        Log.d(TAG, url);
        parseGpx(url);
    }

    public void retrivedTrip(){
        //Logcat tag GPXLOG

        //Read through coordinates

        //Make points on map


        //Draw lines between points

        //Display name of map.

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
                }
                else {
                    if (mParser != null) {

                        Track track = gpx.getTracks().get(0);
                        tripTitleName = track.getTrackName();
                        List<TrackSegment> segments = track.getTrackSegments();
                        for (int j = 0; j < segments.size(); j++) {
                            TrackSegment segment = segments.get(j);
                            //Log.d(GPXLOG, "  segment " + j + ":");

                            //Registers the first polylines at the start of the trip, this will be used in the addMarker.
                            tripStartLocation = new LatLng(segment.getTrackPoints().get(0).getLatitude(), segment.getTrackPoints().get(0).getLongitude());
                            for (TrackPoint trackPoint : segment.getTrackPoints()) {
                                //Log.d(GPXLOG, "    point: lat " + trackPoint.getLatitude() + ", lon " + trackPoint.getLongitude());
                                //Insert all latLng objects into "options" variable
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
