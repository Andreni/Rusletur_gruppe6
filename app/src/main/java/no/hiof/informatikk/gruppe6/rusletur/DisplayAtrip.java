package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.MapsActivity;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.Model.LocalStorage;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Class used for displaying a trip object
 * Takes the trip object that is passed, and displays the data that is stored in it.
 * Used by {@link FindAtrip} and {@link no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocalStorageTrips}
 * @author Andreas N.
 * @author Andreas M.
 * @version 1.1
 */
public class DisplayAtrip extends AppCompatActivity implements OnMapReadyCallback {

    private Trip aTrip;
    private GoogleMap mMap;
    private String[] neededPermissions = { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE };

    Context context = this;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_atrip);

        // Using getParcelableExtra(String key) method
        aTrip = getIntent().getParcelableExtra("object");
        // Get the class the activity was called from
        String senderClass = getIntent().getStringExtra("sender");
        checkPermissions();

        //Load the map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(( this));
        if(senderClass.equals("FindAtrip")){
            setupItems(false);
        }else if (senderClass.equals("LocalStorageTrips")){
            setupItems(true);
        }


    }


    /**
     * Method used when the Trip object is viewed from {@link no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocalStorageTrips}
     * Initializes the view items, and populates them with text. Enables the delete button.
     */
    public void setupItems(Boolean editMode){
        Button btnGoTrip = findViewById(R.id.displayAtrip_goTrip_button);
        TextView displayTripName = findViewById(R.id.displayAtrip_tripName_textView);
        displayTripName.setText(aTrip.getNavn());
        TextView displayLicense = findViewById(R.id.displayAtrip_displayLisens_textView3);
        displayLicense.setText(aTrip.getLisens());
        TextView displayProvider = findViewById(R.id.displayAtrip_displayProviders_textView);
        displayProvider.setText(aTrip.getTilbyder());
        TextView displayDifficulty = findViewById(R.id.displayAtrip_gradeOfTrip_textView);
        displayDifficulty.setText(aTrip.getGradering());
        //ScrollView displayDescrption = findViewById(R.id.displayAtrip_tripDesc_ScrollView);
        TextView desc = findViewById(R.id.displayAtrip_description_TextView);
        Log.i(MainScreen.TAG3   , "dISPLAY A TRIP ANTALL :"  + String.valueOf(aTrip.getCoordinates().size()));
        for(int i = 0; i < aTrip.getCoordinates().size(); i++){
            Log.i(MainScreen.TAG3, "Coords i display a trip : " + String.valueOf(aTrip.getCoordinates().get(i).latitude + " " +  String.valueOf(aTrip.getCoordinates().get(i).longitude)));
        }

        desc.setText(Html.fromHtml(aTrip.getBeskrivelse()));
        //TextView displayTag = findViewById(R.id.displayAtrip_tagOfTrip_textView2);
        //displayTag.setText(aTrip.getTag());
        TextView displayLengthOfTripTime = findViewById(R.id.displayAtrip_trip_TimeLength_textView);
        displayLengthOfTripTime.setText(aTrip.getTidsbruk());
        TextView displayUrl = findViewById(R.id.displayAtrip_urlForTrip_textView);
        displayUrl.setText(aTrip.getUrl());
        if(editMode){
            Button deleteMode = findViewById(R.id.displayAtrip_DeleteAtrip_button);
            deleteMode.setVisibility(View.VISIBLE);
            deleteMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Lagring")
                            .setMessage("Vil du slette turen?")
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LocalStorage localStorage = LocalStorage.getInstance(context);
                                    localStorage.deleteAtrip(aTrip.getId());
                                    startActivity(new Intent(context,MainScreen.class));
                                }
                            })
                            .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                }
            });

            btnGoTrip.setVisibility(View.INVISIBLE);
        }




    }

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
     * Google Maps fragment for drawing a preview of the selected trip
     * @param map takes the map variable when is loaded.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        PolylineOptions options = new PolylineOptions();
        //Start position
        LatLng tripStartLocation = null;
        tripStartLocation = new LatLng(aTrip.getCoordinates().get(0).latitude, aTrip.getCoordinates().get(0).longitude);

        //Registers the first polylines at the start of the trip, this will be used in the addMarker.
        for (int i = 0; i< aTrip.getCoordinates().size();i++) {
            options.add(new LatLng(aTrip.getCoordinates().get(i).latitude,aTrip.getCoordinates().get(i).longitude));

        }

        //Place a marker on the map
        mMap.addMarker(new MarkerOptions().position(tripStartLocation).title(aTrip.getNavn()));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(tripStartLocation, 11, 0, 0)));


        //Polylines options
        options.color(Color.GREEN);
        options.width(10);
        mMap.addPolyline(options);
    }


    public void goBack(View view){
        super.onBackPressed();
    }



    public void goToMaps(View view){
        if (!UserUtility.checkIfUserHasGPSEnabled(this)){
            Toast.makeText(this, "Du må skru på GPS", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, MapsActivity.class);

            intent.putExtra("object", aTrip);
            startActivity(intent);
        }


    }

}
