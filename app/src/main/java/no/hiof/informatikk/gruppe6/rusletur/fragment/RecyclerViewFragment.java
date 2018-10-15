package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.RecycleViewAdapter;

public class RecyclerViewFragment extends Fragment {


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

        initRecyclerView();

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        RecycleViewAdapter adapter = new RecycleViewAdapter(mItem, getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }




}
