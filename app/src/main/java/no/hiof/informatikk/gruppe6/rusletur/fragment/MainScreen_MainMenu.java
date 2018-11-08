package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.LocalStorageTrips;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.Trips;

/**
 * Fragment that is loaded when the user logs on the application.
 * From here the user selects one of the presented options
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
                startActivity(new Intent(getContext(),Trips.class));
            }
        });

        view.findViewById(R.id.mainScreenMainMenufragment_makeAtrip_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
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



