package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.User.User;

public class ProfilePageFragment extends Fragment {

    private FirebaseUser mUser;
    private String test = "Hei";
    private TextView username;
    private TextView usermail;
    private TextView fullName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profilescreen, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        username = (TextView) view.findViewById(R.id.fragment_profile_username);
        usermail = (TextView) view.findViewById(R.id.fragment_profile_usermail);
        fullName = (TextView) view.findViewById(R.id.fragment_profile_firstandlastname);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        username.setText(MainScreen.mainscreenUsername);
        usermail.setText(mUser.getEmail());
        fullName.setText(MainScreen.mainscreenFirstname + " " + MainScreen.mainscreenLastname);


    }
}
