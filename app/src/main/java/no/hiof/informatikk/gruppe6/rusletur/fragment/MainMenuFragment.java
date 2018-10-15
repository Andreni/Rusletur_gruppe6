package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
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



        final View view = inflater.inflate(R.layout.activity_main_recycleview, container, false);
        Log.d("TESTER", "UTENFOR ONCLICK");



        /*
        view.findViewById(R.id.mainScreen_logout_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log out user from app
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        */

         /*
        userEmail = getView().findViewById(R.id.mainScreen_emialField_editText);
        signOut = getView().findViewById(R.id.mainScreen_logout_Button);
        //TODO Remove Activity and class when done testing
        mAuth = FirebaseAuth.getInstance();
        //Get user currently signed in
        mUser = mAuth.getCurrentUser();
        //userEmail.setText(mUser.getEmail());
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */

        //return inflater.inflate(R.layout.fragment_mainscreen, container, false);

        initArraysForRecyclerView();

        return view;



    }


    private void initArraysForRecyclerView(){
        mItem.add("Kart");
        mItem.add("Start egen tur");
        mItem.add("Min posisjon");
        mItem.add("Innstillinger");
        mItem.add("4");
        mItem.add("g");
        mItem.add("3");
        mItem.add("q");
        mItem.add("g");
        mItem.add("l");
        mItem.add("v");
        mItem.add(" ");
        mItem.add("j");



    }




}
