package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import no.hiof.informatikk.gruppe6.rusletur.R;

public class LocalStorageTrips extends AppCompatActivity {
    ArrayList<LatLng> myLatLng = new ArrayList<>();
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_storage_trips);
        btnBack = findViewById(R.id.localStorage_goBack_button);
        /*double a1, a2, b1, b2, c1, c2;
        a1 = 6.28295153901365;
        a2 = 58.8226193213945;
        myLatLng.add(new LatLng(a1, a2));
        b1 = 6.29651278779295;
        b2 = 58.8205753142577;
        c1 = 6.30921572968748;
        c2 = 58.8211085451286;
        myLatLng.add(new LatLng(b1, b2));
        myLatLng.add(new LatLng(c1, c2));
        String[] mValue = new String[myLatLng.size()];
        for (int i = 0; i < myLatLng.size(); i++) {
            mValue[i] = myLatLng.get(i).latitude + " - " + myLatLng.get(i).longitude;
        }

        for (int i = 0; i < myLatLng.size(); i++) {
            Log.d("SQLQ", myLatLng.get(i) + "OY!!");
        }*/
        /////////////////////////////////////////



        SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("TripsLocal.db", MODE_PRIVATE, null);
        //Intitial run config


        //String sqlToInsert = "DROP TABLE trips;";
        //sqLiteDatabase.execSQL(sqlToInsert);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS trips(id TEXT,navn TEXT," +
                "tag TEXT,gradering TEXT,tilbyder TEXT,fylke TEXT,kommune TEXT,beskrivelse TEXT," +
                "lisens TEXT, url TEXT, tidsbruk TEXT, latLng TEXT);");



        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM trips;", null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String tag = cursor.getString(2);
                String gradering = cursor.getString(3);
                String tilbyder = cursor.getString(4);
                String fylke = cursor.getString(5);
                String kommune = cursor.getString(6);
                String beskrivelse = cursor.getString(7);
                String lisens = cursor.getString(8);
                String url = cursor.getString(9);
                String tidsbruk = cursor.getString(10);
                String latLng = cursor.getString(11);




                Log.d("SQLQ",id + "-" + name + " - " + tag + " - " 
                     );



                //Fetch the string that contains the compressed Array
                //String latLng = cursor.getString(10);


                /*Log.d("SQLQ", latLng + " looks like this");

                //Remove the [ ] from the recovered string
                String arrRemoved = latLng.substring(1, latLng.length() - 1);

                //Make a new Array by splitting the latLng points by ,
                String[] latLngArray = arrRemoved.split(", ");

                //Split simple array into Lat[0} and Long[1]
                for (int i = 0; i < latLngArray.length; i++) {
                    String[] latLngSplits = latLngArray[0].split(" - ");
                    Double lat = Double.parseDouble(latLngSplits[0]);
                    Double longt = Double.parseDouble(latLngSplits[1]);
                    myLatLng.add(new LatLng(lat, longt));

                }

                for (int i = 0; i < myLatLng.size(); i++) {
                    Log.d("SQLQ", myLatLng.get(i) + "OY!!");
                }*/
            } while ((cursor.moveToNext()));


        }
        cursor.close();
        sqLiteDatabase.close();
    }

    public static String addAitemToStorage(Context context,Trip aTrip){
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("TripsLocal.db", MODE_PRIVATE, null);
        Log.d("SQLQ","Attempting to write to database");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS trips(id TEXT,navn TEXT," +
                "tag TEXT,gradering TEXT,tilbyder TEXT,fylke TEXT,kommune TEXT,beskrivelse TEXT," +
                "lisens TEXT, url TEXT, tidsbruk TEXT, latLng TEXT);");

        String[] mValue = new String[aTrip.getCoordinates().size()];
        for (int i = 0; i < aTrip.getCoordinates().size(); i++) {
            mValue[i] = aTrip.getCoordinates().get(i).latitude + " - " + aTrip.getCoordinates().get(i).longitude;
        }

        String insertIntoDB = "INSERT INTO trips VALUES('"+aTrip.getId()+"','"+aTrip.getNavn()+"','"+aTrip.getTag()+
                "','"+aTrip.getGradering()+"','"+aTrip.getTilbyder()+"','"+aTrip.getFylke()+"','"+
                aTrip.getKommune()+"','"+aTrip.getBeskrivelse()+"','"+aTrip.getLisens()+"','"+
                aTrip.getUrl()+"','"+aTrip.getTidsbruk()+"','"+mValue.toString()+"');";

        sqLiteDatabase.execSQL(insertIntoDB);
        sqLiteDatabase.close();

        return "Lagret";

    }

    public static ArrayList<Trip> retriveItemsFromStorage(String fylke, String kommune){
        ArrayList<Trip> availableTrips = new ArrayList<>();

        return availableTrips;
    }

    public void goBack(View view){
        super.onBackPressed();
    }
}



