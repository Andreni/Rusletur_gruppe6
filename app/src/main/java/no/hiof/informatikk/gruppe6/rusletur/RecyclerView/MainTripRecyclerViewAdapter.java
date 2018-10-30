package no.hiof.informatikk.gruppe6.rusletur.RecyclerView;

import android.content.Context;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;

public class MainTripRecyclerViewAdapter extends RecyclerView<MainTripRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Trip> mItem;
    Context mContext;

    public MainTripRecyclerViewAdapter(Context kont, ArrayList<Trip> turer){
        this.mItem = turer;
        this.mContext = kont;
    }


}
