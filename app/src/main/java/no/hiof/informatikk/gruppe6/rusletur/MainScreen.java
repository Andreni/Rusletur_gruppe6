package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.LookUpRegisterNasjonalTurbase;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.FirebaseHandler;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocationHandler;

import no.hiof.informatikk.gruppe6.rusletur.fragment.RecordFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainScreen_MainMenu;
import no.hiof.informatikk.gruppe6.rusletur.fragment.ProfilePageFragment;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * This is the landing page for all users once they're logged in. This is where all
 * fragments are displayed in a container. Navigation between fragments are done in
 * navigation drawer.
 * @author Bjørnar P.
 * @author Magnus P.
 * @author Andreas N.
 */
public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


        public static FirebaseUser mUser;
        private DrawerLayout drawerLayout;
        private String TAG = "MainScreen";
        public static String mainscreenUsername;
        public static String mainscreenFirstname;
        public static String mainscreenLastname;

        private String[] neededPermissions = { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_screen);
            checkPermissions();


            //Check user
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseHandler.getUserInfo(mUser.getUid());

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
            navHeaderEmail.setText(mUser.getEmail());

            loadFindAtripRegister();
        }

        public void loadFindAtripRegister(){
            LookUpRegisterNasjonalTurbase lookUpRegisterNasjonalTurbase = new LookUpRegisterNasjonalTurbase(this);
            lookUpRegisterNasjonalTurbase.createObjectsFromRegister();
        }

    /**
     * Method for retriving user information from FirebaseHandler.
     * @param username user: username
     * @param firstname user : given name
     * @param lastname user: surname
     */
        public static void getAllUserInfo(String username, String firstname, String lastname){
            mainscreenUsername = username;
            mainscreenFirstname = firstname;
            mainscreenLastname = lastname;
        }

    /**
     * Easy permissions.
     * @return true or false depending on permission.
     */

    private boolean checkPermissions(){
        boolean isPermissionsGranted = false;

        if (EasyPermissions.hasPermissions(this,neededPermissions)){
            isPermissionsGranted = true;
        }else{
            startActivity(new Intent(this,MainActivity.class));

        }

        return isPermissionsGranted;
    }

    /**
     *
     *  Handle clicking for each item on navigation drawer. Each item clicked
     *  opens a relevant fragment.
     * @param menuItem Switches fragments.
     * @return Selected fragment.
     */

    @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainScreen_MainMenu()).commit();
                    break;
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilePageFragment()).commit();
                    break;
                case R.id.nav_trip:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecordFragment()).commit();
                    break;
                case R.id.nav_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainScreen.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.nav_findtrip:
                    Intent findTripIntent = new Intent(MainScreen.this, FindAtrip.class);
                    startActivity(findTripIntent);

            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        }

    /**
     * If drawer is open, closes it first. Then sets fragment as mainscreen_mainmenu.
     */

    @Override
        public void onBackPressed() {
            /*
             * When the navigation drawer is open, clicking back will close the navigation drawer,
             * or open a dialog to ask if the user wants to log out.
             */

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainScreen_MainMenu()).commit();

            }
        }



    }