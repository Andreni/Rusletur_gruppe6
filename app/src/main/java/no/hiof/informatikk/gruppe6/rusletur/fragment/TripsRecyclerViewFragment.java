package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.TripsRecycleViewAdapter;

public class TripsRecyclerViewFragment extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public static final String TAG = "RusleTur";
    private int LOCATION_PERMISSION_CODE = 1;
    private ArrayList<String> mItem = new ArrayList<>();
    private ArrayList<String> mUrl = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Inflate view to container
        final View view = inflater.inflate(R.layout.fragment_mainscreen_tripsview, container, false);

        //Recyclerview needs to be set in a layoutmanager.
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Yee added");


        //Added a few demo items for alpha.
        mItem.add("Øyer");
        mItem.add("Halden Byvandring");
        mItem.add("GAPAHUKENE");

        //Adds URL to the demo items.

        mUrl.add("https://www.ut.no/tur/2.4808/gpx/");
        mUrl.add("https://www.ut.no/tur/2.6916/gpx");
        mUrl.add("https://www.ut.no/tur/2.9239/gpx");


        //Set adapter. The adapter takes three parameters (two arrays and context)
        TripsRecycleViewAdapter rAdapter = new TripsRecycleViewAdapter(mItem, mUrl, getActivity());
        recyclerView.setAdapter(rAdapter);

        return view;
    }

    public void fillArrayWithTrips(){

    }


}
