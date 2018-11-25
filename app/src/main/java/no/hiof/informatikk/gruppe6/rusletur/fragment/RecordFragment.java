package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.MapsActivity;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.ShowProgressOfTrip;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.TripTracker;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.UserUtility;

/**
 * comment
 * @author Bjørnar P.
 *
 */
public class RecordFragment extends Fragment {

    String userEmail;
    Button signOut;
    ConstraintLayout layout;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public static final String TAG = "RusleTur";
    private int LOCATION_PERMISSION_CODE = 1;
    private ArrayList<String> mItem = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<LatLng> savedTrip;
    private Chronometer chronometer;
    private boolean timerRunning;
    private Button recordTripButton;
    private Button stopRecordButton;
    private Button showInMapButton;

    private static boolean isTimerRunning;


    //Worst practice = best practice.
    public static boolean saveWasClicked = false;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Inflate view. Basically "set" view.
        final View view = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        Log.d("TESTER", "UTENFOR ONCLICK");

        //Get the maker of the phone
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equals("Huawei")||manufacturer.equals("HUAWEI")){
            //If the user has a Huawei product, warn them about battery saving features:
            TextView text_warning = view.findViewById(R.id.fragment_mainscreen_warning);
            text_warning.setVisibility(View.VISIBLE);
        }


        /*
        * This is how you call methods from fragment to activity. In most cases it's easier to create the relevant methods here,
        * except in cases where you want to create a method which more than one fragment needs.
        * Currently two buttons: Start record and stop record.
        * Start record checks if the button has already been clicked, because startService
        * calls onStartCommand in service, where a method is called to start recording.
        * onStartCommand runs everytime the button is clicked, so if you click it multiple times
        * FusedLocationProvider will queue up multiple times.
         */

        chronometer = view.findViewById(R.id.fragment_mainscreen_chronometer);
        chronometer.setFormat("Tid brukt på tur: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        recordTripButton = view.findViewById(R.id.recordTripButton);
        stopRecordButton = view.findViewById(R.id.stopRecordButton);
        showInMapButton = view.findViewById(R.id.showContextOfArray);
        stopRecordButton.setVisibility(View.INVISIBLE);
        showInMapButton.setVisibility(View.INVISIBLE);

        if(isTimerRunning){
            recordTripButton.setVisibility(View.INVISIBLE);
            stopRecordButton.setVisibility(View.VISIBLE);
            showInMapButton.setVisibility(View.VISIBLE);
        }





        recordTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtility.checkIfUserHasMobileNetworkEnabled(getActivity()) && UserUtility.checkIfUserHasGPSEnabled(getActivity())) {
                    Intent startRecordIntent = new Intent(getActivity(), TripTracker.class);
                    getActivity().startService(startRecordIntent);

                    recordTripButton.setVisibility(View.INVISIBLE);
                    stopRecordButton.setVisibility(View.VISIBLE);
                    showInMapButton.setVisibility(View.VISIBLE);

                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    isTimerRunning = true;
                }
                else {
                    Toast.makeText(getActivity(), "Trenger internett for å starte denne funksjonen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        * Stop record currently creates an AlertDialog which gives the user a choice of either
        * discarding their trip, or saving it.
         */

        stopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }

                    builder.setTitle("Avslutt tur")
                            .setMessage("Vil du lagre denne turen?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(UserUtility.checkIfUserHasMobileNetworkEnabled(getActivity()) && UserUtility.checkIfUserHasGPSEnabled(getActivity())) {
                                        Log.i(MapsActivity.TAG, "Yes selected");
                                        saveWasClicked = true;
                                        Intent saveAndStopIntent = new Intent(getActivity(), TripTracker.class);
                                        getActivity().stopService(saveAndStopIntent);
                                        chronometer.stop();
                                        recordTripButton.setVisibility(View.VISIBLE);
                                        stopRecordButton.setVisibility(View.INVISIBLE);
                                        showInMapButton.setVisibility(View.INVISIBLE);
                                        isTimerRunning = false;
                                        
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "Du mangler internett, får desverre ikke lagret turen", Toast.LENGTH_LONG).show();
                                    }


                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "Tur slettet", Toast.LENGTH_SHORT).show();
                                    saveWasClicked = false;
                                    Intent discardAndStopIntent = new Intent(getActivity(), TripTracker.class);
                                    getActivity().stopService(discardAndStopIntent);
                                    recordTripButton.setVisibility(View.VISIBLE);
                                    stopRecordButton.setVisibility(View.INVISIBLE);
                                    showInMapButton.setVisibility(View.INVISIBLE);
                                    isTimerRunning = false;

                                    chronometer.stop();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        });

        showInMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowProgressOfTrip.class);
                    getActivity().startActivity(intent);
            }
        });

        return view;

    }

    public void showcaseMethodTwo(){
        Log.d("RecordFragment", "Called method OK");
    }



}
