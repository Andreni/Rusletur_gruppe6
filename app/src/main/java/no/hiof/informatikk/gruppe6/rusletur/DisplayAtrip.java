package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

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

        setupItems();

    }


    public void setupItems(){
        TextView displayTripName = findViewById(R.id.displayAtrip_tripName_textView);
        displayTripName.setText(aTrip.getNavn());
        TextView displayLicense = findViewById(R.id.displayAtrip_displayLisens_textView3);
        displayLicense.setText(aTrip.getLisens());
        TextView displayProvider = findViewById(R.id.displayAtrip_displayProviders_textView);
        displayProvider.setText(aTrip.getTilbyder());
        TextView displayDifficulty = findViewById(R.id.displayAtrip_gradeOfTrip_textView);
        displayDifficulty.setText(aTrip.getGradering());
        TextView displayDescrption = findViewById(R.id.displayAtrip_tripDesc_textView2);
        displayDescrption.setText(Html.fromHtml(aTrip.getBeskrivelse()));
        TextView displayTag = findViewById(R.id.displayAtrip_tagOfTrip_textView2);
        displayTag.setText(aTrip.getTag());
        TextView displayLengthOfTripTime = findViewById(R.id.displayAtrip_trip_TimeLength_textView);
        displayLengthOfTripTime.setText(aTrip.getTidsbruk());
        TextView displayUrl = findViewById(R.id.displayAtrip_urlForTrip_textView);
        displayUrl.setText(aTrip.getUrl());

        ImageView displayTripPic = findViewById(R.id.displayAtrip_TripImage_imageView);


    }
}
