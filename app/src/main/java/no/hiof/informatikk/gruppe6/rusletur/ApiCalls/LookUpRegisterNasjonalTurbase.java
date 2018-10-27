package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.Trips;

/**
 * Class for looking up available ids for User
 * Downloads register.json and selects ids that are relevant for the user.
 */
public class LookUpRegisterNasjonalTurbase extends AsyncTask<String,Void,String> {

    ArrayList<String> fylkeOversikt = new ArrayList<>();
    ArrayList<String> kommuneOversikt = new ArrayList<>();
    ArrayList<String> idsOversikt = new ArrayList<>();
    RequestQueue mQueue;
    Context currentContext;


    public LookUpRegisterNasjonalTurbase(Context currentContext) {
        this.currentContext = currentContext;
    }

    String urlForRegister = "";
    @Override
    protected String doInBackground(String... strings) {
        //Populate arrays

        mQueue = Volley.newRequestQueue(currentContext);



        return null;
    }

    public ArrayList<String> getFylkeOversikt() {
        return fylkeOversikt;
    }

    public void setFylkeOversikt(ArrayList<String> fylkeOversikt) {
        this.fylkeOversikt = fylkeOversikt;
    }

    public ArrayList<String> getKommuneOversikt() {
        return kommuneOversikt;
    }

    public void setKommuneOversikt(ArrayList<String> kommuneOversikt) {
        this.kommuneOversikt = kommuneOversikt;
    }

    public ArrayList<String> getIdsOversikt() {
        return idsOversikt;
    }

    public void setIdsOversikt(ArrayList<String> idsOversikt) {
        this.idsOversikt = idsOversikt;
    }
}
