package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import no.hiof.informatikk.gruppe6.rusletur.Model.Fylke;
import no.hiof.informatikk.gruppe6.rusletur.Model.FylkeList;
import no.hiof.informatikk.gruppe6.rusletur.Model.IdForTur;
import no.hiof.informatikk.gruppe6.rusletur.Model.Kommune;

/**
 * Class for looking up available ids for User
 * Downloads register.json and selects ids that are relevant for the user.
 * Should be called upon on a separate thread
 */
public class LookUpRegisterNasjonalTurbase {

    private RequestQueue mQueue;
    private Context currentContext;
    private String urlForRegister = "https://raw.githubusercontent.com/Andreni/Rusletur_host/master/register.json";
    String TAG = "JSON";

    //Constructor for creating downloader
    public LookUpRegisterNasjonalTurbase(Context currentContext) {
        this.currentContext = currentContext;
    }

    /*

     */
    public void createObjectsFromRegister() {
        mQueue = Volley.newRequestQueue(currentContext);
        FylkeList.getFylkeListArrayList().add(new FylkeList("ListForId"));


        //add dummy element to list
        FylkeList.getFylkeListArrayList().get(0).addFylkeToList(new Fylke("Valg:"));

        Log.d(TAG,"Startingdownload");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlForRegister,
                null, new com.android.volley.Response.Listener<JSONObject>() {
        //Build register File

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,"Response");
                try {
                    JSONArray jsonArrayFylke = response.getJSONArray("fylker");
                    for (int i = 0; i < jsonArrayFylke.length(); i++) {
                        JSONObject fylkename = jsonArrayFylke.getJSONObject(i);

                        String aFylkeName = fylkename.getString("fylkenavn");
                        Log.d(TAG,aFylkeName);
                        //Create all "Fylke" objects
                        FylkeList.getFylkeListArrayList().get(0).addFylkeToList(new Fylke(aFylkeName));
                        FylkeList.getRegisterForFylke().get(i+1).addKommuneForFylke(new Kommune("Valg:"));
                        //Once the "Fylke object is created add "Kommune"
                        //Get all kommune objects appended to the choosen "Fylke"
                        JSONArray jsonArrayKommune = response.getJSONArray("fylker")
                                .getJSONObject(i)
                                .getJSONArray("kommuner");

                        for (int j = 0; j < jsonArrayKommune.length(); j++){
                            JSONObject kommuneName = jsonArrayKommune.getJSONObject(j);

                            //String to hold the current "Kommune"
                            String aKommuneName = kommuneName.getString("kommunenavn");
                            Log.d(TAG,aKommuneName);
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

                            for (int k = 0; k < jsonArrayIds.length(); k++){
                                JSONObject idName = jsonArrayIds.getJSONObject(k);
                                String aIdName = idName.getString("id");
                                Log.d(TAG,aIdName);
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