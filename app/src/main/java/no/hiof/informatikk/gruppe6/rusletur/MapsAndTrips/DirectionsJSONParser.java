package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

/**
 * A class to parse the data recieved from Google Directions services.
 * Code recreated and edited to fit the project from : https://bitbucket.org/snippets/faizan_mubasher_skm/89Edap
 * @author Magnus P.
 */
public class DirectionsJSONParser {
    private final String TAG = "DirectionsJSONParser";

    /**
     * Receives a JSONObject and returns a list of lists containing latitude and longitude
     * */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        int distanceRaw = 0;
        String distance = null;
        int durationRaw = 0;
        String duration = null;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /* For each route */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();
                /* Fetches total distance and duration */
                if(i == 0) {
                    distance = (String) ((JSONObject) ((JSONObject) jLegs.get(0)).get("distance")).get("text");
                    duration = (String) ((JSONObject) ((JSONObject) jLegs.get(0)).get("duration")).get("text");
                    distanceRaw = ((JSONObject) ((JSONObject) jLegs.get(0)).get("duration")).getInt("value");
                    durationRaw = ((JSONObject) ((JSONObject) jLegs.get(0)).get("duration")).getInt("value");
                }

                /* For each legs, get the steps */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");


                    /* Foe each steps, gets the list which each contain LatLng */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /* For each LatLng, add it to hashmap hm. First loop gets the distance and duration */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            if(l == 0) {
                                hm.put("distance", distance);
                                hm.put("distanceRaw", String.valueOf(distanceRaw));
                                hm.put("duration", duration);
                                hm.put("durationRaw", String.valueOf(durationRaw));
                            }
                            path.add(hm);
                        }
                    }
                    Log.d(TAG,"Path added to routes...");
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        Log.d(TAG,"Routes returning...");
        return routes;
    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        Log.d(TAG,"Poly added, total: " + poly.size());
        return poly;
    }
}