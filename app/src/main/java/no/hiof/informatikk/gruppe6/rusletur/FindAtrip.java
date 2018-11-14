package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.ApiNasjonalturbase;
import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.LookUpRegisterNasjonalTurbase;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.Model.Fylke;
import no.hiof.informatikk.gruppe6.rusletur.Model.FylkeList;
import no.hiof.informatikk.gruppe6.rusletur.Model.Kommune;
import no.hiof.informatikk.gruppe6.rusletur.Model.LocalStorage;
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.MainTripRecyclerViewAdapter;

/**
 * Class for selection of available trips for the user
 * Sends a call to start downloading register.json
 * Takes backs generated objects and populates the spinners with them
 */
public class FindAtrip extends AppCompatActivity  {
    private Spinner spinnerKommune;
    private Spinner spinnerFylke;
    private Boolean kommuneListLoaded = false;
    private Boolean fylkeListLoaded = false;
    private String TAG = "TRIPS";
    private int selectionFylke = 0;
    private String selectionNameFylke = "";
    private int selectionKommune = 0;
    private String selectionNameKommune = "";
    public static ArrayList<Trip> turer = new ArrayList<>();
    private MainTripRecyclerViewAdapter adapter;
    private int antall = 0;
    private ProgressBar pgsBar;
    private Boolean onlyRusleturTrips = false;
    private LocalStorage localStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        pgsBar = findViewById(R.id.progressBarForLoadingTrips);

