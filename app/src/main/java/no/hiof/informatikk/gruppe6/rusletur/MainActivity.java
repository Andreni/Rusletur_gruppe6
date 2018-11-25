package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import no.hiof.informatikk.gruppe6.rusletur.User.CreateNewUser;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Sends a method call to see if the application has the needed permissions for
 * optimal function.(LOCATION,STORAGE,CAMERA)
 * If these requirements are not granted, the user will not be able to log in.
 *
 * If no user is logged in, this is the first activity the user sees.
 * By checking if a user is logged in via firebase,
 * it is determind if the activity should be launched or not.
 *  @author Andreas N.
 *  @author Andreas M.
 *  @version 1.0
 *
 */
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    EditText edPass;
    EditText edEmail;
    ConstraintLayout loginPage;
    ConstraintLayout registerPage;
    EditText inputEmail;
    EditText inputPassword;
    EditText secondInputPassword;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static boolean sendtToRegistrationPage = false;
    public static boolean completedRegistrationPage = true;

    private boolean newUser;

    //Global variable for permission:

    private static final int MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE = 1;

    private String[] neededPermissions = { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();




        //First lets see if we have the necessary permissions
        checkPermissions();
        if (checkPermissions()){
            //Check if there is an active session with firebase and user is logged in:
            if(mUser!=null){
                startActivity(new Intent(MainActivity.this, MainScreen.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        }

        //If no user is logged in, show login screen:
        //Initialize elements:
        edEmail = findViewById(R.id.mainA_loginEmail_editText);
        edPass = findViewById(R.id.mainA_loginPass_editText);
        loginPage = findViewById(R.id.mainA_loginLayout_cLayoutLogin);
        registerPage = findViewById(R.id.mainA_registrerLayout_cLayoutLogin);
        inputEmail = findViewById(R.id.mainA_registrerEmail_editText);
        inputPassword = findViewById(R.id.mainA_registrerPass_editText);
        secondInputPassword = findViewById(R.id.mainA_registrerPassConfirm_editText);
        registerPage = findViewById(R.id.mainA_registrerLayout_cLayoutLogin);

         new Handler().post(new Runnable() {
            @Override
            public void run() {
                DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("user");
                /** Adds a listener that will loop through all dataChange */
                zonesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            /** For each trip in Firebase-Trip */
                            HashMap<String, String> hm = new HashMap<>();
                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                                Log.d("MainActivity", "Snapshot: " + zoneSnapshot.getKey());
                                if (zoneSnapshot.getKey().equals(mUser.getUid())) {
                                    for (DataSnapshot x : zoneSnapshot.getChildren()) {
                                        hm.put(x.getKey(), x.getValue().toString());
                                    }
                                }

                            }
                            if (hm.get("username") == null || hm.get("firstname") == null || hm.get("lastname") == null) {
                                if(sendtToRegistrationPage == false || completedRegistrationPage == false) {
                                    Log.e("MainAcitivty", "DID NOT FOUND USER INFORMATION");
                                    Log.d("SendingToIntent", "...");
                                    sendtToRegistrationPage = true;
                                    completedRegistrationPage = false;
                                    Intent newUserIntent = new Intent(MainActivity.this, CreateNewUser.class);
                                    startActivity(newUserIntent);
                                }

                            }
                        }
                        catch (NullPointerException e) {
                            Log.i("MainActivity","User not logged in");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



    }

    /**
     * Method for prompting the user to give consent on the the required permissions
     * @return Gives back a boolean to verify if the user has granted the required permissions
     */
    private boolean checkPermissions(){
        boolean isPermissionsGranted = false;

        if (EasyPermissions.hasPermissions(this,neededPermissions)){
            isPermissionsGranted = true;
        }else{
            EasyPermissions.requestPermissions(this, "Appen trenger tilattelser til å bruke \n" +
                    "GPS og lagring",MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE,neededPermissions);

            }

        return isPermissionsGranted;
    }


    /**
     * After the result of the request has come through
     * @param requestCode What requestcode the permission has
     * @param permissions String array with the permissions
     * @param grantResults Integer array with the result of the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults);
        //Statement to check if we have the needed permissions:
        if (EasyPermissions.hasPermissions(this,neededPermissions)){
            //Proceed with current activity
        }else{
            EasyPermissions.requestPermissions(this, "For at applikasjonen skal fungere optimalt \n" +
                    "trenger den tilattelse til å bruke GPS og Lagring  ",123,neededPermissions);
        }
    }


    /**
     * Attempt to login the user
     * @param view a button push
     */
    public void loginUser(View view){
        //TODO Animate login process
        checkPermissions();
        if(checkForValidUserInput(1)){
            //Login in user with input values:
            mAuth.signInWithEmailAndPassword(edEmail.getText().toString()
                    ,edPass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Send user to second screen
                                if(checkPermissions()){
                                    if(newUser){
                                        Intent newUserIntent = new Intent(MainActivity.this, CreateNewUser.class);
                                        startActivity(newUserIntent);
                                    }
                                    else {
                                        Intent loginIntent = new Intent(MainActivity.this, MainScreen.class);
                                        startActivity(loginIntent);
                                        newUser = false; //If user returns to login-screen.
                                    }

                                }else{
                                    writeMessageToUser("Du får ikke logget inn uten å ha gitt tilattelser");

                                }

                            }else{
                                //If the login process is interrupted: warn user:
                                writeMessageToUser(task.getException().toString());
                            }
                        }
                    });
        }
    }


    /**
     * Method for switching displays and showing the user a register page
     * @param view a button push
     */
    public void registerUser(View view){
        //Hide login page
        if(checkForValidUserInput(2)){
            mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString(),
                    inputPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            newUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(task.isSuccessful()){
                                writeMessageToUser("Du er registrert :)");
                                registerPage.setVisibility(View.INVISIBLE);
                                loginPage.setVisibility(View.VISIBLE);
                                edEmail.setText(inputEmail.getText());
                                if(sendtToRegistrationPage == true) {
                                    Intent loginIntent = new Intent(MainActivity.this, MainScreen.class);
                                    startActivity(loginIntent);
                                }
                            }else{
                                writeMessageToUser(task.getException().toString());
                            }
                        }
                    });
        }

    }

    /**
     * When the "Register" button is pressed
     * @param view The button as a View
     */
    public void goToRegistration(View view){
        loginPage.setVisibility(View.INVISIBLE);
        registerPage.setVisibility(View.VISIBLE);
    }

    /**
     * When the cancle registre button is pressed
     * @param view The button as a View
     */
    public void cancleRegistration(View view) {
        loginPage.setVisibility(View.VISIBLE);
        registerPage.setVisibility(View.INVISIBLE);
    }


    //Check if field values entered are correct:
    //Option 1 checks loginpage for valid input
    //Option 2 checks registerpage for valid input

    /**
     * Checks the input fields for valid input.
     * @param option What screen the user is comming from. 1 for loginpage and 2 for registerpage
     * @return True or False. Based on if the input is correct or not
     */
    private boolean checkForValidUserInput(int option){

        if(option==1) {
            if ((!edEmail.getText().toString().isEmpty() && edEmail.toString().length() > 0)
                    && (edPass.getText().toString() != "")) {
                if (edPass.getText().toString().length() > 5) {
                    return true;
                } else {

                    writeMessageToUser("Passordet er for kort.");
                    return false;
                }
            } else {

                writeMessageToUser("Kontroller hva du har skrevet inn");
                return false;
            }
        }else if(option == 2){
            if ((!inputEmail.getText().toString().isEmpty() && inputEmail.toString().length() > 0)){
                if((!inputPassword.getText().toString().isEmpty())
                        &&inputPassword.getText().toString().equals(secondInputPassword.getText().toString())){
                    return true;
                }
                else {
                    writeMessageToUser("Passord matcher ikke");
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

    /**
     * Writes a message for the user that is being Toasted
     * @param messageToUser The message
     */
    private void writeMessageToUser(String messageToUser){
        Toast.makeText(this,messageToUser,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {


    }

    /**
     * If the user denied the permission regquest
     * @param requestCode The request code for the permission
     * @param perms List with parameters
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //Display dialog to user
            new AppSettingsDialog.Builder(this).build().show();
            //Send the user to the settings page to grant permissions
        }
    }

    //When the user returns from the settings page
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){

        }
    }
}
