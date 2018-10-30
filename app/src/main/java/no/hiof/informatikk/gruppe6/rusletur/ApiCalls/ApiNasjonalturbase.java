package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;

/**
 *                  ApiNasjonalturbase.java
 */
 /**
 *      getTrips(String fylke, final String kommune, Context context)
 *
 * @return Returns a ArrayList with Trip objects that the user would see.
 */
 /**
 *              getTripInfo(ArrayList<String> id)
 *
 * Takes a ArrayList of id's as argument
 * Creating Trip objects and adding them to an ArrayList.
 * @return Returns ArrayList with trips
 *
 */


public class ApiNasjonalturbase {

     //public static RequestQueue mQueue;
     public static int antall = 0;
     static RequestQueue mQueue;
     private static Trip trip;

     //public static ArrayList<Trip> getTrips(String fylke, final String kommune, Context context) {

        // String f = fylke;
         //String k = kommune;
         //Context kont = context;

         String url = "https://raw.githubusercontent.com/Andreas981/httpRequestForRusleTur/master/register.json?token=Ae4q3xPSJyoUwQKbpO2uoHA78Lx6MRqzks5b4XrbwA%3D%3D";
         //final ArrayList<String> id = new ArrayList<>();

         //Get id's from github file
        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray fylker = (JSONArray) response.get("fylker");

                    for(int i = 0; i < fylker.length(); i++){
                        JSONObject tmpFylke = (JSONObject) fylker.get(i);
                        if(tmpFylke.get("fylkenavn").toString().equals(f)){
                            JSONArray kommuner = (JSONArray) tmpFylke.get("kommuner");
                            for(int j = 0; j < kommuner.length(); j++){
                                JSONObject tmpKommune = (JSONObject) kommuner.get(j);
                                if(tmpKommune.get("kommunenavn").toString().equals(k)){
                                    JSONArray turer = (JSONArray) tmpKommune.get("turer");
                                    for(int k = 0; k < turer.length(); k++){
                                        JSONObject tmpTur = (JSONObject) turer.get(k);
                                        id.add(tmpTur.get("id").toString());
                                    }
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();            }
        });
        mQueue.add(request);

        return getTripInfo(id);

    }*/

         public static Trip getTripInfo(String idForTrip, Context context){

             mQueue = Volley.newRequestQueue(context);

             //for (int i = 0; i < ID.size(); i++){
             String url = "http://dev.nasjonalturbase.no/turer/" + idForTrip + "?api_key%{cb93d09a566a0ea1e6499a2be18beed87d7e2bb2}";
             JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {

                     try {
                         String id = response.get("_id").toString();
                         String navn = response.get("navn").toString();

                         JSONArray tags = (JSONArray) response.get("tags");
                         String tag = tags.get(0).toString();

                         String gradering = response.get("gradering").toString();
                         String tilbyder = response.get("tilbyder").toString();

                         JSONArray fylker = (JSONArray) response.get("fylker");
                         String fylke = fylker.get(0).toString();

                         JSONArray kommuner = (JSONArray) response.get("kommuner");
                         String kommume = kommuner.get(0).toString();

                         String beskrivelse = response.get("beskrivelse").toString();
                         String lisens = response.get("lisens").toString();
                         String urlFraUrl = response.get("url").toString();

                         ArrayList<LatLng> latlng = new ArrayList<>();

                         JSONObject geojson = (JSONObject) response.get("geojson");
                         JSONArray coords = (JSONArray) geojson.get("coordinates");
                         for (int j = 0; j < coords.length(); j++) {
                             JSONArray coord = (JSONArray) coords.get(j);
                             for (int k = 0; k < coord.length(); k++) {
                                 latlng.add(new LatLng(coord.getDouble(0), coord.getDouble(1)));
                             }
                         }

                         JSONObject tidsbrukObj = (JSONObject) response.get("tidsbruk");
                         JSONObject normal = (JSONObject) tidsbrukObj.get("normal");
                         String tidsbruk = normal.get("timer").toString() + " timer, " + normal.get("minutter").toString() + " minutter";

                         trip = new Trip(id, navn, tag, gradering, tilbyder, fylke, kommume, beskrivelse, lisens, urlFraUrl, latlng, tidsbruk);

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

             return trip;
         }

     }
