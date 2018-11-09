package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.MainTripRecyclerViewAdapter;
import no.hiof.informatikk.gruppe6.rusletur.Trips;

public class LocalTripRecyclerView extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_local_trip_recyclerview, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.local_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        MainTripRecyclerViewAdapter mainTripRecyclerViewAdapter = new MainTripRecyclerViewAdapter(getActivity(), Trip.trips);
        recyclerView.setAdapter(mainTripRecyclerViewAdapter);

        return view;
    }
    
}
