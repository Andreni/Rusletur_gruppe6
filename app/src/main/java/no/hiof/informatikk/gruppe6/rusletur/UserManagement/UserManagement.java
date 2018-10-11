package no.hiof.informatikk.gruppe6.rusletur.UserManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.R;

public class UserManagement extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseAuth.AuthStateListener mAuthListener;

    EditText userNameInput;
    EditText realNameInput;
    EditText phoneNumberInput;

    Button registrerButton;
    Button cancelButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_personal_info);

        //Initialize view stuff
        userNameInput = findViewById(R.id.personal_info_username);
        realNameInput = findViewById(R.id.personal_info_name);
        phoneNumberInput = findViewById(R.id.personal_info_phonenr);
        registrerButton = findViewById(R.id.personal_info_reg_button);
        cancelButton = findViewById(R.id.personal_info_cancel_button);

        registrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Få detta te å fungere. AuthStateChanged æ tydlevis homofilt.

                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if(user!=null){
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    //userNameInput.getText().toString()) <-- burde værra denna
                                    .setDisplayName("Test").build();
                            user.updateProfile(profileUpdates);

                        }
                    }
                };
                */

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName("Jane Q. User")
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserManagement.this,"hUZZAH",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                /*
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String name = user.getDisplayName();

                Toast.makeText(UserManagement.this,name,Toast.LENGTH_SHORT).show();
                */
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserManagement.this, MainScreen.class);
                startActivity(intent);
            }
        });








    }
}
