package no.hiof.informatikk.gruppe6.rusletur.RecyclerView;

import android.content.Context;
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

import no.hiof.informatikk.gruppe6.rusletur.R;

public class TripFragmentRecyclerViewAdapter extends RecyclerView.Adapter<TripFragmentRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> mItem = new ArrayList<>();
    Context mContext;


    public TripFragmentRecyclerViewAdapter(ArrayList<String> mItem, Context mContext) {
        this.mItem = mItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem_tripfragment, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.item.setText(mItem.get(i));

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mItem.get(i), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView item;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            item = itemView.findViewById(R.id.item);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }

    }
}
