package no.hiof.informatikk.gruppe6.rusletur;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.FirebaseHandler;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocationHandler;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.ShowProgressOfTrip;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.TripTracker;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainScreen_MainMenu;
import no.hiof.informatikk.gruppe6.rusletur.fragment.ProfilePageFragment;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.TripTracker.LocalTrackBinder;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        private FirebaseAuth mAuth;
        public static FirebaseUser mUser;
        private DrawerLayout drawerLayout;
        private String TAG = "MainScreen";
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


        private TripTracker tripTracker;
        private boolean isBound = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_screen);

            //Check user
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseHandler.getUserInfo(mUser.getUid());
            //Retrieving trips from nasjonalturbase.no
            //ApiNasjonalturbase.jsonFetchTripList(this, 20);

            //Bindtest
            Intent bindToTrackerIntent = new Intent(this, TripTracker.class);
            bindService(bindToTrackerIntent,serviceConnection, Context.BIND_AUTO_CREATE );


            FirebaseHandler.downloadAllCustomTrips();

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

            //Clickhandling on navigationdrawer
            NavigationView navigationView = findViewById(R.id.navigationView);
            navigationView.setNavigationItemSelectedListener(this);

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainScreen_MainMenu()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
            }

            //Make a cool spinning animation for the hamburgermeny when navigationdrawer opens
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
 

            //Personalize navigation drawer.
            View headerView = navigationView.getHeaderView(0);
            TextView navHeaderUsername = (TextView) headerView.findViewById(R.id.nav_header_username);
            TextView navHeaderEmail = (TextView) headerView.findViewById(R.id.nav_header_email);
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

        public void startShowProgressOfTrip(){
            ArrayList<LatLng> tempArray = new ArrayList<>();
            tempArray = tripTracker.getArray();
            Intent intent = new Intent(MainScreen.this, ShowProgressOfTrip.class);
            intent.putExtra("tempLocationArray", tempArray);
            startActivity(intent);
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
                    tripTracker.toasty();
                    break;
                case R.id.nav_trip:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
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
                /*new AlertDialog.Builder(this)
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
                        .show();*/
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainScreen_MainMenu()).commit();

            }
        }

        private ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocalTrackBinder binder = (LocalTrackBinder) service;
                tripTracker = binder.getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };


    }