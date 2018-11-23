package no.hiof.informatikk.gruppe6.rusletur.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.DisplayAtrip;
import no.hiof.informatikk.gruppe6.rusletur.Model.Trip;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.UserUtility;

/**
 * Adapter for displaying passed trip objects
 * @author Andreas M.
 * @author Andreas N.
 * @author Bjørnar P.
 * @version 1.1
 */
public class MainTripRecyclerViewAdapter extends RecyclerView.Adapter<MainTripRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Trip> mItem;
    Context mContext;
    final String TAG = "RecyclerViewAdapterLogT";

    /**
     * Constructor for
     * @param kont The current context from the activity the recycler view is used
     * @param turer ArrayList with {@link Trip} objects
     */
    public MainTripRecyclerViewAdapter(Context kont, ArrayList<Trip> turer){
        this.mItem = turer;
        this.mContext = kont;
        Log.d(TAG, "MainTripRecyclerViewAdapter: top " + turer.size());
    }

    /**
     * Method for creating the ViewHolder object
     * @param viewGroup The viewGroup that contians the views in the xml
     * @param i Counter that tells what position in the recycler view the item is
     * @return Returns a ViewHolder object
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        Log.d(TAG, "onCreateViewHolder:");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem_tripsview, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    /**
     * Bind the ViewHolder with the data from the arrayList with {@link Trip} objects
     * @param holder The ViewHolder object that is created in {@link #onCreateViewHolder(ViewGroup, int)}
     * @param position What position the item is in the recycler view
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: created");
        if(mItem.get(0) != null){
            if(position % 2 == 0){
                holder.listitemParent.setBackgroundColor(Color.rgb(250,250,250));
            }else{
                holder.listitemParent.setBackgroundColor(Color.rgb(240,240,240));
            }
            holder.itemNavn.setText(mItem.get(position).getNavn());
            if(mItem.get(position).getGoogleDirections() != null) {
                holder.itemAvstandKm.setText(mItem.get(position).getGoogleDirections().getDistance());
            } else {
                holder.itemAvstandKm.setText("? km");
            }
            holder.itemTidsbruk.setText(mItem.get(position).getTidsbruk());
            holder.itemGradering.setText(mItem.get(position).getGradering());
        }

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!UserUtility.checkIfUserHasGPSEnabled(mContext)) {
                        Toast.makeText(mContext, "Du må skru på GPS", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(mContext, mItem.get(position).getNavn() + " clicked", Toast.LENGTH_SHORT).show();
                        // using context and next component class to create intent
                        Intent intent = new Intent(mContext, DisplayAtrip.class);
                        // using putExtra(String key, Parcelable value) method
                        intent.putExtra("object", mItem.get(position));
                        intent.putExtra("sender", mContext.getClass().getSimpleName());
                        // If the sending class is LocalStorage, add the rowId to the intent

                        mContext.startActivity(intent);
                    }
                }
            });




    }

    /**
     * Method that gets the total amount of items in the ArrayList
     * @return ArrayList size
     */
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + mItem.size());
        return mItem.size();
    }

    /**
     * Innerclass that defines the ViewHolder.
     * Needed to create a group of the views from the xml
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout parentLayout;
        TextView itemNavn;
        TextView itemTidsbruk;
        TextView itemAvstandKm;
        TextView itemGradering;
        LinearLayout listitemParent;

        /**
         * Constructor for the ViewHolder
         * @param itemView A view from the XML
         */
        public ViewHolder(View itemView){
            super(itemView);

            listitemParent = itemView.findViewById(R.id.listitem_tripsview_parent);
            itemNavn = itemView.findViewById(R.id.itemNavn);
            itemAvstandKm = itemView.findViewById(R.id.itemAvstandKm);
            itemTidsbruk = itemView.findViewById(R.id.itemTidsbruk);
            itemGradering = itemView.findViewById(R.id.itemGradering);
            parentLayout = itemView.findViewById(R.id.parentLayoutForListItem);
        }

    }



}
