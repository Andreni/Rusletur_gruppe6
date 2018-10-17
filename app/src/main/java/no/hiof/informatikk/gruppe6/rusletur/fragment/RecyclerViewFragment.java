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
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.RecycleViewAdapter;

public class RecyclerViewFragment extends Fragment {


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

        final View view = inflater.inflate(R.layout.activity_main_recycleview, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Yee added");



        mItem.add("Demo Tur 1");
        mItem.add("Demo Tur 2");
        mItem.add("Demo Tur 3");

        mUrl.add("https://www.ut.no/tur/2.15173/gpx");
        mUrl.add("https://www.ut.no/tur/2.6916/gpx");
        mUrl.add("https://www.ut.no/tur/2.9239/gpx");


        RecycleViewAdapter rAdapter = new RecycleViewAdapter(mItem, mUrl, getActivity());

        recyclerView.setAdapter(rAdapter);

        return view;
    }


}
