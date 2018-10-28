package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.Model.Fylke;
import no.hiof.informatikk.gruppe6.rusletur.Model.FylkeList;
import no.hiof.informatikk.gruppe6.rusletur.Model.IdForTur;
import no.hiof.informatikk.gruppe6.rusletur.Model.Kommune;
import no.hiof.informatikk.gruppe6.rusletur.Trips;

/**
 * Class for looking up available ids for User
 * Downloads register.json and selects ids that are relevant for the user.
 * Should be called upon on a separate thread
 */
public class LookUpRegisterNasjonalTurbase {
    private RequestQueue mQueue;
    private Context currentContext;
    private String urlForRegister = "";


    //Constructor for creating downloader
    public LookUpRegisterNasjonalTurbase(Context currentContext) {
        this.currentContext = currentContext;
    }

    /*

     */
    public void createObjectsFromRegister() {
        final FylkeList aRegister = new FylkeList("ListForId");
        //add dummy element to list
        aRegister.addFylkeToList(new Fylke("Valg:"));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlForRegister,
                null, new com.android.volley.Response.Listener<JSONObject>() {
        //Build register File

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayFylke = response.getJSONArray("fylker");
                    for (int i = 0; i < jsonArrayFylke.length(); i++) {
                        JSONObject fylkename = jsonArrayFylke.getJSONObject(i);

                        String aFylkeName = fylkename.getString("fylkenavn");

                        //Create all "Fylke" objects
                        aRegister.addFylkeToList(new Fylke(aFylkeName));

                        //Once the "Fylke object is created add "Kommune"
                        //Get all kommune objects appended to the choosen "Fylke"
                        JSONArray jsonArrayKommune = response.getJSONArray("fylker")
                                .getJSONObject(i)
                                .getJSONArray("kommuner");

                        for (int j = 0; j < jsonArrayKommune.length(); j++){
                            JSONObject kommuneName = jsonArrayKommune.getJSONObject(j);

                            //String to hold the current "Kommune"
                            String aKommuneName = kommuneName.getString("kommunenavn");
                            //Create the "Kommune" object in the corresponding "Fylke"
                            //Add +1 to the increment loop to account for the dummy object
                            FylkeList.getRegisterForFylke().get(i+1)
                                    .addKommuneForFylke(new Kommune(aKommuneName));

                            //Once the "Kommune" object is created add relevant ids
                            //Get all ids objects appended to the choosen "Kommune"
                            JSONArray jsonArrayIds = response.getJSONArray("fylker")
                                    .getJSONObject(i)
                                    .getJSONArray("kommuner").getJSONObject(j)
                                    .getJSONArray("turer");

                            for (int k = 0; j < jsonArrayIds.length(); k++){
                                JSONObject idName = jsonArrayKommune.getJSONObject(k);
                                String aIdName = idName.getString("id");
                                FylkeList.getRegisterForFylke().get(i+1)
                                        .getKommuneArrayList()
                                        .get(j).addIdForKommune(new IdForTur(aIdName));

                            }

                        }



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }
}