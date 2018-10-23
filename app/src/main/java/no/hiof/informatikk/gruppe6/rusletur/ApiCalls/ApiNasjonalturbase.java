package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiNasjonalturbase extends AppCompatActivity{
    public static String TAG = "APICall";

    public static void jsonFetchTripList(Context k) {

        RequestQueue mQueue = Volley.newRequestQueue(k);

        String url = "http://dev.nasjonalturbase.no/turer?limit=1000";

        Log.d(TAG, "jsonFetchTripList: KALLL2 tester");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: KALLL2 uttafor");
                try {
                    JSONArray jsonArray = response.getJSONArray("documents");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject turer = jsonArray.getJSONObject(i);

                        String id = turer.getString("_id");
                        String status = turer.getString("status");
                        String endret = turer.getString("endret");
                        String tilbyder = turer.getString("tilbyder");
                        JSONObject tags  = turer.getJSONObject("tags");
                        String lisens = turer.getString("lisens");
                        String navn = turer.getString("navn");

                        Log.d(TAG, "onResponse: KALLL2 id: " + id + ". Navn: " + navn);

                    }

                } catch (JSONException e) {
                    Log.d(TAG, "onResponse: KALLL FAIL ");
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
