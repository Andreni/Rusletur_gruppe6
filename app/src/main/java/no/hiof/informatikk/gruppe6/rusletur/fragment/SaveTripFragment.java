package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class SaveTripFragment extends Fragment{
    private String selectedDifficulty;
    private EditText nameInput;
    private EditText descInput;
    private RadioGroup difficultyRadioGroup;
    private Button saveTripButton;
    private boolean checked;
    private String nameinput;
    private String description;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_savetrip, container, false);


        /*
        * This is a pretty simple XML which just displays a name input, description input
        * and RadioButtons for selecting difficulty.
         */

        nameInput = view.findViewById(R.id.savetrip_nameOfTripInput);
        descInput = view.findViewById(R.id.savetrip_descriptionInput);

        difficultyRadioGroup = (RadioGroup) view.findViewById(R.id.savetrip_radioGroup);

        difficultyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.savetrip_easySelected:
                        selectedDifficulty = "Lett";
                        break;
                    case R.id.savetrip_mediumSelected:
                        selectedDifficulty = "Middels";
                        break;
                    case R.id.savetrip_hardSelected:
                        selectedDifficulty = "Vanskelig";
                        break;
                }
            }
        });


        saveTripButton = view.findViewById(R.id.savetrip_saveTripButton);
        saveTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInput != null && descInput != null && selectedDifficulty != null) {
                    /*
                    * Check if input is not null lol.
                    * If everything checks out, send input from name, description and radiogroup to
                    * handleStorageOfTrips in MainScreen, and switch from this fragment back to the
                    * MainMenuFragment. It happens lightning quick because Fragments are fucking rad bro.
                     */
                    nameinput = nameInput.getText().toString();
                    description = descInput.getText().toString();

                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Lagring")
                            .setMessage("Vil du dele turen?")
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((MainScreen) getActivity()).handleStorageOfTrips(nameinput, description, selectedDifficulty);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();

                                }
                            })
                            .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((MainScreen) getActivity()).handleOfflineStorageOfTrips(nameinput, description, selectedDifficulty);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
                                }
                            })
                            .show();

                } else {
                    Toast.makeText(getActivity(), "Er alle feltene fylt ut?", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}


