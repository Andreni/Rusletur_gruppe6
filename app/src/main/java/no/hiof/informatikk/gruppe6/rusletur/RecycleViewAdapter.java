package no.hiof.informatikk.gruppe6.rusletur;

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

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.Viewholder>{

    private ArrayList<String> mItem = new ArrayList<>();
    Context mContext;


    public RecycleViewAdapter (ArrayList<String> mItem, Context mContext) {
        this.mItem = mItem;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem_mainscreen, viewGroup, false);
        Viewholder viewholder = new Viewholder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, final int i) {
        viewholder.item.setText(mItem.get(i));

        viewholder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mItem.get(i), Toast.LENGTH_SHORT).show();
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
