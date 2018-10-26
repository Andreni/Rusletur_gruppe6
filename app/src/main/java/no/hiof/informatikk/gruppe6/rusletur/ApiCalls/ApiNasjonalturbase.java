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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiNasjonalturbase{
    public static String TAG = "APICall";

    public static void jsonFetchTripList(Context k) {

        final Context kont = k;
        RequestQueue mQueue = Volley.newRequestQueue(k);

        String url = "http://dev.nasjonalturbase.no/turer?limit=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("documents");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject turer = jsonArray.getJSONObject(i);

                        String id = turer.getString("_id");
                        /*String status = turer.getString("status");
                        String endret = turer.getString("endret");
                        String tilbyder = turer.getString("tilbyder");
                        String lisens = turer.getString("lisens");
                        String navn = turer.getString("navn");*/

                        Log.d(TAG, "onResponse: KALLL2 id: " + id);

                        //ApiNasjonalturbase.jsonFetchIdInfo(kont, "http://dev.nasjonalturbase.no/turer/" + id);

                        new JsonArrayRequest(Request.Method.GET, "dev.nasjonalturbase.no/turer/"+id, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                for(int j = 0; j < response.length(); j++){
                                    try {
                                        JSONObject tur = response.getJSONObject(j);

                                        String id = tur.getString("_id");
                                        String beskrivelse = tur.getString("beskrivelse");

                                        Log.d(TAG, "onResponse: DOOOONE: " + id);
                                        Log.d(TAG, "onResponse: DOOOONE: " + beskrivelse);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "onErrorResponse: Error when retrieving id info");
                                error.printStackTrace();
                            }
                        });


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
//Kommentar!!
    public static void jsonFetchIdInfo(Context k, String id){
        RequestQueue idQueue = Volley.newRequestQueue(k);

        String url = "dev.najsonalturbase.no/turer/" + id;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int j = 0; j < response.length(); j++){
                    try {
                        JSONObject tur = response.getJSONObject(j);

                        String id = tur.getString("_id");
                        String beskrivelse = tur.getString("beskrivelse");

                        Log.d(TAG, "onResponse: DOOOONE: " + id);
                        Log.d(TAG, "onResponse: DOOOONE: " + beskrivelse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: Error when retrieving id info");
                error.printStackTrace();
            }
        });
        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: Error when retrieving id info");
                error.printStackTrace();
            }
        });*/

        idQueue.add(request);
    }

}
