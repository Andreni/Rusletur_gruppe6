package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import no.hiof.informatikk.gruppe6.rusletur.R;

public class SaveTripFragment extends Fragment {
    private int selectedDifficulty = 0;
    private EditText nameInput;
    private EditText descInput;
    private RadioGroup difficultyRadioButton;
    private Button saveTripButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_savetrip, container, false);

        nameInput = view.findViewById(R.id.savetrip_nameOfTripInput);
        descInput = view.findViewById(R.id.savetrip_descriptionInput);


        difficultyRadioButton = view.findViewById(R.id.savetrip_radioGroup);
        difficultyRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.savetrip_easySelected:
                        selectedDifficulty = 1;
                        break;
                    case R.id.savetrip_mediumSelected:
                        selectedDifficulty = 2;
                        break;
                    case R.id.savetrip_hardSelected:
                        selectedDifficulty = 3;
                }
            }
        });

        saveTripButton = view.findViewById(R.id.savetrip_saveTripButton);
        saveTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameInput != null && descInput != null && selectedDifficulty != 0){
                    //Save data. How to?
                }
                else {
                    Toast.makeText(getActivity(), "Pass på at alle felt er utfylt, din jævla mongo", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}
