package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.ApiNasjonalturbase;
import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.LookUpRegisterNasjonalTurbase;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;
import no.hiof.informatikk.gruppe6.rusletur.Model.Fylke;
import no.hiof.informatikk.gruppe6.rusletur.Model.FylkeList;
import no.hiof.informatikk.gruppe6.rusletur.Model.IdForTur;
import no.hiof.informatikk.gruppe6.rusletur.Model.Kommune;

/**
 * Class for selection of available trips for the user
 * Sends a call to start downloading register.json
 * Takes backs generated objects and populates the spinners with them
 */
public class Trips extends AppCompatActivity  {
    Spinner spinnerKommune;
    Spinner spinnerFylke;
    Boolean kommuneListLoaded = false;
    Boolean fylkeListLoaded = false;
    String TAG = "TRIPS";
    int selectionFylke = 0;
    int selectionKommune = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);


        loadLists();
    }

    public void loadLists(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                LookUpRegisterNasjonalTurbase lookUpRegisterNasjonalTurbase = new LookUpRegisterNasjonalTurbase(Trips.this);
                lookUpRegisterNasjonalTurbase.createObjectsFromRegister();
                setUpFylkeSpinner(FylkeList.getFylkeListArrayList().get(0));
            }
        }).run();




    }



    //Initalize the fylke dropdown menu
    public void setUpFylkeSpinner(FylkeList alist){
        spinnerFylke = findViewById(R.id.tripsA_SelectFylke_spinner);
        //Load fylker from array
        ArrayAdapter<Fylke> arrayAdapterFylke = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,alist.getRegisterForFylke());
        arrayAdapterFylke.setDropDownViewResource(android.R.layout.simple_list_item_1);

        spinnerFylke.setAdapter(arrayAdapterFylke);

        spinnerKommune = findViewById(R.id.tripsA_SelectKommune_spinner2);
        spinnerKommune.setVisibility(View.INVISIBLE);

        spinnerFylke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String fylkeSelected;
                    //If the position is 0, nothing is selected
                    if (position==0){
                        spinnerKommune.setVisibility(View.INVISIBLE);

                    }
                    else if(kommuneListLoaded){
                        //If there already is loaded a kommunelist, set the kommunespinner
                        //selection to zero
                        spinnerKommune.setVisibility(View.VISIBLE);
                        spinnerKommune.setSelection(0);
                        setupKommuneSpinner(position);

                    }

                    else{
                        //When a valid selection is made, pass the positon for the method,
                        //so the proper kommunelist can be loaded from the fylke.
                        spinnerKommune.setVisibility(View.VISIBLE);
                        setupKommuneSpinner(position);
                        fylkeListLoaded = true;
                        selectionFylke = position;
                    }

            }
            //Not in use
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    //Load the kommunespinner
    public void setupKommuneSpinner(Integer positonFylke){
        //Load Kommuner from array
        //Setup adapter for loading Kommune objects
        ArrayAdapter<Kommune> arrayAdapterKommune =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,
                FylkeList.getRegisterForFylke()
                        .get(positonFylke)
                        .getKommuneArrayList());
        arrayAdapterKommune.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerKommune.setAdapter(arrayAdapterKommune);

        spinnerKommune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Selection statement for K spinner

                int lastPosition = position;

                    if (position==0){

                    }else{
                        kommuneListLoaded = true;
                        selectionKommune = position;
                        //Send the position so we can start a search based on all the valid ids
                        fetchIds();
                    }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void fetchIds(){

        String ids = FylkeList.getRegisterForFylke().get(selectionFylke).getKommuneArrayList().get(selectionKommune).getIdForTurArrayList().get(0).getIdForTur();

        String selection = FylkeList.getRegisterForFylke().get(selectionFylke).getKommuneArrayList().get(selectionKommune).getIdForTurArrayList().get(0).getIdForTur();
        Toast.makeText(this," ewrw",Toast.LENGTH_SHORT).show();

        ApiNasjonalturbase.getTripInfo(selection,this);





    }
}
