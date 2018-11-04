package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.MapsActivity;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.TripTracker;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class MainMenuFragment extends Fragment {

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
    private boolean recordAlreadyClicked = false;
    //Worst practice = best practice.
    public static boolean saveWasClicked = false;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Inflate view. Basically "set" view.
        final View view = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        Log.d("TESTER", "UTENFOR ONCLICK");



        //Showcase Method: How to find items in fragment XML.
        view.findViewById(R.id.mainScreen_logout_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        * This is how you call methods from fragment to activity. In most cases it's easier to create the relevant methods here,
        * except in cases where you want to create a method which more than one fragment needs.
        * Currently two buttons: Start record and stop record.
        * Start record checks if the button has already been clicked, because startService
        * calls onStartCommand in service, where a method is called to start recording.
        * onStartCommand runs everytime the button is clicked, so if you click it multiple times
        * FusedLocationProvider will queue up multiple times.
         */
        // ((MainScreen)getActivity()).showcaseMethod();




        view.findViewById(R.id.recordTripButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!recordAlreadyClicked) {
                    getActivity().startService(new Intent(getActivity(), TripTracker.class));
                }
                else{
                    Toast.makeText(getActivity(), "Recording in prosess. Stop first", Toast.LENGTH_SHORT).show();
                }
                recordAlreadyClicked = true;
            }
        });

        /*
        * Stop record currently creates an AlertDialog which gives the user a choice of either
        * discarding their trip, or saving it.
         */

        view.findViewById(R.id.stopRecordButton).setOnClickListener(new View.OnClickListener() {
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
                                Log.i(MapsActivity.TAG, "Yes selected");
                                saveWasClicked = true;
                                recordAlreadyClicked = false;
                                getActivity().stopService(new Intent(getActivity(), TripTracker.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Tur slettet", Toast.LENGTH_SHORT).show();
                                recordAlreadyClicked = false;
                                saveWasClicked = false;
                                getActivity().stopService(new Intent(getActivity(), TripTracker.class));
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return view;

    }

    public void showcaseMethodTwo(){
        Log.d("MainMenuFragment", "Called method OK");
    }



}
