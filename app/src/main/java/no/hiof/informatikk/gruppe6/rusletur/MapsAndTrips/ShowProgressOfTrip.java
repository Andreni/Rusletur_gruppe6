package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.R;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * This class uses the coordinates recorded by the ongoing background service and uses it to draw polylines in
 * a google map fragment.
 * @author Bjørnar P
 * @author Andreas N.
 *
 */
public class ShowProgressOfTrip extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private ArrayList<LatLng> receievedList;
    private LatLng startLocation;
    public  final String TAG = "ShowProgressOfTrip";
    private String[] neededPermissions = { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();

        setContentView(R.layout.activity_progressmap);

        receievedList = TripTracker.fetchArray();


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.progress_map_gmap);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        PolylineOptions poly = new PolylineOptions();
        startLocation = new LatLng(receievedList.get(0).latitude, receievedList.get(0).longitude);

        //Draw polylines.
        for(int i=0; i < receievedList.size();i++){
            poly.add(new LatLng(receievedList.get(i).latitude, receievedList.get(i).longitude));
        }

        //Fløtt kamera
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(startLocation, 12, 0, 0)));

        //Fargefaen
        poly.color(Color.GREEN);
        poly.width(10);
        gMap.addPolyline(poly);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecordFragment()).commit();


    }
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
}
