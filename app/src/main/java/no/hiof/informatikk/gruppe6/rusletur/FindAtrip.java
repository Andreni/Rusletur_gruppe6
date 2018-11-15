package no.hiof.informatikk.gruppe6.rusletur;

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
import java.util.List;

import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.ApiNasjonalturbase;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.Model.Fylke;
import no.hiof.informatikk.gruppe6.rusletur.Model.FylkeList;
import no.hiof.informatikk.gruppe6.rusletur.Model.Kommune;
import no.hiof.informatikk.gruppe6.rusletur.Model.LocalStorage;
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.MainTripRecyclerViewAdapter;
import no.hiof.informatikk.gruppe6.rusletur.Spinner.CustomSpinnerAdapter;
import no.hiof.informatikk.gruppe6.rusletur.Spinner.SpinnerData;

/**
 * Class for selection of available trips for the user.
 * Checks first if there are any available trips in Fylkeliste, then checks with LocalStorage and
 * Firebase Database.
 * Takes backs generated objects and populates the spinners with them.
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

    /**
     *
     * Checks with Fylkelist to see what objects exists, then check all avalible trips stored
     * locally on the device and on Firebase Database.
     * Only {@see Fylke} and Kommune that contains Trip objects, will be selectable by the user
     */
    public void loadLists() {
        if (FylkeList.getRegisterForFylke().size() > 15) {
            localStorage = LocalStorage.getInstance(FindAtrip.this);
            ArrayList<Trip> rusleTurTrips = localStorage.getAllTrips();
            rusleTurTrips.addAll(Trip.allCustomTrips);
            //If there exists a object with a Kommune that is not in the Register that was downloaded
            for (Trip trip : rusleTurTrips) {
                boolean kommuneExists = true;
                for (int i = 1; i < FylkeList.getRegisterForFylke().size(); i++) {
                    if (FylkeList.getRegisterForFylke().get(i).getFylkeName().startsWith(trip.getFylke())) {
                        for (int j = 0; j < FylkeList.getRegisterForFylke().get(i).getKommuneArrayList().size(); j++) {
                            if (FylkeList.getRegisterForFylke().get(i).getKommuneArrayList()
                                    .get(j).getKommuneNavn().equals(trip.getKommune())
                                    ||FylkeList.getRegisterForFylke().get(i)
                                    .getKommuneArrayList().get(j).getKommuneNavn().
                                            startsWith(trip.getKommune())) {
                                kommuneExists = true;
                                break;
                            }
                            else{
                               kommuneExists =false;
                            }
                        }
                    }
                    if (!kommuneExists) {
                        FylkeList.getRegisterForFylke().get(i).getKommuneArrayList().add(new Kommune(trip.getKommune()));
                        kommuneExists = true;
                    }
                }
            }
            setUpFylkeSpinner(FylkeList.getFylkeListArrayList().get(0));
        }
    }

    //Initalize the fylke dropdown menu
    public void setUpFylkeSpinner(FylkeList alist){

        final List<SpinnerData> customList = new ArrayList<>();

        Log.d(TAG, "setUpFylkeSpinner: CustomSpinner aList: " + alist.getRegisterForFylke().size());
        Log.d(TAG, "setUpFylkeSpinner: CustomSpinner aList data: " + alist.getRegisterForFylke().toString());
        customList.add(new SpinnerData("Valg: "));
        for(int i = 1; i < alist.getRegisterForFylke().size(); i++){
            Log.d(TAG, "setUpFylkeSpinner: CustomSpinner: Inside forloop");
            String name = alist.getRegisterForFylke().get(i).getFylkeName().toLowerCase();

            if(alist.getRegisterForFylke().get(i).getFylkeName().toLowerCase().contains("ø")){
                name = name.replace("ø", "o");
            }
            if(alist.getRegisterForFylke().get(i).getFylkeName().toLowerCase().contains("-")){
                name = name.replace("-", "_");
            }
            if(alist.getRegisterForFylke().get(i).getFylkeName().toLowerCase().contains(" ")){
                name = name.replace(" ", "_");
            }
            if(alist.getRegisterForFylke().get(i).getFylkeName().toLowerCase().contains("finnmark")){
                name = "finnmark";
            }
            customList.add(new SpinnerData(alist.getRegisterForFylke().get(i).getFylkeName(), getResources().getIdentifier(name, "drawable", getPackageName())));
            Log.d(TAG, "setUpFylkeSpinner: CustomSpinner: Name: " + name);
            Log.d(TAG, "setUpFylkeSpinner: CustomSpinner: added to customList: " + new SpinnerData(alist.getRegisterForFylke().get(i).getFylkeName(), getResources().getIdentifier(name, "drawable", getPackageName())));
        }

        spinnerFylke = findViewById(R.id.tripsA_SelectFylke_spinner);
        //Load fylker from array
        CustomSpinnerAdapter arrayAdapterFylke =
                new CustomSpinnerAdapter(FindAtrip.this, R.layout.spinner_layout_listitem
                        ,customList);
        //arrayAdapterFylke.setDropDownViewResource(R.layout.spinner_layout_listitem);
        spinnerFylke.setAdapter(arrayAdapterFylke);

        Log.d(TAG, "setUpFylkeSpinner: CustomSpinner: adapter set");
        Log.d(TAG, "setUpFylkeSpinner: CustomSpinner: spinner.toString: " + spinnerFylke.toString());


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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * When a valid id (Not 0 and 0 on the Selection spinners) are chosen for Fylke and kommune. Fetch the valid ids
     * stored on the kommune object, and pass them to the recycler view.
     * Send a call to {@see getC}
     */
    public void fetchIds() {

        if (FylkeList.getRegisterForFylke()
                .get(selectionFylke)
                .getKommuneArrayList().get(selectionKommune + 1).getIdForTurArrayList().size() == 0) {
            onlyRusleturTrips = true;
            Log.i(TAG, "fetchIds: this is a RULSETUR");
            getSelectedFylkeAndKommune();
            antall = 0;
            turer.clear();
            lookUpRusleTurTrips(localStorage);

        }else if (!onlyRusleturTrips) {
            Log.i(TAG, "fetchIds: Looking up from all sources");
            turer.clear();
            getSelectedFylkeAndKommune();
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
                //If there exists no stored ids in register, skip checking with Nasjonal Turbase
                ApiNasjonalturbase.getTripInfo(selection, this);
            }
        }
        initRecyclerView();
    }

    public void getSelectedFylkeAndKommune(){
        selectionNameFylke = FylkeList.getRegisterForFylke()
                .get(selectionFylke).toString();
        selectionNameKommune = FylkeList.getRegisterForFylke()
                .get(selectionFylke)
                .getKommuneArrayList()
                .get(selectionKommune + 1).toString();
    }

    public void lookUpRusleTurTrips(LocalStorage localStorage){
        //Check localStorage
        if (localStorage.getTripsByCriteria(selectionNameFylke, selectionNameKommune).size() > 0) {
            Log.i(TAG, "fetchIds: We found a match");
            turer.addAll(localStorage.getTripsByCriteria(selectionNameFylke, selectionNameKommune));
            antall += localStorage.getTripsByCriteria(selectionNameFylke, selectionNameKommune).size();
            Log.i(TAG, "fetchIds: " + antall + " results");
        }
        if(Trip.allCustomTrips.size()>0){
            for (Trip aTrip: Trip.allCustomTrips){
                if (aTrip.getFylke().equals(selectionNameFylke)||selectionNameFylke.startsWith(aTrip.getFylke())){
                    if(aTrip.getKommune().equals(selectionNameKommune)||selectionNameKommune.startsWith(aTrip.getKommune())){
                        turer.add(aTrip);
                        Log.d(TAG, "lookUpRusleTurTrips: Tidsbruk: " + aTrip.getTidsbruk());
                        antall++;
                    }
                }
            }
        }
    }

    //Init the recycler view.
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
            onlyRusleturTrips = false;
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
                //Does there exists trips that matches the search criteria?
                lookUpRusleTurTrips(localStorage);

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
