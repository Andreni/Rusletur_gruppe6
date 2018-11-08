package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.User.User;

public class NewUserFragment extends Fragment {

    private EditText userName;
    private EditText firstName;
    private EditText lastName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_newuser, container, false);

        userName = view.findViewById(R.id.fragment_newuser_username);
        firstName = view.findViewById(R.id.fragment_newuser_firstname);
        lastName = view.findViewById(R.id.fragment_newuser_lastname);

        view.findViewById(R.id.fragment_newuser_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.setAll(userName.toString(), firstName.toString(), lastName.toString());
                Toast.makeText(getActivity(), "Velkommen", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
            }
        });

        return view;
    }
}
