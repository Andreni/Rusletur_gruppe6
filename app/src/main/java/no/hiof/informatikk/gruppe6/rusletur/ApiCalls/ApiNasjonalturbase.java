package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.MainTripRecyclerViewAdapter;
import no.hiof.informatikk.gruppe6.rusletur.Trips;

import static no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment.TAG;

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
     private static Context kont;

         String url = "https://raw.githubusercontent.com/Andreas981/httpRequestForRusleTur/master/register.json?token=Ae4q3xPSJyoUwQKbpO2uoHA78Lx6MRqzks5b4XrbwA%3D%3D";

         public static void getTripInfo(String idForTrip, final Context context) {

             kont = context;
             mQueue = Volley.newRequestQueue(context);

             String url = "https://dev.nasjonalturbase.no/turer/" + idForTrip;
             Log.d(TAG, "getTripInfo onResponse2: Id for tur: " + idForTrip);
             JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {
                     try{
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


                         String urlFraUrl;// = response.get("url").toString();

                         String urlFraUrl1 = response.get("url").toString();

                            urlFraUrl = urlFraUrl1;
                         ArrayList<LatLng> latlng = new ArrayList<>();

                         JSONObject geojson = (JSONObject) response.get("geojson");
                         JSONArray coords = (JSONArray) geojson.get("coordinates");
                         for (int j = 0; j < coords.length(); j++) {
                             JSONArray coord = (JSONArray) coords.get(j);
                             for (int k = 0; k < coord.length(); k++) {
                                 latlng.add(new LatLng(coord.getDouble(0), coord.getDouble(1)));
                             }
                         }

                         //Setter sammen strengen for tidsbruk
                         JSONObject tidsbrukObj = (JSONObject) response.get("tidsbruk");
                         JSONObject normal = null;

                         if(tidsbrukObj.has("normal")){
                             normal = (JSONObject) tidsbrukObj.get("normal");
                         }else if(tidsbrukObj.has("min")){
                             normal = (JSONObject) tidsbrukObj.get("min");
                         }

                         String dager = null;
                         String timer = null;
                         String minutter = null;
                         String tidsbruk;

                         Log.d(TAG, "onResponse2: Normal: " + normal);
                         if(normal.has("dager")){
                             dager = normal.getString("dager");
                         }
                         if(normal.has("timer")){
                             timer = normal.getString("timer");
                         }
                         if(normal.has("minutter")){
                             minutter = normal.getString("minutter");
                         }

                         tidsbruk = ((dager != null) ? (dager + " dager, ") : "") + "" + ((timer != null) ? (timer + " timer, ") : "0 timer, ") + "" + ((minutter != null) ? (minutter + " minutter") : "0 minutter");

                         //Trips.turer.add(new Trip(id, navn, tag, gradering, tilbyder, fylke, kommume, beskrivelse, lisens, urlFraUrl, latlng, tidsbruk));

                         Log.d(TAG, "onResponse: " + Trips.turer);

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
             Log.d(TAG, "getTripInfo: " + trip);

         }


     }
