package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.UserUtility;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Displays the trip that was passed
 * @author Andreas M.
 * @author Magnus P.
 * @author Andreas N.
 * @author Bjørnar P.
 * @version 1.2
 */
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
    private Location current;
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
    private Button btnStart;
    private int amountOfCurrentGotten = 0;
    private Button showPath;

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

        showPath = findViewById(R.id.showPath);
        aTrip = getIntent().getParcelableExtra("object");
        aTrip.setGoogleDirections(GoogleDirections.findTripsGoogleDirection(aTrip));

        try {
           if(aTrip.getGoogleDirections().getPolylineOptions().getPoints().size() > 1) {
               View showPathButton = findViewById(R.id.showPath);
               showPathButton.setVisibility(View.VISIBLE);
           }
        } catch (NullPointerException e) {
            Log.e(TAG,"No google directions found...");
        }

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

        btnStart = findViewById(R.id.startTrip);
        btnStart.setVisibility(View.INVISIBLE);

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

    /**
     * Method for checking od the user has granted permissions
     * @return True or False. Based on ig the user has granted or not
     */
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

    private boolean firstTimeShowingPath = false;
    public void showPath(View view) {
        if(!firstTimeShowingPath && current != null) {
            Log.d(TAG, "showPath: CLICKED");
            aTrip.setGoogleDirections(GoogleDirections.findTripsGoogleDirection(aTrip));
            mMap.addPolyline(aTrip.getGoogleDirections().getPolylineOptions());
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(aTrip.getGoogleDirections().getPolylineOptions().getPoints().get(0), 15, 0, 0)));
            firstTimeShowingPath = true;
        }
    }


    /**
     * Method runs automatically when the maps ahs finished loading.
     * This makes sure that the map is complete before we do anything.
     * @param googleMap The instance of GoogleMaps that needs to be loaded
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Method onMapReady() started.");
        mMap = googleMap;
        getCurrentLocation();
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

    /**
     * Method for activating the assistent.
     * Checks wvery 30 seconds if the user is more than 200 meters away from the trip course.
     * @param view The button as a View
     */
    public void startCurrentTrip(View view){
        view.setVisibility(View.INVISIBLE);
        checkLocation();
    }

    /**
     * Parse the trip if the activity is called with an url
     * Feature currently not implemented for user
     * @param urlToGpx The url where the gpx file is placed
     */
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

    /**
     * Is called on if the activity is called on from with a {@link Trip} object
     * @param trip The trip that the user has selected and passed onto this activity
     */
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


    /**
     * Sets the start position of the {@link Trip} with latitude and longitude coordinates
     * @param pos The position of the start of the {@link Trip}
     */
    public void setTripStartLocation(LatLng pos) {
        tripStartLocation = pos;
    }

    /**
     * Gets the current location of the user.
     * Is used before the calculation of the closest marker to the user in {@link #calcClosestMarker()}
     */
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
                            if(amountOfCurrentGotten == 0){
                                btnStart.setVisibility(View.VISIBLE);
                                amountOfCurrentGotten = 1;
                            }

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

    /**
     * Method that creates the notification if the user is mroe than 200 meters away from the {@link Trip}
     */
    private void checkLocation(){
        if(!STOP && current != null) {
            Log.d(TAG, "checkLocation: Started");
            Handler currentLocHandler = new Handler();
            Log.d(TAG, "checkLocation: New handler");
            currentLocHandler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (calcClosestMarker() > differenceBeforePing && !STOP) {
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

                    getCurrentLocation();
                    checkLocation();

                }
            }, 30000);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkLocation();
                }
            }, 2000);
        }

    }

    /**
     * Calculates the closest marker to the user.
     * Loops through the polyline markers and saves the closest point and the length between that point and the user.
     * @return A double that is the distance between the closetst point and the user
     */
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

    /**
     * Method that runs when the activity dies
     */
    @Override
    public void onDestroy(){
        STOP = true;
        super.onDestroy();
    }

    /**
     * Overrides the super onBackPressed method.
     * Makes a AlertDialog to make sure that the user really wants to quit the {@link Trip}
     */
    @Override
    public void onBackPressed(){
        STOP = true;
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Avslutt")
                .setMessage("Vil du avslutte turen?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        startActivity(new Intent(getApplication(),MainScreen.class));

                    }
                })
                .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }



}
