package no.hiof.informatikk.gruppe6.rusletur.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class MainTripRecyclerViewAdapter extends RecyclerView<MainTripRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Trip> mItem;
    Context mContext;

    public MainTripRecyclerViewAdapter(Context kont, ArrayList<Trip> turer){
        this.mItem = turer;
        this.mContext = kont;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem_tripsview, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    

}
