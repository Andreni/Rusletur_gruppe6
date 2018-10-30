package no.hiof.informatikk.gruppe6.rusletur.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class MainTripRecyclerViewAdapter extends RecyclerView.Adapter<MainTripRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Trip> mItem;
    Context mContext;

    public MainTripRecyclerViewAdapter(Context kont, ArrayList<Trip> turer){
        this.mItem = turer;
        this.mContext = kont;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem_tripsview, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.itemNavn.setText(mItem.get(position).getNavn());
        holder.itemTidsbruk.setText(mItem.get(position).getTidsbruk());
        holder.itemGradering.setText(mItem.get(position).getGradering());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mItem.get(position).getNavn() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout parentLayout;
        TextView itemNavn;
        TextView itemTidsbruk;
        TextView itemGradering;
        public ViewHolder(View itemView){
            super(itemView);

            itemNavn = itemView.findViewById(R.id.itemNavn);
            itemTidsbruk = itemView.findViewById(R.id.itemTidsbruk);
            itemGradering = itemView.findViewById(R.id.itemGradering);
            parentLayout = itemView.findViewById(R.id.parentLayoutForListItem);
        }

    }



}
