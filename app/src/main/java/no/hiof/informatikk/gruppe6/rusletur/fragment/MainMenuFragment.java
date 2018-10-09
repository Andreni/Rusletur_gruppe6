package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class MainMenuFragment extends Fragment {

    EditText userEmail;
    Button signOut;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Handle view

        return inflater.inflate(R.layout.fragment_mainscreen, container, false);



    }



}
