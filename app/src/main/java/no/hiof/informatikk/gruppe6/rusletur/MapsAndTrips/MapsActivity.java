package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;
import io.ticofab.androidgpxparser.parser.task.GpxFetchedAndParsed;
import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.UserUtility;
import pub.devrel.easypermissions.EasyPermissions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String GPXLOG = "GPXLOG";
    private Trip aTrip;
    public static final String TAG = "MapsActivity";
    private LatLng currentPosition = new LatLng(0,0);
    protected static LatLng tripStartLocation = null;
    private ArrayList<LatLng> markerpoints;
    //Checking if current trip is a new trip.
    private GoogleDirections test = null;
    private boolean AddTrip = false;
    private int count = 0;
    private String tripTitleName = null;
    //Current location
    private Location current = null;
    //Polyline options
    private PolylineOptions options;
    private double differenceBeforePing = 0.0045;
    private String CHANNEL_1_ID = "default";
    private boolean STOP = false;

    private LatLng startLocation;
    private String tripName;
    private String[] neededPermissions = { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE };

    //Used for drawing up stuff
    private ArrayList<LatLng> receivedTripInProgress = new ArrayList<>();


    GPXParser mParser = new GPXParser(); // consider injection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.d(TAG,"MapsActivity has been initiated");
        checkPermissions();
        if (!UserUtility.checkIfUserHasGPSEnabled(this)){
            STOP = true;
            super.onDestroy();
        }

        aTrip = getIntent().getParcelableExtra("object");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Default", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Alert for avstand fra tur");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnStart = findViewById(R.id.startTrip);

        //SJEKK OM GPS STÅR PÅ
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Du trenger GPS for denne funksjonen", Toast.LENGTH_SHORT).show();
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

        startLocation = new LatLng(aTrip.getCoordinates().get(0).longitude, aTrip.getCoordinates().get(0).latitude);
        tripName = aTrip.getNavn();


    }

    private boolean checkPermissions(){
        boolean isPermissionsGranted = false;

        if (EasyPermissions.hasPermissions(this,neededPermissions)){
            isPermissionsGranted = true;
        }else{

            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        return isPermissionsGranted;
    }

    private void startTracking(){
        //Context.startService(new Intent(this, TripTracker.class))
        startService(new Intent(this, TripTracker.class));
        Log.i(TAG, "startTracking is called");
    }
    public void showPath(View view) {
        if(count == 0) {
            test = new GoogleDirections(startLocation, tripName);
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
        checkLocation();
        options = new PolylineOptions();

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

    public void startCurrentTrip(View view){
        view.setVisibility(View.INVISIBLE);
        checkLocation();

    }



    public void parseGpx(String urlToGpx){
        Log.i(TAG, "Method urlToGpx() started.");
        //Standard GPX parser....
        final GPXParser mParser = new GPXParser();
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
                            Trip.addTrip(tripTitleName, saveList, FirebaseAuth.getInstance().getCurrentUser(), "Normal", "",
                                    "", "", "Bratt",
                                    "", "20 min", "",FirebaseAuth.getInstance().getCurrentUser().getEmail());
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

    }

    private void parseObject(Trip trip){

        //Start position
        LatLng tripStartPos = null;
        tripStartPos = new LatLng(trip.getCoordinates().get(0).latitude, trip.getCoordinates().get(0).longitude);

        for(int i = 0; i < trip.getCoordinates().size(); i++){
            options.add(new LatLng(trip.getCoordinates().get(i).latitude, trip.getCoordinates().get(i).longitude));
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

    private void getCurrentLocation(){
        if(!STOP) {
            Log.d(TAG, "getCurrentLocation: checkLocation Before fusedlocationprovider");
            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            try {
                Task location = mFusedLocationProviderClient.getLastLocation();
                Log.d(TAG, "getCurrentLocation: checkLocation: after init task");
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: checkLocation: Location found");
                            //Adds blue dot at your location
                            mMap.setMyLocationEnabled(true);
                            current = (Location) task.getResult();


                        } else {
                            Log.d(TAG, "onComplete: checkLocation: Failed to find location");
                        }
                    }
                });
                Log.d(TAG, "getCurrentLocation: checkLocation: Task complete");
            } catch (SecurityException e) {
                Log.d(TAG, "getCurrentLocation: checkLocation Permission not granted");
                e.printStackTrace();
            }
        }
    }

    private void checkLocation(){
        if(!STOP) {
            Log.d(TAG, "checkLocation: Started");
            Handler currentLocHandler = new Handler();
            Log.d(TAG, "checkLocation: New handler");
            currentLocHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    getCurrentLocation();

                    if (calcClosestMarker() > differenceBeforePing) {
                        Log.d(TAG, "onComplete: checkLocation: ALERT");
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MapsActivity.this, CHANNEL_1_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Warning!")
                                .setContentText("Du går bort fra turen")
                                .setAutoCancel(true)
                                .setPriority(NotificationManager.IMPORTANCE_HIGH);
                        mNotificationManager.notify(0, mBuilder.build());
                    }
                    
                    checkLocation();

                }
            }, 30000);
        }

    }
    //Calculate the closetst point on the polyline
    private double calcClosestMarker(){

        Log.d(TAG, "calcClosestMarker: checkLocation: started calculation");
        //Gets current coordinates
        double closestValue = 0;

        for(int i = 0; i < options.getPoints().size(); i++){
            Log.d(TAG, "calcClosestMarker: checkLocation: inside for");
            LatLng currentPolylinePoint = options.getPoints().get(i);

            //Think of the polyline points and your position as points in a 2 dim graph
            // (x1, y1) is the coordinates for the polyline marker
            // (x2, y2) is the coordinates for your position
            double x1 = currentPolylinePoint.latitude;
            double y1 = currentPolylinePoint.longitude;
            double x2 = current.getLatitude();
            double y2 = current.getLongitude();

            //Gets the difference in x-axis and y-axis
            double differenceInX = Math.pow(Math.abs(x2-x1) , 2);
            double differenceInY = Math.pow(Math.abs(y2-y1) , 2);

            //Gets the length between (x1,y1) and (x2,y2)
            double hypotenuse = Math.sqrt((differenceInX+differenceInY));

            Log.d(TAG, "calcClosestMarker: checkLocation: " + hypotenuse);

            //Init the first polyline as the closest
            if(i == 0){
                closestValue = hypotenuse;
            }
            //If the new difference is lower than the old
            if(hypotenuse < closestValue){
                closestValue = hypotenuse;
            }
        }

        return closestValue;
    }

    @Override
    public void onDestroy(){
        STOP = true;
        super.onDestroy();
    }

}
