package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.ApiNasjonalturbase;
import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.ReadFromFileFromUrl;
import no.hiof.informatikk.gruppe6.rusletur.UserManagement.UserManagmentDebug;

import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;
import no.hiof.informatikk.gruppe6.rusletur.fragment.TripsRecyclerViewFragment;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        private FirebaseAuth mAuth;
        private FirebaseUser mUser;
        private DrawerLayout drawerLayout;
        private final String TAG = "MainScreen";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_screen);

            //Retrieving trips from nasjonalturbase.no
            //ApiNasjonalturbase.jsonFetchTripList(this, 20);

            ReadFromFileFromUrl.getFile(this);

            //Set toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //Initialize drawerlayout
            drawerLayout = findViewById(R.id.drawerLayout);

            //When activity starts, open the fragment immediately. SavedInstanceState handling for rotating phone.
            if(savedInstanceState == null) {
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TripsRecyclerViewFragment()).commit();
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