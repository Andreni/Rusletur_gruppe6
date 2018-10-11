package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.informatikk.gruppe6.rusletur.MainActivity;
import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class MainMenuFragment extends Fragment {

    String userEmail;
    Button signOut;
    ConstraintLayout layout;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        Log.d("TESTER", "UTENFOR ONCLICK");

        view.findViewById(R.id.mainScreen_logout_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TESTER", " KLIKK");
                EditText test = view.findViewById(R.id.mainScreen_emialField_editText);
                String email = test.getText().toString();
                Toast.makeText(getActivity(), "hallo " + email, Toast.LENGTH_SHORT).show();
            }
        });

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
                //Log out user from app
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        */

        //return inflater.inflate(R.layout.fragment_mainscreen, container, false);
        return view;



    }

    public void getEmailMethod() {
        EditText et = layout.findViewById(R.id.mainScreen_emialField_editText);
        userEmail = et.getText().toString();
        Log.d("TEST", userEmail);
    }



}
