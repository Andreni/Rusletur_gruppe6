package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *                  ApiNasjonalturbase.java
 */
 /**
 *            jsonFetchTripList(Context k, int antallSkips)
 *
 * Takes context and amount of skips when sending the call to get trips.
 * Gets the 100*antallSkips amount of trips from nasjonalturbase.no
 * Sends them to jsonFetchIdInfo(Context k, String id) to get the rest of the info needed.
 * When finished, it writes the id, county and municipality to a file that is sorted in folders for counties.
 * @return Returns a list of id's from nasjonalturbase.no with their county and municipality.
 */
 /**
 *              jsonFetchIdInfo(Context k, String id)
 *
 * Takes the context and an id for a trip as argument.
 * Reutrn the infomation needed from a trip. That info beeing:
 * ID, navn, tidsbruk, tilbyder, kommuner, fylker, passer_for, lisens, coordinates, beskrivelse
 * After retrieving the information, it can be dispalyed on the map or in a detailed list form.
 * @return Returns the info of a trip with the id provided.
 *
 */


public class ApiNasjonalturbase{

   private Context myContext;
   private String idTrip;
   private String urlBase = "http://api.nasjonalturbase.no/";
   public static String TAG = "APICall";
   private RequestQueue mQueue;


     public String getIdTrip() {
         return idTrip;
     }

     public ApiNasjonalturbase(String idTrip) {
         this.idTrip = idTrip;
     }

     public void jsonFetchTripList(Context k) {

        final Context kont = k;
        mQueue = Volley.newRequestQueue(k);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlBase +getIdTrip(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: DOOOONE DOOOONE2 Inside onResponse");
                    try {

                        //JSONArray array = new JSONArray();

                        //array.put(response.getString("_id"));
                        //array.put(response.getString("beskrivelse"));

                        String id  = response.getJSONObject("_id").toString();

                        //String beskrivelse = array.getString(1);
                        Log.d(TAG, "onResponse: DOOOONE DOOOONE2 Id: " + id + ". Beksirvelse: " );


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            mQueue.add(request);

    }
}


    /*public static void jsonFetchIdInfo(Context k, String id){

        String url = "http://dev.nasjonalturbase.no/turer/" + id;

        Log.d(TAG, "jsonFetchIdInfo: DOOOONE DOOOONE2 Inside fetchIdInfo");
        
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: DOOOONE DOOOONE2 Inside onResponse");
                try {

                    JSONArray array = new JSONArray();

                    array.put(response.getString("_id"));
                    array.put(response.getString("beskrivelse"));

                    String id  = array.getString(0);
                    String beskrivelse = array.getString(1);
                    Log.d(TAG, "onResponse: DOOOONE DOOOONE2 Id: " + id + ". Beksirvelse: " + beskrivelse);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,  Error when retrieving id info");
                error.printStackTrace();
            }
        }); */





