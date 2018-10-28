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
    public static String TAG = "APICall";

    public static RequestQueue mQueue;
    public static int antall = 0;
    /*
    public static void jsonFetchTripList(Context k, int antallSkip) {

        final Context kont = k;
        mQueue = Volley.newRequestQueue(k);
        ApiNasjonalturbase.antall = 0;
        String url = "http://dev.nasjonalturbase.no/turer?limit=100&skip=";

        Log.d(TAG, "jsonFetchTripList: DOOOONE Start of jsonFetchTripList");
        /*Get more than a 100 trips.
          Making tha API calls in a for loop where j is beeing incremented.
          That increments the "skip" parameter in the url
         */ /*
        for (int j = 0; j < antallSkip; j += 1) {
            url = url + (j*100);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("documents");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject turer = jsonArray.getJSONObject(i);

                            String id = turer.getString("_id");
                            ApiNasjonalturbase.antall += 1;
                            Log.d(TAG, "onResponse: DOOOONE id: " + id);
                            Log.d(TAG, "onResponse: DOOOONE DOOOONE3 Antall: " + ApiNasjonalturbase.antall);
                            //ApiNasjonalturbase.jsonFetchIdInfo(kont, id);
                            try {
                                ApiNasjonalturbase.readJsonSimpleDemo("raw/register.json");
                            }catch (FileNotFoundException e){
                                e.printStackTrace();
                            }

                        }

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

    public static void jsonFetchIdInfo(Context k, String id){
        RequestQueue idQueue = Volley.newRequestQueue(k);

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
                Log.d(TAG, "onErrorResponse DOOOONE DOOOONE2: Error when retrieving id info");
                error.printStackTrace();
            }
        });


        mQueue.add(request);
    }


     public static Object readJsonSimpleDemo(String filename) throws FileNotFoundException{
         FileReader reader = new FileReader(filename);
         JsonParser jsonParser = new JsonParser();
         return jsonParser.parse(reader);
     }


*/
}
