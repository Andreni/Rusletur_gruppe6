package no.hiof.informatikk.gruppe6.rusletur;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.FirebaseHandler;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocalStorageTrips;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocationHandler;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.MapsActivity;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.TripTracker;
import no.hiof.informatikk.gruppe6.rusletur.Model.Fylke;
import no.hiof.informatikk.gruppe6.rusletur.Model.FylkeList;
import no.hiof.informatikk.gruppe6.rusletur.Model.Kommune;
import no.hiof.informatikk.gruppe6.rusletur.User.User;
import no.hiof.informatikk.gruppe6.rusletur.UserManagement.UserManagmentDebug;

import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainScreen_MainMenu;
import no.hiof.informatikk.gruppe6.rusletur.fragment.NewUserFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.ProfilePageFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.TripsRecyclerViewFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.SaveTripFragment;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        private FirebaseAuth mAuth;
        private FirebaseUser mUser;
        private DrawerLayout drawerLayout;
        private String TAG = "MainScreen";
        private ArrayList<LatLng> savedTripCoordinateList;
        private Geocoder geocoder;
        private String fylke;
        private String kommune;
        private Location currentLocation;
        private Context context;
        private boolean checkIfNewUser;
        public static String mainscreenUsername;
        public static String mainscreenFirstname;
        public static String mainscreenLastname;
        private DatabaseReference db;
        public final static String TAG2 = "Jesus";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_screen);

            //Broadcast Receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(arrayReceiever, new IntentFilter("SendArrayList"));

            //Check user
            mUser = FirebaseAuth.getInstance().getCurrentUser();

            //Retrieving trips from nasjonalturbase.no
            //ApiNasjonalturbase.jsonFetchTripList(this, 20);

            //Check if user is new user.
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                checkIfNewUser = extras.getBoolean("newUser");
                Log.i(MapsActivity.TAG, String.valueOf(checkIfNewUser));
            }
            else {
                Log.i(MapsActivity.TAG, "Extras contained nothing");
            }

            if(checkIfNewUser){
                Toast.makeText(this, "Welcome, new user!", Toast.LENGTH_SHORT).show();
                Log.i(MapsActivity.TAG, "MainScreen found new user");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewUserFragment()).commit();
            }
            else {
                Log.i(MapsActivity.TAG, "checkIfNewUser is false");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainScreen_MainMenu()).commit();
                Log.i(TAG2, mUser.toString());
                Log.i(TAG2, mUser.getUid());
                FirebaseHandler.getUserInfo(mUser.getUid());
            }


            //Calls location
            LocationHandler.forceUpdateOfCurrentLocation(this);
            Location currentLocation = LocationHandler.getCurrentLocation();
            if(currentLocation != null) {
                Log.d(TAG, "Lat: " + currentLocation.getLatitude() + " Lon; " + currentLocation.getLongitude());
            }

            //Set toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //Initialize drawerlayout
            drawerLayout = findViewById(R.id.drawerLayout);

            //When activity starts, open the fragment immediately. SavedInstanceState handling for rotating phone.
            if(savedInstanceState == null) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TripsRecyclerViewFragment()).commit();
            }

            //Clickhandling on navigationdrawer
            NavigationView navigationView = findViewById(R.id.navigationView);
            navigationView.setNavigationItemSelectedListener(this);

            //Make a cool spinning animation for the hamburgermeny when navigationdrawer opens
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();


            //Personalize navigation drawer.
            View headerView = navigationView.getHeaderView(0);
            TextView navHeaderUsername = (TextView)headerView.findViewById(R.id.nav_header_username);
            TextView navHeaderEmail = (TextView)headerView.findViewById(R.id.nav_header_email);
            navHeaderUsername.setText("Velkommen");
            Log.i(TAG2, "Navigation drawer ser: " + mainscreenUsername);
            navHeaderEmail.setText(mUser.getEmail());
        }

        //Static method required for getting data from FireBaseHandler
        public static void getAllUserInfo(String username, String firstname, String lastname){
            mainscreenUsername = username;
            Log.i(TAG2, username);
            mainscreenFirstname = firstname;
            mainscreenLastname = lastname;
        }






        private BroadcastReceiver arrayReceiever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                /*
                * This BroadCastReceiver receives the intent sent from TripTracker once the service is terminated AND the user
                * selected to save the trip. Unpack the intent to find our beloved arraylist, and save the arraylist in the
                * class specific arraylist savedTripCoordinateList. Once the intent has been received, array unpacked and saved
                * then send the user to SaveTripFragment, where he/she/it can fill out further info.
                 */
                ArrayList<LatLng> receivedList = intent.getParcelableArrayListExtra("LatLngArray");
                Log.i(MapsActivity.TAG, "BroadcastReceiver got the Array! Size of Array: " + String.valueOf(receivedList.size()));
                savedTripCoordinateList = receivedList;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SaveTripFragment()).commit();
            }
        };


        public void getArrayFromTripTracker(){
            ArrayList<LatLng> gimmegimme;
            gimmegimme = TripTracker.fetchArray();
            Log.i(MapsActivity.TAG, "getArrayFromTripTracker : " + String.valueOf(gimmegimme.size()));
            gimmegimme.clear();
            Log.i(MapsActivity.TAG, "getArrayFromTripTracker empty : " + String.valueOf(gimmegimme.size()));

        }


        public void handleStorageOfTrips(String tripName, String tripDescription, String tripDifficulty){

            /*
            * handleStorageOfTrips is initialized when the user has saved the trip in SaveTripFragment.
            * It starts of by getting the users current location from the LocationHandler class, and saves
            * the users municipality (getLocality) and county (getAdminArea) in Strings.
            *
            * Once that is done, we finally have all the pieces needed for adding the trip to firebase.
            * 1. ArrayList<LatLng> comes from TripTracker
            * 2. Municipality and county are extracted and saved in this method.
            * 3. Name of the trip, description and difficulty are all made in SaveTripFragment.
            *
            * Then we add it to firebase, through the static method addTrip.
            * Once added, empty the array so it won't get cluttered with future coordinates
            *
            * Todo: LocationHandler crashes if GPS doesn't exist. Fix that shit
            */


            //Geocoder for locations

            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            LocationHandler.forceUpdateOfCurrentLocation(this);
            currentLocation = LocationHandler.getCurrentLocation();
            double lat = currentLocation.getLatitude();
            double lon = currentLocation.getLongitude();
            try {
                Log.i(MapsActivity.TAG, "try-catch method called");
                List<Address> listAdresses = geocoder.getFromLocation(lat, lon, 1);
                if(listAdresses != null && listAdresses.size()>0){
                    Log.i(MapsActivity.TAG, "Listadresses contains items.");
                    kommune = listAdresses.get(0).getLocality();
                    fylke = listAdresses.get(0).getAdminArea();
                }
                else {
                    Log.i(MapsActivity.TAG, "Listadresses ga null eller va mindre enn 0");
                }
            } catch (IOException io){
                io.printStackTrace();
            }

            Toast.makeText(this, "SUCCESS: " + kommune + " ligg i " + fylke, Toast.LENGTH_SHORT).show();

            Log.i(MapsActivity.TAG, "Geocoder gives: " + kommune + " and " + fylke);

            Log.i(MapsActivity.TAG, "handleStorageofTrips mottar: param1: " + tripName + " param2: " + tripDescription + " param3: " + tripDifficulty);
            Log.i(MapsActivity.TAG, "Innhold av arrayet: " + String.valueOf(savedTripCoordinateList.size()));

            //Test the realshitz
            Trip.addTrip(tripName, savedTripCoordinateList, FirebaseAuth.getInstance().getCurrentUser(), tripDifficulty, fylke, kommune, tripDescription);

            //After add trip
            savedTripCoordinateList.clear();

            Log.i(MapsActivity.TAG, "Slettet arrayet: " + String.valueOf(savedTripCoordinateList.size()));
        }

        public void handleOfflineStorageOfTrips(String tripName, String tripDescription, String tripDifficulty){


            //Geocoder for locations.
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            LocationHandler.forceUpdateOfCurrentLocation(this);
            currentLocation = LocationHandler.getCurrentLocation();
            double lat = currentLocation.getLatitude();
            double lon = currentLocation.getLongitude();
            try {
                Log.i(MapsActivity.TAG, "try-catch method called");
                List<Address> listAdresses = geocoder.getFromLocation(lat, lon, 1);
                if(listAdresses != null && listAdresses.size()>0){
                    Log.i(MapsActivity.TAG, "Listadresses contains items.");
                    kommune = listAdresses.get(0).getLocality();
                    fylke = listAdresses.get(0).getAdminArea();
                }
                else {
                    Log.i(MapsActivity.TAG, "Listadresses ga null eller va mindre enn 0");
                }
            } catch (IOException io){
                io.printStackTrace();
            }

            String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());

           String msg = LocalStorageTrips.addAitemToStorage(this,new Trip(timestamp, tripName, null, tripDifficulty, "Rusletur", fylke, kommune, tripDescription, "Rusletur","", savedTripCoordinateList, "0"));
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();


        }



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            /*
             *Handle clicking for each item on navigation drawer. Will change this so each item clicked
             * opens a new fragment, which will display the relevant pages of the app once the
             * xml files follow the material design standard
             */
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainScreen_MainMenu()).commit();
                    break;
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilePageFragment()).commit();
                    break;
                case R.id.nav_settings:
                    Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_trip:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
                    break;
                case R.id.to_debug_page:
                    startActivity(new Intent(this, UserManagmentDebug.class));
                    break;
                case R.id.nav_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainScreen.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;

            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        }

        @Override
        public void onBackPressed() {
            /*
             * When the navigation drawer is open, clicking back will close the navigation drawer,
             * or open a dialog to ask if the user wants to log out.
             */

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Log ut")
                        .setMessage("Vil du logge ut?:")
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplication(),MainActivity.class));

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

    }