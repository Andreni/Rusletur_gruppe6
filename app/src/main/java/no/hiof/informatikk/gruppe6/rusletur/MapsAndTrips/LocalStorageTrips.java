package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import no.hiof.informatikk.gruppe6.rusletur.R;
import no.hiof.informatikk.gruppe6.rusletur.RecyclerView.MainTripRecyclerViewAdapter;

import static no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip.trips;

/**
 * Class for displaying stored Trip objects
 * SQLite
 * When the user either wants to store or load objects, the static methods are called upon
 */
public class LocalStorageTrips extends AppCompatActivity {
    ArrayList<LatLng> myLatLng = new ArrayList<>();
    Button btnBack;
    private MainTripRecyclerViewAdapter mainTripAdapter;
    private ArrayList<Trip> availableTrips = new ArrayList<>();
    public static ArrayList<String> rowIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_storage_trips);
        btnBack = findViewById(R.id.localStorage_goBack_button);

        //TODO Make prepared statements
        SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("TripsLocal.db", MODE_PRIVATE, null);
        //String sqlToInsert = "DROP TABLE trips;";
        String sqlToInsert = "SELECT rowid  FROM trips;";
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT *  FROM trips WHERE kommune = 'Østfold' AND fylke='Halden';",null);
        if (cursor.moveToFirst()) {
            do {

                //Intitial run config
                Log.i("SQLQ",cursor.getString(1));

            } while ((cursor.moveToNext()));

        }else{
            Log.i("SQLQ","No record found");
        }
        retriveItemsFromStorage();
        sqLiteDatabase.close();
        // Initialize recyclerview and set adapter
        
        RecyclerView recyclerView = findViewById(R.id.local_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainTripAdapter = new MainTripRecyclerViewAdapter(this, availableTrips);
        recyclerView.setAdapter(mainTripAdapter);



    }

    /**
     *  When the user wants to store the obejct locally this method is used.
     * @param  context The context from where the method is called from
     * @param aTrip The Trip object the user wants to store is passed
     * @return Gives back a message to the user that trip is stored
     */
    public static String addAitemToStorage(Context context,Trip aTrip){
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("TripsLocal.db", MODE_PRIVATE, null);
        Log.d("SQLQ","Attempting to write to database");
        // TODO MAKE Prepare statement
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS trips(id TEXT,navn TEXT," +
                "tag TEXT,gradering TEXT,tilbyder TEXT,fylke TEXT,kommune TEXT,beskrivelse TEXT," +
                "lisens TEXT, url TEXT, tidsbruk TEXT, latLng TEXT);");

        String[] mValue = new String[aTrip.getCoordinates().size()];
        for (int i = 0; i < aTrip.getCoordinates().size(); i++) {
            mValue[i] = aTrip.getCoordinates().get(i).latitude + " - " + aTrip.getCoordinates().get(i).longitude;
            Log.i("SQLQ",mValue[i]);
            Log.i("SQLQ", " " + aTrip.getCoordinates().get(i).latitude);
            Log.i("SQLQ",Arrays.toString(mValue));

        }

        String insertIntoDB = "INSERT INTO trips VALUES('"+aTrip.getId()+"','"+aTrip.getNavn()+"','"+aTrip.getTag()+
                "','"+aTrip.getGradering()+"','"+aTrip.getTilbyder()+"','"+aTrip.getFylke()+"','"+
                aTrip.getKommune()+"','"+aTrip.getBeskrivelse()+"','"+aTrip.getLisens()+"','"+
                aTrip.getUrl()+"','"+aTrip.getTidsbruk()+"','"+Arrays.toString(mValue)+"');";

        sqLiteDatabase.execSQL(insertIntoDB);
        sqLiteDatabase.close();

        return "Lagret";

    }

    //Method for retrieving all the objects the user has stored
    public static ArrayList<Trip> retriveItemsFromStorage(Context context,String aFylke, String aKommune){
        ArrayList<Trip> availableTrips = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("TripsLocal.db", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS trips(id TEXT,navn TEXT," +
                "tag TEXT,gradering TEXT,tilbyder TEXT,fylke TEXT,kommune TEXT,beskrivelse TEXT," +
                "lisens TEXT, url TEXT, tidsbruk TEXT, latLng TEXT);");
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM trips " +
                "WHERE fylke = '"+aFylke+"'  AND kommune =  '"+aKommune+"';", null);
        if (cursor.moveToFirst()) {
            do {
                Log.i("SQLQ","Match found!");
                String id = cursor.getString(0);
                String navn = cursor.getString(1);
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
                //Fetch the string that contains the compressed Array
                //Remove the [ ] from the recovered string
                String arrRemoved = latLng.substring(1, latLng.length() - 1);
                //Make a new Array by splitting the latLng points by ,
                String[] latLngArray = arrRemoved.split(", ");
                ArrayList<LatLng> arrayListLatLng = new ArrayList<>();
                //Split simple array into Lat[0} and Long[1]
                for (int i = 0; i < latLngArray.length; i++) {
                    String[] latLngSplits = latLngArray[0].split(" - ");
                    Double lat = Double.parseDouble(latLngSplits[0]);
                    Double longt = Double.parseDouble(latLngSplits[1]);
                    arrayListLatLng.add(new LatLng(lat, longt));
                }
                //Build the retrived trip object from storage
                availableTrips.add(new Trip(id,navn,tag,gradering,tilbyder,
                        fylke,kommune,beskrivelse,lisens,url,arrayListLatLng,tidsbruk));
                Log.i("SQLQ","Matches: " + availableTrips.size());

            } while ((cursor.moveToNext()));

        }
        cursor.close();
        sqLiteDatabase.close();

        return availableTrips;
    }

    /**
     * Method for showing all the stored objects to the user.
     * Adds the row ids to the array
     * @ Gives back a array of available objects
     */
    public ArrayList<Trip> retriveItemsFromStorage(){
        rowIds.clear();

        availableTrips = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("TripsLocal.db", MODE_PRIVATE, null);
        // TODO Make prepared statement
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS trips(id TEXT,navn TEXT," +
                "tag TEXT,gradering TEXT,tilbyder TEXT,fylke TEXT,kommune TEXT,beskrivelse TEXT," +
                "lisens TEXT, url TEXT, tidsbruk TEXT, latLng TEXT);");
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT ROWID,* FROM trips;", null);
        if (cursor.moveToFirst()) {
            do {
                rowIds.add(cursor.getString(0));
                Log.i("SQLQ",rowIds.get(0) + " Current row");
                String id = cursor.getString(1);
                String navn = cursor.getString(2);
                String tag = cursor.getString(3);
                String gradering = cursor.getString(4);
                String tilbyder = cursor.getString(5);
                String fylke = cursor.getString(6);
                String kommune = cursor.getString(7);
                String beskrivelse = cursor.getString(8);
                String lisens = cursor.getString(9);
                String url = cursor.getString(10);
                String tidsbruk = cursor.getString(11);
                String latLng = cursor.getString(12);
                // Fetch the string that contains the compressed Array
                // Remove the [ ] from the recovered string
                String arrRemoved = latLng.substring(1, latLng.length() - 1);
                // Make a new Array by splitting the latLng points by ,
                String[] latLngArray = arrRemoved.split(", ");
                ArrayList<LatLng> arrayListLatLng = new ArrayList<>();
                // Split simple array into Lat[0} and Long[1]
                for (int i = 0; i < latLngArray.length; i++) {
                    String[] latLngSplits = latLngArray[i].split(" - ");
                    Double lat = Double.parseDouble(latLngSplits[0]);
                    Double longt = Double.parseDouble(latLngSplits[1]);
                    arrayListLatLng.add(new LatLng(lat, longt));
                }
                // Build the retrieved trip object from storage
                availableTrips.add(new Trip(id,navn,tag,gradering,tilbyder,
                        fylke,kommune,beskrivelse,lisens,url,arrayListLatLng,tidsbruk));


            } while ((cursor.moveToNext()));

        }



        cursor.close();
        sqLiteDatabase.close();

        return availableTrips;
    }

    public static String getRowId(int position){
        return rowIds.get(position);
    }

    /**
     * Method for deleting objects.
     * @param context Context from the activity that wants to delete the object
     * @param rowId The row index that the object is listed on
     * @return Gives back a message when the object is deleted.
     */
    public static String deleteTripFromTable(Context context,String rowId){
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("TripsLocal.db", MODE_PRIVATE, null);

        sqLiteDatabase.delete("trips" , "ROWID"
                    + " = " + rowId, null);
        sqLiteDatabase.close();

        return "Slettet";
    }

    public void goBack(View view){
        super.onBackPressed();
    }
}



