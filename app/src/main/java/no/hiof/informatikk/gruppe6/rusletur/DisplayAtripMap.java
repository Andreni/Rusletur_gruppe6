package no.hiof.informatikk.gruppe6.rusletur;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;

public class DisplayAtripMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Trip aTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_atrip_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        aTrip = (Trip) getIntent().getParcelableExtra("object");
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(tripStartLocation, 15, 0, 0)));


        //Polylines options
        options.color(Color.GREEN);
        options.width(10);
        mMap.addPolyline(options);

    }


}
