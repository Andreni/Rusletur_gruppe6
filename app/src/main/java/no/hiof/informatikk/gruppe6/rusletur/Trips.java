package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.LookUpRegisterNasjonalTurbase;

public class Trips extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> mySpinnerArray = new ArrayList<>();
    Spinner spinnerKommune;
    Spinner spinnerFylke;
    ArrayList<String> mySpinnerArrayk = new ArrayList<>();
    Boolean kommuneListLoaded = false;
    Boolean fylkeListLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        mySpinnerArray.add("Valg:");
        mySpinnerArray.add("Ostfold");
        mySpinnerArray.add("Rogaland");
        mySpinnerArrayk.add("Valg:");
        mySpinnerArrayk.add("Ostfold");
        mySpinnerArrayk.add("Rogaland");

        /*//Find avalible *Fylke* from JSON file
        //Send call to downloader.
        //Populate fylke oversikt.

        LookUpRegisterNasjonalTurbase lookUpRegisterNasjonalTurbase = new LookUpRegisterNasjonalTurbase(this);
        lookUpRegisterNasjonalTurbase.execute("Go");

        //Populate spinner that contains the "FylkeOversikt"
        for(int i=0;i<lookUpRegisterNasjonalTurbase.getFylkeOversikt().size();i++){

            //Cycle through all Fylker
            lookUpRegisterNasjonalTurbase.getFylkeOversikt().get(i);
            //Add to spinner list item

        }

        //onselect listner for fylke spinner.

        String selectedFylke = "";
        lookUpRegisterNasjonalTurbase.execute(selectedFylke);
        //Send new call
        //When selection is made, enable kommune spinner. */

        setUpFylkeSpinner();

    }

    //Initalize the fylke dropdown menu
    public void setUpFylkeSpinner(){
        spinnerFylke = findViewById(R.id.tripsA_SelectFylke_spinner);
        //Load fylker from array
        ArrayAdapter<String> arrayAdapterFylke = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mySpinnerArray);
        arrayAdapterFylke.setDropDownViewResource(android.R.layout.simple_list_item_1);

        spinnerFylke.setAdapter(arrayAdapterFylke);

        spinnerKommune = findViewById(R.id.tripsA_SelectKommune_spinner2);
        spinnerKommune.setVisibility(View.INVISIBLE);

        spinnerFylke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String fylkeSelected;

                    if (position==0){
                        spinnerKommune.setVisibility(View.INVISIBLE);

                    }
                    else if(kommuneListLoaded){
                        //fylkeSelected = getSelectedFylke

                    }

                    else{
                        spinnerKommune.setVisibility(View.VISIBLE);
                        setupKommuneSpinner(parent.getItemAtPosition(position).toString());
                        fylkeListLoaded = true;

                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Selection statement for Fylke spinner



    }

    public void setupKommuneSpinner(String fylke){
        //Load Kommuner from array
        ArrayAdapter<String> arrayAdapterKommune = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mySpinnerArrayk);
        arrayAdapterKommune.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerKommune.setAdapter(arrayAdapterKommune);
        spinnerKommune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Selection statement for Kommune spinner

                int lastPosition = position;

                    if (position==0){

                    }else{
                        kommuneListLoaded = true;
                        fetchIds();
                    }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void fetchIds(){
        Toast.makeText(this,"Search started",Toast.LENGTH_SHORT).show();

    }
}
