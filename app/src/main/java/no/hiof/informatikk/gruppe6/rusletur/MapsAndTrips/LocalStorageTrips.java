package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

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
        double a1, a2, b1, b2, c1, c2;
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
        }
        /////////////////////////////////////////

        /**
         * TODO: final you can see this result and can store into database
         */
        Log.e("value:", "" + Arrays.toString(mValue));


        SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("TripsLocal.db", MODE_PRIVATE, null);
        //Intitial run config

        String sqlToInsert = "DROP TABLE trips;";
        sqLiteDatabase.execSQL(sqlToInsert);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS trips(navn TEXT," +
                "tag TEXT,gradering TEXT,tilbyder TEXT,fylke TEXT,kommune TEXT,beskrivelse TEXT," +
                "lisens TEXT, url TEXT, tidsbruk TEXT, latLng TEXT);");

        sqlToInsert = "INSERT INTO trips VALUES('Vallresknuten','kort','vanskelig','lokal','Rogaland','Strand','en beskrivelse','Ingen','blank','0','" + Arrays.toString(mValue) + "');";
        sqLiteDatabase.execSQL(sqlToInsert);

        ArrayList<String[]> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM trips;", null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);

                //Fetch the string that contains the compressed Array
                String latLng = cursor.getString(10);
                Log.d("SQLQ", latLng + " looks like this");

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
                }
            } while ((cursor.moveToNext()));


        }
        cursor.close();
        sqLiteDatabase.close();
    }

    public void goBack(View view){
        super.onBackPressed();
    }
}



