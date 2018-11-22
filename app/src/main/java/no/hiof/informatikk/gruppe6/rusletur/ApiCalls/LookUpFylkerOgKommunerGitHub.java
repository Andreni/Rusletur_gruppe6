package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.SaveTripActivity;

import static no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment.TAG;

/**
 * comments
 * @author Andreas M.
 */
public class LookUpFylkerOgKommunerGitHub {

    private Context currentContext;
    private RequestQueue mQueue;
    private String url = "https://raw.githubusercontent.com/Andreni/Rusletur_host/master/fylkerogkommuner.txt";
    public static ArrayList<ArrayList<String>> fylkerOgKommuner = new ArrayList<>();


    public LookUpFylkerOgKommunerGitHub(Context currentContext) {
        this.currentContext = currentContext;
    }

    /**
     * Method for retrieving fylkerogkommuner from GitHub.
     * Uses the URL for the text file that's on GitHub. Static url, won't change.
     * Uses volley to retriev the text. Uses ":" as seperation character in the csv file
     */
    public void createObjectsFromFile(){
        mQueue = Volley.newRequestQueue(currentContext);

        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: setupSpinner2: response: " + response);
                String[] fylkerMedKommuner = response.split("\n");
                Log.d(TAG, "onResponse: setupSpinner2: første fylke: " + fylkerMedKommuner[0]);
                for(int i = 0; i < fylkerMedKommuner.length; i++){
                    String[] fylkeKommune = fylkerMedKommuner[i].split(":");
                    boolean fylkeExists = false;
                    int fylkeIndex = 0;
                    for(int j = 0; j < fylkerOgKommuner.size(); j++){
                        if(fylkerOgKommuner.get(j).get(0).equals(fylkeKommune[0])){
                            fylkeExists = true;
                            fylkeIndex = j;
                        }
                    }
                    if(fylkeExists){
                        fylkerOgKommuner.get(fylkeIndex).add(fylkeKommune[1]);
                    }else {
                        fylkerOgKommuner.add(new ArrayList<String>(Arrays.asList(fylkeKommune[0], fylkeKommune[1])));
                    }
                }

                Log.d(TAG, "onResponse: setupSpinner2: Finished with array");
                SaveTripActivity.finished = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(stringRequest);
    }
}
