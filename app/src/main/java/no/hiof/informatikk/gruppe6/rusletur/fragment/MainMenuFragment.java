package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class MainMenuFragment extends Fragment {

    String userEmail;
    Button signOut;
    ConstraintLayout layout;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public static final String TAG = "RusleTur";
    private int LOCATION_PERMISSION_CODE = 1;
    private ArrayList<String> mItem = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Inflate view. Basically "set" view.
        final View view = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        Log.d("TESTER", "UTENFOR ONCLICK");



        //Showcase Method: How to find items in fragment XML.
        view.findViewById(R.id.mainScreen_logout_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        * This is how you call methods from fragment to activity. In most cases it's easier to create the relevant methods here,
        * except in cases where you want to create a method which more than one fragment needs.
         */
        // ((MainScreen)getActivity()).showcaseMethod();




        return view;

    }

    public void showcaseMethodTwo(){
        Log.d("MainMenuFragment", "Called method OK");
    }



}
