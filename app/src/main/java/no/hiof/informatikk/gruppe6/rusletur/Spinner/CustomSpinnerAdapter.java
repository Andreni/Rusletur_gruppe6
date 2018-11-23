package no.hiof.informatikk.gruppe6.rusletur.Spinner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import no.hiof.informatikk.gruppe6.rusletur.R;

/**
 * Adapter to setup fylke spinner in the FindAtrip class.
 * Uses objects from SpinnerData.java as items in the spinner.
 * @author Andreas M.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<SpinnerData> {
    private Context context;
    private List<SpinnerData> spinnerData;

    /**
     * Constructor for the CustomSpinnerAdapter.
     * @param context The current context from the activity you're comming from
     * @param resource The resources you are using.
     * @param spinnerData A list of spinnerData objects
     */
    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, List<SpinnerData> spinnerData) {
        super(context, resource, spinnerData);
        this.context = context;
        this.spinnerData = spinnerData;
    }

    /**
     * Method where you say what variable from SpinnerData goes to what view in the viewGroup
     * @param position What position the item that is being worked on is
     * @param myView The item as a View
     * @param parent The view group. Contains the views that is in the SpinnerData xml
     * @return The view that is beeing showed in the spinner.
     */
    private View myCustomSpinnerView(int position, @Nullable View myView, @Nullable ViewGroup parent){
        if(spinnerData.get(position).getPicture() == 0){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.spinner_layout_listitem, parent, false);

            ConstraintLayout constraintLayout = customView.findViewById(R.id.customspinner_parent);
            if(position % 2 == 0) {
                constraintLayout.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("listitem_border_bottom_colorprimary", "drawable", context.getPackageName())));
            }else{
                constraintLayout.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("listitem_border_bottom_colorlight", "drawable", context.getPackageName())));
            }

            customView.findViewById(R.id.spinner_imageview).setVisibility(View.GONE);
            TextView textView = customView.findViewById(R.id.spinner_textview);

            textView.setText(spinnerData.get(position).getName());

            return customView;
        }else{
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.spinner_layout_listitem, parent, false);

            ConstraintLayout constraintLayout = customView.findViewById(R.id.customspinner_parent);
            if(position % 2 == 0) {
                constraintLayout.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("listitem_border_bottom_colorprimary", "drawable", context.getPackageName())));
            }else{
                constraintLayout.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("listitem_border_bottom_colorlight", "drawable", context.getPackageName())));
            }

            TextView textView = customView.findViewById(R.id.spinner_textview);
            ImageView imageView = customView.findViewById(R.id.spinner_imageview);

            textView.setText(spinnerData.get(position).getName());
            imageView.setImageResource(spinnerData.get(position).getPicture());

            return customView;
        }
    }

    /**
     * Creates the view that is being showed.
     * @param position What position the item that is being worked on is
     * @param convertView
     * @param parent The view group. Contains the views that is in the SpinnerData xml
     * @return Returs the view that is being shoed in the spinner.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position, convertView, parent);
    }

    /**
     * Defines how it's going to be showed in the dropdown form
     * @param position What position the item that is being worked on is
     * @param convertView
     * @param parent The view group. Contains the views that is in the SpinnerData xml
     * @return Returs the view that is being shoed in the spinner.
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position, convertView, parent);
    }
}
