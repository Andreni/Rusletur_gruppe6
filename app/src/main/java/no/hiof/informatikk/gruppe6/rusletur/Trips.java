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
    ArrayList<String> mySpinnerArrayk = new ArrayList<>();
    Boolean kommuneListLoaded = false;
    Boolean fylkeListLoaded = false;
    String TAG = "TRIPS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        mySpinnerArrayk.add("Valg:");
        mySpinnerArrayk.add("Ostfold");
        mySpinnerArrayk.add("Rogaland");



        /* How to:
        //Make new register:
        //Top register name aka FylkeListe
        FylkeList aFylkeList = new FylkeList("Listen");
        //Add a new Fylke to the list
        aFylkeList.addFylkeToList(new Fylke("Rogland"));
        //To the excisting fylke, add a "Kommune"
        aFylkeList.getRegisterForFylke().get(0).addKommuneForFylke(new Kommune("Strand"));
        aFylkeList.getRegisterForFylke().get(0).getKommuneArrayList().get(0).addIdForKommune(new IdForTur("sda"));
        */

        setUpDummyData();


    }

    public void setUpDummyData(){
        //Make 2 Fylker
        FylkeList aList = new FylkeList("Register");
        aList.addFylkeToList(new Fylke("Valg:"));
        aList.addFylkeToList(new Fylke("Østfold"));
        aList.addFylkeToList(new Fylke("Rogaland"));

        //Østfold
        aList.getRegisterForFylke().get(1).addKommuneForFylke(new Kommune("Valg:"));
        aList.getRegisterForFylke().get(1).addKommuneForFylke(new Kommune("Halden"));
        aList.getRegisterForFylke().get(1).addKommuneForFylke(new Kommune("Moss"));
        aList.getRegisterForFylke().get(1).getKommuneArrayList().get(0).addIdForKommune(new IdForTur("Hiof"));
        aList.getRegisterForFylke().get(1).getKommuneArrayList().get(1).addIdForKommune(new IdForTur("Mosseporten"));

        //Rogaland
        aList.getRegisterForFylke().get(2).addKommuneForFylke(new Kommune("Valg:"));
        aList.getRegisterForFylke().get(2).addKommuneForFylke(new Kommune("Stavanger"));
        aList.getRegisterForFylke().get(2).addKommuneForFylke(new Kommune("Sandnes"));
        aList.getRegisterForFylke().get(2).getKommuneArrayList().get(1).addIdForKommune(new IdForTur("Harfsfjord"));

        setUpFylkeSpinner(aList);

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
                        //Send the position so we can start a search based on all the valid ids
                        fetchIds(position);
                    }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    
    public void fetchIds(Integer kommunePosition){
        Toast.makeText(this,"Search started" + kommunePosition,Toast.LENGTH_SHORT).show();

    }
}
