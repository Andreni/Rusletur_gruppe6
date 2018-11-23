package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import no.hiof.informatikk.gruppe6.rusletur.FindAtrip;
import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocalStorageTrips;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.UserUtility;

/**
 * Fragment that is loaded when the user logs on the application.
 * From here the user selects one of the presented options
 * @author Andreas N.
 * @author Bjørnar P.
 * @version 1.1
 */
public class MainScreen_MainMenu extends Fragment {


    public MainScreen_MainMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_main_screen__main_menu, container, false);


        view.findViewById(R.id.mainScreenMainMenufragment_findAtrip_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserUtility.checkIfUserHasGPSEnabled(getContext())) {
                    Toast.makeText(getContext(), "Du må skru på GPS", Toast.LENGTH_SHORT).show();
                } else {
                    if (UserUtility.checkIfUserHasMobileNetworkEnabled(getActivity())) {
                        startActivity(new Intent(getActivity(), FindAtrip.class));
                    } else {
                        Toast.makeText(getActivity(), "Du mangler internett tilkobling", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        view.findViewById(R.id.mainScreenMainMenufragment_makeAtrip_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtility.checkIfUserHasGPSEnabled(getActivity())) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecordFragment()).commit();
                }else{
                    Toast.makeText(getActivity(),"Du må skru på GPS",Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.mainScreenMainMenu_goToLocalStorage_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),LocalStorageTrips.class));
            }
        });
        return view;

    }
}



