package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edPass;
    EditText edEmail;
    ConstraintLayout loginPage;
    ConstraintLayout registerPage;
    EditText inputEmail;
    EditText inputPassword;
    EditText secondInputPassword;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //Initialize elements:
        edEmail = findViewById(R.id.mainA_loginEmail_editText);
        edPass = findViewById(R.id.mainA_loginPass_editText);
        loginPage = findViewById(R.id.mainA_loginLayout_cLayoutLogin);
        registerPage = findViewById(R.id.mainA_registrerLayout_cLayoutLogin);
        inputEmail = findViewById(R.id.mainA_registrerEmail_editText);
        inputPassword = findViewById(R.id.mainA_registrerPass_editText);
        secondInputPassword = findViewById(R.id.mainA_registrerPassConfirm_editText);
        registerPage = findViewById(R.id.mainA_registrerLayout_cLayoutLogin);


        //TODO 1.1 Make simple login screen:
        //TODO 1.2 Make registration page:
        //TODO 1.3 If statement to check if user is logged in
        //TODO 1.4 Make horizontal layout (Non priority)


    }

    //Click from button
    public void loginUser(View view){
        //TODO Animate login process

        if(checkForValidUserInput(1)){
            mAuth.signInWithEmailAndPassword(edEmail.getText().toString()
                    ,edPass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Send user to second screen
                                startActivity(new Intent(MainActivity.this,MainScreen.class));
                            }else{
                                //If the login process is interrupted: warn user:
                                writeMessageToUser(task.getException().toString());
                            }
                        }
                    });
        }
    }

    //Click from button
    public void registerUser(View view){
        //Hide login page
        if(checkForValidUserInput(2)){
            mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString(),
                    inputPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                writeMessageToUser("Du er registrert :)");
                                //TODO Go back to login view
                                //TODO Add email to email field in login view
                            }else{
                                writeMessageToUser(task.getException().toString());
                            }
                        }
                    });
        }

    }
    public void goToRegistration(View view){
        loginPage.setVisibility(View.INVISIBLE);
        registerPage.setVisibility(View.VISIBLE);
    }
    public void cancleRegistration(View view) {
        loginPage.setVisibility(View.VISIBLE);
        registerPage.setVisibility(View.INVISIBLE);
    }

    //Check if field values entered are correct:
    //Option 1 checks loginpage for valid input
    //Option 2 checks registerpage for valid input
    private boolean checkForValidUserInput(int option){

        if(option==1) {
            if ((!edEmail.getText().toString().isEmpty() && edEmail.toString().length() > 0)
                    && (edPass.getText().toString() != "")) {
                if (edPass.getText().toString().length() > 5) {
                    return true;
                } else {
                    //TODO Write static variable
                    writeMessageToUser("Passordet er for kort.");
                    return false;
                }
            } else {
                //TODO Write static variable
                writeMessageToUser("Kontroller hva du har skrevet inn");
                return false;
            }
        }else if(option == 2){
            if ((!inputEmail.getText().toString().isEmpty() && inputEmail.toString().length() > 0)){
                if((!inputPassword.getText().toString().isEmpty())
                        &&inputPassword.getText().toString().equals(secondInputPassword.getText().toString())){
                    return true;
                }
            }else{
                writeMessageToUser("Ugyldig input");
                return false;
            }


        }

        //Input is faulty!
        return false;
    }

    //Send message to user:
    private void writeMessageToUser(String messageToUser){
        Toast.makeText(this,messageToUser.toString(),Toast.LENGTH_SHORT).show();
    }

}
