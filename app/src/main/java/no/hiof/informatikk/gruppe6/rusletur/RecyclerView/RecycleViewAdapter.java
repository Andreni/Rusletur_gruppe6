package no.hiof.informatikk.gruppe6.rusletur.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.MapsActivity;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.Viewholder>{

    private ArrayList<String> mItem = new ArrayList<>();
    private ArrayList<String> mUrl = new ArrayList<>();
    private Context context;

    private String selectedIndex;


    public RecycleViewAdapter (ArrayList<String> mItem, ArrayList<String> mUrl, Context context) {
        this.context = context;
        this.mItem = mItem;
        this.mUrl = mUrl;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem_mainscreen, viewGroup, false);
        Viewholder viewholder = new Viewholder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, final int i) {
        viewholder.item.setText(mItem.get(i));
        viewholder.item.setTag(mUrl.get(i));
        Log.d("CLICKTEST", "BINDVIEWHOLDER");





        //Click method for the buttons..
        viewholder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Button pressed " + mItem.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                //Recieves the URL from the pressed button tag-data.
                String urlLink = v.getTag().toString();
                intent.putExtra("url", urlLink);
                v.getContext().startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mItem.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        TextView item;
        RelativeLayout parentLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
