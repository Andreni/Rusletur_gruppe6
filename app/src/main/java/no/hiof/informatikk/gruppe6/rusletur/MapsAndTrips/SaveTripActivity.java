package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.LookUpFylkerOgKommunerGitHub;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.Model.LocalStorage;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;

import static no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment.TAG;

public class SaveTripActivity extends AppCompatActivity {

    //Views
    private String selectedDifficulty;
    private EditText nameInput;
    private EditText descInput;
    private RadioGroup difficultyRadioGroup;
    private Button saveTripButton;
    private boolean checked;
    private String nameinput;
    private String description;
    private String municipality;
    private String county;
    private Spinner municipalitySpinner;
    private Spinner countySpinner;

    private ArrayList<LatLng> savedCoordinates = new ArrayList<>();

    //Setup Spinner
    //private HttpURLConnection conn = null;
    //private ArrayList<ArrayList<String>> fylkerOgKommuner = new ArrayList<>();
    public static boolean finished = false;
    private ArrayList<String> tmpFylker = new ArrayList<>();
    private ArrayList<String> tmpKommuner = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_savetrip);

        //Setup views
        //countySpinner = findViewById(R.id.savetrip_selectCounty);
        municipalitySpinner = findViewById(R.id.savetrip_selectMunicipality);


        savedCoordinates = getIntent().getParcelableArrayListExtra("coordsArray");
        Log.i(TAG, "SaveTripActivity mottok Array! : " + String.valueOf(savedCoordinates.size()));

        //String with custom time spent on trip. Can be in any format, Day:Hour:Minute:Seconds
        String test = getIntent().getStringExtra("timeSpent");
        Log.i(TAG, "SaveTripActivity mottok String au! : " + test);

        difficultyRadioGroup = findViewById(R.id.savetrip_radioGroup);
        difficultyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.savetrip_easySelected:
                        selectedDifficulty = "Lett";
                        break;
                    case R.id.savetrip_mediumSelected:
                        selectedDifficulty = "Middels";
                        break;
                    case R.id.savetrip_hardSelected:
                        selectedDifficulty = "Vanskelig";
                        break;
                }
            }
        });

        saveTripButton = findViewById(R.id.savetrip_saveTripButton);
        saveTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInput != null && descInput != null && selectedDifficulty != null) {
                    /*
                     * Check if input is not null lol.
                     * If everything checks out, send input from name, description and radiogroup to
                     * handleStorageOfTrips in MainScreen, and switch from this fragment back to the
                     * MainMenuFragment. It happens lightning quick because Fragments are fucking rad bro.
                     */
                    nameinput = nameInput.getText().toString();
                    description = descInput.getText().toString();
                    //municipality = municipalityInput.getText().toString();
                    //county = countyInput.getText().toString();

                    new AlertDialog.Builder(SaveTripActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Lagring")
                            .setMessage("Vil du dele turen?")
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    handleStorageOfTrips(nameinput, description, selectedDifficulty, municipality, county);
                                    //REDIRECT

                                }
                            })
                            .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    handleOfflineStorageOfTrips(nameinput, description, selectedDifficulty, municipality, county);
                                    //REDIRECT
                                }
                            })
                            .show();

                } else {
                    Toast.makeText(SaveTripActivity.this, "Er alle feltene fylt ut?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //loadList();

    }

    public void handleStorageOfTrips(String tripName, String tripDescription, String tripDifficulty, String municipality, String county){

        //Upload to firebase, timer and location will be handled from timer in savetripfragment and location from geolocation/spinner with choices.
        Trip.addTrip(tripName,savedCoordinates,MainScreen.mUser,tripDifficulty,county,municipality,tripDescription,null,"Rusletur", "0", "", "Lokal");
        //After add trip

    }

    public void handleOfflineStorageOfTrips(String tripName, String tripDescription, String tripDifficulty, String municipality, String county){

        //Generate a unique id for the trip:
        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        LocalStorage localStorage = LocalStorage.getInstance(this);
        localStorage.addTrip(new Trip(timestamp, tripName, "Gi et tag", tripDifficulty, "Lokal", county, municipality, tripDescription, "Rusletur","Blank", savedCoordinates, "0"));
        Toast.makeText(this,"Tur lagret",Toast.LENGTH_SHORT).show();

    }


    private void setupFylkeSpinner(){
        Log.d(TAG, "setupFylkeSpinner: setupSpinner2: Started setup method");
        tmpFylker.clear();
        tmpKommuner.clear();
        countySpinner = findViewById(R.id.savetrip_selectCounty);
        for(int i = 0; i < LookUpFylkerOgKommunerGitHub.fylkerOgKommuner.size(); i++){
            tmpFylker.add(LookUpFylkerOgKommunerGitHub.fylkerOgKommuner.get(i).get(0));
        }
        //ArrayAdapter
        Log.d(TAG, "setupFylkeSpinner: setupSpinner: countySpinner: " + countySpinner);
        ArrayAdapter<String> fylkeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tmpFylker);
        fylkeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        Log.d(TAG, "setupFylkeSpinner: setupSpinner: adapter: " + fylkeAdapter);
        countySpinner.setAdapter(fylkeAdapter);

    }


    private void loadList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                LookUpFylkerOgKommunerGitHub lookup = new LookUpFylkerOgKommunerGitHub(SaveTripActivity.this);
                lookup.createObjectsFromFile();
            }
        }).run();
        checkFinished();

    }

    private void checkFinished(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(finished){
                    setupFylkeSpinner();
                }else{
                    checkFinished();
                }
            }
        }, 1000);
    }
    

}
