package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.informatikk.gruppe6.rusletur.R;

public class ProfileFragment extends Fragment {

    private TextView userMail;
    private FirebaseUser mUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_profilescreen, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //Get info from user and paste it to profile page.

        userMail = view.findViewById(R.id.fragment_profile_usermail);
        userMail.setText(mUser.getEmail());


        return view;


    }
}
