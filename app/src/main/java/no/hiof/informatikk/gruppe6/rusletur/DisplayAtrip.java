package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.MapsActivity;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;

/**
 * Class used for displaying a trip object
 * Takes the trip object that is passed, and displays the data that is stored in it.
 */
public class DisplayAtrip extends AppCompatActivity implements OnMapReadyCallback {

    private Trip aTrip;
    private GoogleMap mMap;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_atrip);

        // Using getParcelableExtra(String key) method
        aTrip = (Trip) getIntent().getParcelableExtra("object");

        //Load the map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(( this));
        setupItems();

    }

    //TODO Make method that passes the object, if the user chooses to take the trip

    /**
     * Initializes the view items, and populates them with text.
     */
    public void setupItems(){
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

        desc.setText(Html.fromHtml(aTrip.getBeskrivelse()));
        //TextView displayTag = findViewById(R.id.displayAtrip_tagOfTrip_textView2);
        //displayTag.setText(aTrip.getTag());
        TextView displayLengthOfTripTime = findViewById(R.id.displayAtrip_trip_TimeLength_textView);
        displayLengthOfTripTime.setText(aTrip.getTidsbruk());
        TextView displayUrl = findViewById(R.id.displayAtrip_urlForTrip_textView);
        displayUrl.setText(aTrip.getUrl());



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
        tripStartLocation = new LatLng(aTrip.getCoordinates().get(0).longitude, aTrip.getCoordinates().get(0).latitude);

        //Registers the first polylines at the start of the trip, this will be used in the addMarker.
        for (int i = 0; i< aTrip.getCoordinates().size();i++) {
            options.add(new LatLng(aTrip.getCoordinates().get(i).longitude,aTrip.getCoordinates().get(i).latitude));

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
        Intent intent = new Intent(this, MapsActivity.class);

        intent.putExtra("object", aTrip);
        startActivity(intent);
    }

}
