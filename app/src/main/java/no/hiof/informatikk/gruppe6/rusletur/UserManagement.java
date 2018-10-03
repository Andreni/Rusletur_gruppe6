package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserManagement extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    EditText userNameInput;
    EditText realNameInput;
    EditText phoneNumberInput;

    Button registrerButton;
    Button cancelButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_personal_info);

        //Fetch user
        mUser = mAuth.getInstance().getCurrentUser();

        //Initialize view stuff
        userNameInput = findViewById(R.id.personal_info_username);
        realNameInput = findViewById(R.id.personal_info_name);
        phoneNumberInput = findViewById(R.id.personal_info_phonenr);
        registrerButton = findViewById(R.id.personal_info_reg_button);
        cancelButton = findViewById(R.id.personal_info_cancel_button);

        registrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserManagement.this,"Hey you Pikachu",Toast.LENGTH_SHORT).show();
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
