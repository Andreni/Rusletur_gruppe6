package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.Model.LocalStorage;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;
import pub.devrel.easypermissions.EasyPermissions;

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
    EditText editHour;
    EditText editMin;
    Boolean isImport = false;
    String tripLength = "0";
    private String[] neededPermissions = { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE };


    private ArrayList<LatLng> savedCoordinates = new ArrayList<>();

    //Setup Spinner
    //private HttpURLConnection conn = null;
    //private ArrayList<ArrayList<String>> fylkerOgKommuner = new ArrayList<>();
    public static boolean finished = false;
    private boolean valgtKommune = false;
    private ArrayList<String> tmpFylker = new ArrayList<>();
    private ArrayList<String> tmpKommuner = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_savetrip);
        checkPermissions();

        //Setup views
        //countySpinner = findViewById(R.id.savetrip_selectCounty);
        municipalitySpinner = findViewById(R.id.savetrip_selectMunicipality);
        nameInput = findViewById(R.id.savetrip_nameOfTripInput);
        descInput = findViewById(R.id.savetrip_descriptionInput);

        savedCoordinates = getIntent().getParcelableArrayListExtra("coordsArray");
        String senderActivity = getIntent().getStringExtra("sender");

        //If the sender of the intent is LocalStorageTrips, we need to fill in time:
        if(senderActivity.equals("LocalStorageTrips")){
            //Enable manual handling of time insertion
            editHour = findViewById(R.id.saveTrip_Hour_editText);
            editMin = findViewById(R.id.saveTrip_Min_editText);
            TextView textHour = findViewById(R.id.savetrip_timer_textview);
            textHour.setVisibility(View.VISIBLE);
            TextView textMin = findViewById(R.id.savetrip_minutter_textview);
            textMin.setVisibility(View.VISIBLE);
            editHour.setVisibility(View.VISIBLE);
            editMin.setVisibility(View.VISIBLE);
            isImport=true;

        }
        Log.i(TAG, "SaveTripActivity mottok Array! : " + String.valueOf(savedCoordinates.size()));

        //String with custom time spent on trip. Can be in any format, Day:Hour:Minute:Seconds
        String timeSpent = getIntent().getStringExtra("timeSpent");
        Toast.makeText(this, "Tid brukt på tur: " + timeSpent, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "SaveTripActivity mottok String au! : " + timeSpent);

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

                Log.d(TAG, "onClick: setupSpinner: What is false: nameInput: " + nameInput + "; descInput: " + descInput + "; selectedDifficulty: " + selectedDifficulty + "; valgtKommuner: " + valgtKommune);



                if (nameInput != null && descInput != null && selectedDifficulty != null && valgtKommune) {

                    /*
                     * Check if input is not null.
                     * If everything checks out, send input from name, description and radiogroup to
                     * handleStorageOfTrips in MainScreen, and switch from this fragment back to the
                     * MainMenuFragment.
                     */

                    //Assume the input is valid when the trip is not imported:
                    Boolean validInput = true;
                    Boolean includeHour = false;


                    // If the trip is imported, check if the user has filled in time values:
                    if(isImport){
                        if(!editHour.getText().toString().equals("") && Integer.parseInt(editHour.getText().toString()) > 0){
                            includeHour = true;
                            if(Integer.parseInt(editHour.getText().toString())>1){
                                tripLength =  editHour.getText().toString() + " timer ";
                            }else{
                                tripLength =  editHour.getText().toString() + " time ";
                            }
                        }
                        if(!editMin.getText().toString().equals("") && Integer.parseInt(editMin.getText().toString()) > 0) {
                            if(Integer.parseInt(editMin.getText().toString())>1){
                                if(includeHour){tripLength +=  editMin.getText().toString() + " minutter ";}else {tripLength =  editMin.getText().toString() + " minutter ";}

                            }else{
                                if(includeHour){tripLength +=  editMin.getText().toString() + " minutt ";}else {tripLength =  editMin.getText().toString() + " minutt ";}
                            }
                        }
                        //If the time value has not changed, the input is not valid:
                        if(tripLength.equals("0")) validInput = false;
                    }

                    nameinput = nameInput.getText().toString();
                    description = descInput.getText().toString();
                    municipality = municipalitySpinner.getSelectedItem().toString();
                    county = countySpinner.getSelectedItem().toString();
                if(validInput) {
                    new AlertDialog.Builder(SaveTripActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Lagring")
                            .setMessage("Vil du dele turen?")
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    handleStorageOfTrips(nameinput, description, selectedDifficulty, municipality, county);
                                    //REDIRECT
                                    startActivity(new Intent(SaveTripActivity.this,MainScreen.class));

                                }
                            })
                            .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    handleOfflineStorageOfTrips(nameinput, description, selectedDifficulty, municipality, county);
                                    //REDIRECT
                                    startActivity(new Intent(SaveTripActivity.this,MainScreen.class));

                                }
                            })
                            .show();

                    }else{
                    Toast.makeText(SaveTripActivity.this, "Du må fylle ut tid,", Toast.LENGTH_SHORT).show();
                     }
                } else {
                    Toast.makeText(SaveTripActivity.this, "Er alle feltene fylt ut?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadList();

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


    public void handleStorageOfTrips(String tripName, String tripDescription, String tripDifficulty, String municipality, String county){

        //Upload to firebase, timer and location will be handled from timer in savetripfragment and location from geolocation/spinner with choices.
        Trip.addTrip(tripName,savedCoordinates,MainScreen.mUser,tripDifficulty,county,municipality,tripDescription,null,"Rusletur", "0", "", "Lokal");
        //After add trip

    }

    public void handleOfflineStorageOfTrips(String tripName, String tripDescription, String tripDifficulty, String municipality, String county){

        //Generate a unique id for the trip:
        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        LocalStorage localStorage = LocalStorage.getInstance(this);
        localStorage.addTrip(new Trip(timestamp, tripName, "Gi et tag", tripDifficulty, "Lokal", county, municipality, tripDescription, "Rusletur","Blank", savedCoordinates, tripLength));
        Toast.makeText(this,"Tur lagret",Toast.LENGTH_SHORT).show();

    }


    private void setupFylkeSpinner(){
        Log.d(TAG, "setupFylkeSpinner: setupSpinner2: Started setup method");
        tmpFylker.clear();
        tmpKommuner.clear();
        countySpinner = findViewById(R.id.savetrip_selectCounty);
        tmpFylker.add("Valg:");
        for(int i = 0; i < LookUpFylkerOgKommunerGitHub.fylkerOgKommuner.size(); i++){
            tmpFylker.add(LookUpFylkerOgKommunerGitHub.fylkerOgKommuner.get(i).get(0));
        }
        //ArrayAdapter
        Log.d(TAG, "setupFylkeSpinner: setupSpinner: countySpinner: " + countySpinner);
        ArrayAdapter<String> fylkeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tmpFylker);
        fylkeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        Log.d(TAG, "setupFylkeSpinner: setupSpinner: adapter: " + fylkeAdapter);
        countySpinner.setAdapter(fylkeAdapter);
        
        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fylkeSelected;
                if(position == 0){
                    //Do nothing
                }else{
                    setupKommuneSpinner(position-1);
                    municipalitySpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setupKommuneSpinner(int position){
        //Loading kommune from array
        tmpKommuner.clear();
        tmpKommuner.add("Valg:");
        for(int i = 1; i < LookUpFylkerOgKommunerGitHub.fylkerOgKommuner.get(position).size(); i++){
            tmpKommuner.add(LookUpFylkerOgKommunerGitHub.fylkerOgKommuner.get(position).get(i));
        }
        ArrayAdapter<String> kommuneArray = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tmpKommuner);
        kommuneArray.setDropDownViewResource(android.R.layout.simple_list_item_1);
        municipalitySpinner.setAdapter(kommuneArray);

        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    valgtKommune = false;
                }else{
                    valgtKommune = true;
                    Log.d(TAG, "onItemSelected: setupSpinner: kommune valgt?" + valgtKommune);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