        loadLists();
    }

    public void loadLists() {
        Log.d(TAG, "run: setupTrips: CHECK");
        if (FylkeList.getRegisterForFylke().size() > 15) {
            localStorage = LocalStorage.getInstance(FindAtrip.this);
            ArrayList<Trip> localTrips = localStorage.getAllTrips();


            Log.d(TAG, "run: setupTrips: Adding trips from local");
            for (Trip trip : localTrips) {
                boolean kommuneExists = true;
                Log.d(TAG, "run: setupTrips: Fylke: " + trip.getFylke() + "; Kommune: " + trip.getKommune());
                Log.d(TAG, "run: setupTrips: getREgisterForFylke length: " + FylkeList.getRegisterForFylke().size());
                for (int i = 1; i < FylkeList.getRegisterForFylke().size(); i++) {
                    Log.d(TAG, "run: setupTrips> Before first if");
                    if (FylkeList.getRegisterForFylke().get(i).getFylkeName().startsWith(trip.getFylke())) {
                        for (int j = 1; j < FylkeList.getRegisterForFylke().get(i).getKommuneArrayList().size(); j++) {
                            Log.d(TAG, "run: setupTrips> Before second if");
                            if (FylkeList.getRegisterForFylke().get(i).getKommuneArrayList().get(j).getKommuneNavn().startsWith(trip.getKommune())
                                    || FylkeList.getRegisterForFylke().get(i).getKommuneArrayList().get(j).getKommuneNavn().equals(trip.getKommune())
                                    ) {
                                Log.d(TAG, "run: Municipality already exists");
                                kommuneExists = true;
                            }
                            else{
                                kommuneExists =false;
                            }
                        }
                    }
                    if (!kommuneExists) {
                        Log.d(TAG, "run: setupTrips: Adding municipality");
                        FylkeList.getRegisterForFylke().get(i).getKommuneArrayList().add(new Kommune(trip.getKommune()));
                        kommuneExists = true;
                    }
                }
            }

            //  Get all ojects from SQLite db (this is fast :)
            //Start looping through the array for matches
            //for each loop that scans fylke
            //if FylkeListe.aFylke == objectFromDB
            //This is the parent object we need to control "Kommune" on.
            //If FylkeListe.aFylke.aKommune == objectFromDB
            // Kommune already exist.... skipping

            // //If FylkeListe.aFylke.aKommune != objectFromDB
            //This is the parent object we want to make available for selection
            //Insert this kommune into selectable kommune objects!
            //Proit

            setUpFylkeSpinner(FylkeList.getFylkeListArrayList().get(0));

        }
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
                 turer.clear();
                 antall = 0;

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

    public void setupKommuneSpinner(final Integer positonFylke){
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
                        kommuneListLoaded = false;
                        selectionKommune = 0;
                    }else{
                        kommuneListLoaded = true;
                        selectionKommune = position-1;
                        selectionFylke = positonFylke;
                        pgsBar.setVisibility(View.VISIBLE);
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

        if (FylkeList.getRegisterForFylke()
                .get(selectionFylke)
                .getKommuneArrayList().get(selectionKommune + 1).getIdForTurArrayList().size() == 0) {
            onlyRusleturTrips = true;
            Log.i(TAG, "fetchIds: this is a RULSETUR");
            selectionNameFylke = FylkeList.getRegisterForFylke()
                    .get(selectionFylke).toString();
            selectionNameKommune = FylkeList.getRegisterForFylke()
                    .get(selectionFylke)
                    .getKommuneArrayList()
                    .get(selectionKommune + 1).toString();

            if (localStorage.getTripsByCriteria(selectionNameFylke, selectionNameKommune).size() > 0) {
                turer.addAll(localStorage.getTripsByCriteria(selectionNameFylke, selectionNameKommune));
                antall += localStorage.getTripsByCriteria(selectionNameFylke, selectionNameKommune).size();

            }
            initRecyclerView();


        }
        //Loop to cycle to all ids stored on Kommune object chosen

        if (!onlyRusleturTrips) {
            for (int i = 0; i < FylkeList.getRegisterForFylke()
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
                selectionNameFylke = FylkeList.getRegisterForFylke()
                        .get(selectionFylke).toString();
                selectionNameKommune = FylkeList.getRegisterForFylke()
                        .get(selectionFylke)
                        .getKommuneArrayList()
                        .get(selectionKommune + 1).toString();
                //Pass the id to the API class to build a trip object from it
                Log.d(TAG, "fetchIds: Addede to turer");


                //If there exsicsts no stored ids in register, skip checking with Nasjonal Turbase
                if (!onlyRusleturTrips) {
                    ApiNasjonalturbase.getTripInfo(selection, this);

                }
                //If the response to the query is greater than -1 add the results to the array
                Log.d("SQLQ", "Searching for: " + selectionNameKommune + " in" + selectionNameFylke);


                // }


            }

            Log.d(TAG, "onResponse: Init?");
            initRecyclerView();

        }
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
        if(!onlyRusleturTrips){
            checkChange();
        }else{
            //No more trips are to be loaded. Hide the loading bar
            pgsBar.setVisibility(View.INVISIBLE);
        }



    }


    //Handler for notifying a new item in recycler view
    private Handler handler = new Handler();
    //It's a handler/runnablew for doing it in a new thread
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //If antall (amaount of trips in recyclerView) is less than the amount of trips in ArrayList
            if(antall < turer.size()){
                //When the loading is complete, remove progression bar
                pgsBar.setVisibility(View.GONE);
                //The method for loading a new item that is in the array for the recyclerview
                adapter.notifyItemInserted(0);
                Log.d(TAG, "onResponse: CHECK");

                //incrementing antall
                antall++;
                //Call to local storage:
                LocalStorage localStorage = LocalStorage.getInstance(getApplicationContext());

                if (localStorage.getTripsByCriteria(selectionNameFylke,selectionNameKommune).size()>0){
                    turer.addAll(localStorage.getTripsByCriteria(selectionNameFylke,selectionNameKommune));
                    antall+= localStorage.getTripsByCriteria(selectionNameFylke,selectionNameKommune).size();
                }


                //Recursion. Make sure to have a constant loop to always check if there is a new item in the arraylist
                checkChange();
            }
        }
    };

    //Method for cheking new items in arraylist for recycler view
    private void checkChange(){
        Log.d(TAG, "onResponse: Check runned");

        //Makes a new handler, it should run in a new thread so the UI dosnæt stop
        final Handler handler2 = new Handler();
        //Makes it so it chekcs every 3 seconds. Laggy if we constantly makes new threads?
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                //If there is a new item
                if(antall < turer.size()){
                    //Removes any pending postDelay callbacks in handler
                    handler.removeCallbacks(runnable);
                    //Runs the hadnler with a postDelayed method, set to 0 seconds
                    handler.postDelayed(runnable, 0);
                }
                //If there isn't a new item
                if(antall == turer.size()){
                    Log.d(TAG, "run: onResonse: " + turer);
                    //Recursion. Checking again after 3 seconds
                    checkChange();
                }
            }
        }, 3000);

    }
}
