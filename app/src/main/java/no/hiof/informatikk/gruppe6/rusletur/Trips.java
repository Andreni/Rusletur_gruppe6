package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
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
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.MainTripRecyclerViewAdapter;

/**
 * Class for selection of available trips for the user
 * Sends a call to start downloading register.json
 * Takes backs generated objects and populates the spinners with them
 */
public class Trips extends AppCompatActivity  {
    private Spinner spinnerKommune;
    private Spinner spinnerFylke;
    private Boolean kommuneListLoaded = false;
    private Boolean fylkeListLoaded = false;
    private String TAG = "TRIPS";
    private int selectionFylke = 0;
    private int selectionKommune = 0;
    public static ArrayList<Trip> turer = new ArrayList<>();
    private MainTripRecyclerViewAdapter adapter;
    private int antall = 0;



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
        ArrayAdapter<Fylke> arrayAdapterFylke =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1
                        ,alist.getRegisterForFylke());
        arrayAdapterFylke.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerFylke.setAdapter(arrayAdapterFylke);

        //When only FylkeSpinner is in use and there is no valid selection, do not show kommuneSpinner
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

                turer.clear();
                antall = 0;

                int lastPosition = position;

                    if (position==0){

                    }else{
                        kommuneListLoaded = true;
                        selectionKommune = position-1;
                        //Send the position so we can start a search based on all the valid ids
                        fetchIds();
                    }
                Log.d(TAG, "onItemSelected: " + turer.toString());

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * When a valid id (Not 0 and 0) are chosen for Fylke and kommune. Fetch the valid ids
     * stored on the kommune object, and pass them to the recycler view
     */
    public void fetchIds() {
        //Loop to cycle to all ids stored on Kommune object chosen
        for(int i = 0; i < FylkeList.getRegisterForFylke()
                .get(selectionFylke)
                .getKommuneArrayList()
                .get(selectionKommune)
                .getIdForTurArrayList().size(); i++) {
            //Fetch the id that is to be made an object from.
            String selection = FylkeList.getRegisterForFylke()
                    .get(selectionFylke)
                    .getKommuneArrayList()
                    .get(selectionKommune)
                    .getIdForTurArrayList().get(i).getIdForTur();
            //Pass the id to the API class to build a trip object from it
            Log.d(TAG, "fetchIds: Addede to turer");
            ApiNasjonalturbase.getTripInfo(selection, this);
        }

        Log.d(TAG, "onResponse: Init?");
        initRecyclerView();

    }


    /**
     * Method for passing the selected trip, so it can be passed
     * to the DisplayAtrip Activity
     * @param aTrip the trip selected from the recycler view
     */
    public void passSelectedTrip(Trip aTrip){

        // using context and next component class to create intent
        Intent intent = new Intent(this, DisplayAtrip.class);
        // using putExtra(String key, Parcelable value) method
        intent.putExtra("object", aTrip);
        startActivity(intent);
    }


    public void initRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.tripsRecyclerView);
        adapter = new MainTripRecyclerViewAdapter(this, turer);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "onResponse: Run check");
        checkChange();

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(antall < turer.size()){
                adapter.notifyItemInserted(0);
                Log.d(TAG, "onResponse: CHECK");
                antall++;
                checkChange();
            }
        }
    };

    private void checkChange(){
        Log.d(TAG, "onResponse: Check runned");

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(antall < turer.size()){
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 0);
                }if(antall == turer.size()){
                    Log.d(TAG, "run: onResonse: " + turer);
                    checkChange();
                }
            }
        }, 3000);

    }
}
