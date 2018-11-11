package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.DialogInterface;
import android.os.Bundle;
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

import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
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
    private HttpURLConnection conn = null;
    private ArrayList<ArrayList<String>> fylkerOgKommuner = new ArrayList<>();
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
        Log.i(TAG, String.valueOf(savedCoordinates.size()));

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

        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());

        String msg = LocalStorageTrips.addAitemToStorage(this,new Trip(timestamp, tripName, null, tripDifficulty, "Lokal", county, municipality, tripDescription, "Rusletur","", savedCoordinates, "0"));
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }

    private void loadList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url =  new URL("https://raw.githubusercontent.com/Andreni/Rusletur_gruppe6/master/fylkerMedKommuner.txt?token=Ae4q3_gVHObUYa-Lnzn5cr33OnSZNJmvks5b8BTFwA%3D%3D");

                    Log.d(TAG, "setupArray: setupSpinner: Made URL");

                    conn = (HttpURLConnection) url.openConnection();
                    Log.d(TAG, "setupArray: setupSpinner: Make conn");

                    InputStream in = conn.getInputStream();
                    Log.d(TAG, "setupArray: setupSpinner: Made inpustream");

                    if(conn.getResponseCode() == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String inputLine;
                        Log.d(TAG, "setupArray: setupSpinner: Buffered Reader");
                        while((inputLine = br.readLine()) != null){
                            Log.d(TAG, "setupArray: setupSpinner: WHILE");
                            Log.d(TAG, "run: setupSpinner: inputLine: " + inputLine);
                            String[] fylkeKommune = inputLine.split(":");
                            Log.d(TAG, "run: setupSpinner: fylkeKommueArray plass 0: " + fylkeKommune[0] + "; Plass 1: " + fylkeKommune[1]);
                            boolean fylkeExists = false;
                            int fylkeIndex = 0;
                            for(int i = 0; i < fylkerOgKommuner.size(); i++){
                                if (fylkerOgKommuner.get(i).get(0).equals(fylkeKommune[0])) {
                                    fylkeExists = true;
                                    fylkeIndex = i;
                                    break;
                                }
                            }
                            if(fylkeExists){
                                fylkerOgKommuner.get(fylkeIndex).add(fylkeKommune[1]);
                            }else{
                                Log.d(TAG, "run: setupSpinner: Fylkerogkommuner size: " + fylkerOgKommuner.size());
                                fylkerOgKommuner.add(new ArrayList<String>(Arrays.asList(fylkeKommune[0], fylkeKommune[1])));
                            }
                        }
                    }

                }catch (MalformedURLException e){
                    Log.d(TAG, "setupArray: setupSpinner: MAlformed url exception");
                    e.printStackTrace();
                }catch (IOException e) {
                    Log.d(TAG, "setupArray: setupSpinner: IOexception");
                    e.printStackTrace();
                }
                setupFylkeSpinner();
            }
        }).start();
    }

    private void setupFylkeSpinner(){
        tmpFylker.clear();
        tmpKommuner.clear();
        countySpinner = findViewById(R.id.savetrip_selectCounty);
        for(int i = 0; i < fylkerOgKommuner.size(); i++){
            tmpFylker.add(fylkerOgKommuner.get(i).get(0));
        }
        //ArrayAdapter
        Log.d(TAG, "setupFylkeSpinner: setupSpinner: countySpinner: " + countySpinner);
        ArrayAdapter<String> fylkeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tmpFylker);
        fylkeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        Log.d(TAG, "setupFylkeSpinner: setupSpinner: adapter: " + fylkeAdapter);
        countySpinner.setAdapter(fylkeAdapter);

    }


}
