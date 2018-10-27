package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import no.hiof.informatikk.gruppe6.rusletur.ApiCalls.LookUpRegisterNasjonalTurbase;

public class Trips extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        //Find avalible *Fylke* from JSON file
        //Send call to downloader.
        //Populate fylke oversikt.

        LookUpRegisterNasjonalTurbase lookUpRegisterNasjonalTurbase = new LookUpRegisterNasjonalTurbase(this);
        lookUpRegisterNasjonalTurbase.execute("Go");

        //Populate spinner that contains the "FylkeOversikt"
        for(int i=0;i<lookUpRegisterNasjonalTurbase.getFylkeOversikt().size();i++){

            //Cycle through all Fylker
            lookUpRegisterNasjonalTurbase.getFylkeOversikt().get(i);
            //Add to spinner list item

        }

        //onselect listner for fylke spinner.

        String selectedFylke = "";
        lookUpRegisterNasjonalTurbase.execute(selectedFylke);
        //Send new call
        //When selection is made, enable kommune spinner.
    }
}
