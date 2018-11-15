package no.hiof.informatikk.gruppe6.rusletur.Spinner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import no.hiof.informatikk.gruppe6.rusletur.R;

public class CustomSpinnerAdapter extends ArrayAdapter<SpinnerData> {
    private Context context;
    private List<SpinnerData> spinnerData;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, List<SpinnerData> spinnerData) {
        super(context, resource, spinnerData);
        this.context = context;
        this.spinnerData = spinnerData;
    }

    private View myCustomSpinnerView(int position, @Nullable View myView, @Nullable ViewGroup parent){
        if(spinnerData.get(position).getName().equals("Valg: ")){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.spinner_layout_listitem, parent, false);

            TextView textView = customView.findViewById(R.id.spinner_textview);

            textView.setText(spinnerData.get(position).getName());

            return customView;
        }else{

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.spinner_layout_listitem, parent, false);

            TextView textView = customView.findViewById(R.id.spinner_textview);
            ImageView imageView = customView.findViewById(R.id.spinner_imageview);

            textView.setText(spinnerData.get(position).getName());
            imageView.setImageResource(spinnerData.get(position).getPicture());

            return customView;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position, convertView, parent);
    }
}
