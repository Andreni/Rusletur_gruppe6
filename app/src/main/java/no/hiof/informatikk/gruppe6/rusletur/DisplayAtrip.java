package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;

/**
 * Class used for displaying a trip object
 */
public class DisplayAtrip extends AppCompatActivity {

    private Trip aTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_atrip);

        // Using getParcelableExtra(String key) method
        aTrip = (Trip) getIntent().getParcelableExtra("object");

    }
}
