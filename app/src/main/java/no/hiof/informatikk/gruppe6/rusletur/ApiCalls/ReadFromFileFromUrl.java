package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.content.Context;
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

public class ReadFromFileFromUrl {

    static String url = "https://raw.githubusercontent.com/Andreas981/httpRequestForRusleTur/master/register.json?token=Ae4q3_2Tq7QAEoM5ugLuRP37chMdF13Eks5b3xfuwA%3D%3D";
    public static RequestQueue mQueue;
    public static String TAG = "GetAllDataFromJsonFile";


    public static void getFile(Context k){
        mQueue = Volley.newRequestQueue(k);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray fylker = (JSONArray) response.get("fylker");

                    for(int i = 0; i < fylker.length(); i++){
                        JSONObject fylke = (JSONObject) fylker.get(i);
                        JSONArray kommunerIfylket = (JSONArray) fylke.get("kommuner");
                        for(int j = 0; j < kommunerIfylket.length(); j++){
                            JSONObject kommune = (JSONObject) kommunerIfylket.get(j);
                            JSONArray turerIkommune = (JSONArray) kommune.get("turer");
                            for(int k = 0; k < turerIkommune.length(); k++){
                                JSONObject tur = (JSONObject) turerIkommune.get(k);
                                String kommunenavn = kommune.get("kommunenavn").toString();
                                String fylkenavn = fylke.get("fylkenavn").toString();
                                String turId = tur.get("id").toString();
                                Log.d(TAG, "Fylke: " + fylkenavn + "\nKommune: " + kommunenavn + "\nTur id: " + turId + "\n\n\n\n");
                            }
                        }
                    }


                } catch (JSONException e) {
                    Log.d(TAG, "onResponse: DODOOOOONE");
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
