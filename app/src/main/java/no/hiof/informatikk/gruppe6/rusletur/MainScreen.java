package no.hiof.informatikk.gruppe6.rusletur;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocationHandler;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.MapsActivity;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;
import no.hiof.informatikk.gruppe6.rusletur.UserManagement.UserManagmentDebug;

import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;
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

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_screen);

            //Broadcast Receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(arrayReceiever, new IntentFilter("SendArrayList"));

            //Retrieving trips from nasjonalturbase.no
            //ApiNasjonalturbase.jsonFetchTripList(this, 20);

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TripsRecyclerViewFragment()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TripsRecyclerViewFragment()).commit();
            }

            //Clickhandling on navigationdrawer
            NavigationView navigationView = findViewById(R.id.navigationView);
            navigationView.setNavigationItemSelectedListener(this);

            //Make a cool spinning animation for the hamburgermeny when navigationdrawer opens
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();



        }

        public void showcaseMethod(){
            /*
            * This is how you call from activity to methods in fragments.
            * 
             */
            FragmentManager fragmentManager = getSupportFragmentManager();

            MainMenuFragment menuFrag = (MainMenuFragment)fragmentManager.findFragmentById(R.id.fragment_mainscreen);
            // menuFrag.showcaseMethodTwo();

            Log.d("FragmentDemo", "Dance for me baby");
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



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            /*
             *Handle clicking for each item on navigation drawer. Will change this so each item clicked
             * opens a new fragment, which will display the relevant pages of the app once the
             * xml files follow the material design standard
             */
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
                    break;
                case R.id.nav_profile:
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_settings:
                    Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_trip:
                    startActivity(new Intent(this,Trips.class));
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
             * When the navigation drawer is open, clicking back will close the navigation drawer before
             * going to the previous view. Quality of life change, OH YAAAAH
             */

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    }