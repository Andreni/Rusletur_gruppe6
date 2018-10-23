package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiNasjonalturbase extends AppCompatActivity{

    private RequestQueue mQueue = Volley.newRequestQueue(this);

    public static void jsonFetchTripList() {
        String url = "dev.nasjonalturbase.no/turer";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }

}
