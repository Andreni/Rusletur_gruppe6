package no.hiof.informatikk.gruppe6.rusletur;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.FirebaseHandler;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocationHandler;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.MapsActivity;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.TripTracker;
import no.hiof.informatikk.gruppe6.rusletur.Model.LocalStorage;
import no.hiof.informatikk.gruppe6.rusletur.UserManagement.UserManagmentDebug;

import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainScreen_MainMenu;
import no.hiof.informatikk.gruppe6.rusletur.fragment.NewUserFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.ProfilePageFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.SaveTripFragment;

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

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_screen);

            //Check user
            mUser = FirebaseAuth.getInstance().getCurrentUser();

            //Retrieving trips from nasjonalturbase.no
            //ApiNasjonalturbase.jsonFetchTripList(this, 20);


            FirebaseHandler.downloadAllCustomTrips();


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