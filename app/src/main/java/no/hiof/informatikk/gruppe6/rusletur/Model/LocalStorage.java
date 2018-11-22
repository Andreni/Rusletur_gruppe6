package no.hiof.informatikk.gruppe6.rusletur.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;

/**
 * Local DB for Rusletur
 * Allows only one copy of the database to be available for the user
 * Used for storing Trip {@link Trip} objects.
 * @author Andreas N
 * @version 1.0
 */

public class LocalStorage extends SQLiteOpenHelper {
    private static final String TAG = "LocalStorageSQL";
    private static final String DATABASE_NAME = "Rusletur.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TRIPS = "trips";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAVN ="navn";
    private static final String COLUMN_TAG ="tag";
    private static final String COLUMN_GRADERING = "gradering";
    private static final String COLUMN_TILBYDER = "tilbyder";
    private static final String COLUMN_FYLKE = "fylke";
    private static final String COLUMN_KOMMUNE = "kommune";
    private static final String COLUMN_BESKRIVELSE = "beskrivelse";
    private static final String COLUMN_LISENS = "lisens";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_TIDSBRUK = "tidsbruk";
    private static final String COLUMN_LATLNG = "latLng";

    private static LocalStorage instance = null;

    // We only want a singelton of this class.
     LocalStorage(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    /**
     * Get a singelton of the created object's helper
     * @param context content provider context
     * @return SQLite helper object.
     */
   public static LocalStorage getInstance(Context context) {
       if (instance == null) {
           Log.d(TAG, "getInstance: call for getting a instance");
           instance = new LocalStorage(context);
       }

       return instance;
   }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Setting uo database");
        String createStatement = "CREATE TABLE "+ TABLE_TRIPS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY NOT NULL," +
                COLUMN_NAVN + " TEXT NOT NULL," +
                COLUMN_TAG + " TEXT NOT NULL," +
                COLUMN_GRADERING + " TEXT NOT NULL," +
                COLUMN_TILBYDER + " TEXT NOT NULL," +
                COLUMN_FYLKE + " TEXT NOT NULL," +
                COLUMN_KOMMUNE + " TEXT NOT NULL," +
                COLUMN_BESKRIVELSE + " TEXT NOT NULL," +
                COLUMN_LISENS + " TEXT NOT NULL," +
                COLUMN_URL + " TEXT NOT NULL," +
                COLUMN_TIDSBRUK + " TEXT NOT NULL," +
                COLUMN_LATLNG + " TEXT NOT NULL" +
                ");";
        db.execSQL(createStatement);

    }

    /**
     * Add a trip to the local db
     * @param aTrip takes the passed trip that is to be stored locally.
     */
    public void addTrip(Trip aTrip){
        Log.d(TAG, "addTrip: Adding trip");
        ContentValues values = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Log.i(MainScreen.TAG3, "LocalStorage addTrip " + String.valueOf(aTrip.getCoordinates().size()));

        // Compress the LatLng array to a string, so all the values can be stored.
        String[] mValue = new String[aTrip.getCoordinates().size()];
        for (int i = 0; i < aTrip.getCoordinates().size(); i++) {
            mValue[i] = aTrip.getCoordinates().get(i).latitude +
                    " - " +
                    aTrip.getCoordinates().get(i).longitude;
            }

        // Extract the values from the passed object
        values.put(COLUMN_ID,aTrip.getId());
        values.put(COLUMN_NAVN,aTrip.getNavn());
        values.put(COLUMN_TAG,aTrip.getTag());
        values.put(COLUMN_GRADERING,aTrip.getGradering());
        values.put(COLUMN_TILBYDER,aTrip.getTilbyder());
        values.put(COLUMN_FYLKE,aTrip.getFylke());
        values.put(COLUMN_KOMMUNE,aTrip.getKommune());
        values.put(COLUMN_BESKRIVELSE,aTrip.getBeskrivelse());
        values.put(COLUMN_LISENS,aTrip.getLisens());
        values.put(COLUMN_URL,aTrip.getUrl());
        values.put(COLUMN_TIDSBRUK,aTrip.getTidsbruk());
        values.put(COLUMN_LATLNG,Arrays.toString(mValue));

        sqLiteDatabase.insert(TABLE_TRIPS,null,values);
        sqLiteDatabase.close();


    }

    /**
     * Delete a object based on the id
     * @param idOfTrip the passed id of the trip that is about to get deleted
     */
    public void deleteAtrip(String idOfTrip){
        Log.d(TAG, "deleteAtrip: " + idOfTrip + " to get deleted.");
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_TRIPS,COLUMN_ID + " =  '" + idOfTrip + "';",null);
        sqLiteDatabase.close();
    }

    /**
     * Method for reading all the stored objects
     * @return Gives a Built array with all the available trip Objects
     */
    public ArrayList<Trip> getAllTrips(){
        ArrayList<Trip> availableTrips = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TRIPS + ";", null);
        if (cursor.moveToFirst()) {
            do {
                String latLng = cursor.getString(11);
                // Fetch the string that contains the compressed Array
                // Remove the [ ] from the recovered string
                String arrRemoved = latLng.substring(1, latLng.length() - 1);
                // Make a new Array by splitting the latLng points by ,
                String[] latLngArray = arrRemoved.split(", ");
                ArrayList<LatLng> arrayListLatLng = new ArrayList<>();
                // Split simple array into Lat[0} and Long[1]
                for (String aLatLngArray : latLngArray) {
                    String[] latLngSplits = aLatLngArray.split(" - ");
                    Double lat = Double.parseDouble(latLngSplits[0]);
                    Double longt = Double.parseDouble(latLngSplits[1]);
                    arrayListLatLng.add(new LatLng(lat, longt));
                }
                // Build the retrieved trip object from storage
                    availableTrips.add(new Trip(cursor.getString(0),
                            cursor.getString(1),cursor.getString(2),
                            cursor.getString(3),cursor.getString(4),
                            cursor.getString(5),cursor.getString(6),
                            cursor.getString(7),cursor.getString(8),
                            cursor.getString(9),
                            arrayListLatLng,cursor.getString(10)));


            } while ((cursor.moveToNext()));

        }
        cursor.close();
        sqLiteDatabase.close();
        return availableTrips;
    }

    /**
     * Method for searching for FindAtrip that matches a search criteria.
     * @param fylke Takes a passed "fylke" as a search parameter
     * @param kommune Takes a passed "kommune" as a search parameter
     * @return Gives back an array of the objects that matches the search criteria.
     */
    public ArrayList<Trip> getTripsByCriteria(String fylke,String kommune){
        ArrayList<Trip> availableTrips = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TRIPS +  " " +
                "WHERE " + COLUMN_FYLKE + " = '" + fylke + "'" +
                " AND " + COLUMN_KOMMUNE + " = '" + kommune + "';", null);
        if (cursor.moveToFirst()) {
            do {
                String latLng = cursor.getString(11);
                // Fetch the string that contains the compressed Array
                // Remove the [ ] from the recovered string
                String arrRemoved = latLng.substring(1, latLng.length() - 1);
                // Make a new Array by splitting the latLng points by ,
                String[] latLngArray = arrRemoved.split(", ");
                ArrayList<LatLng> arrayListLatLng = new ArrayList<>();
                // Split simple array into Lat[0} and Long[1]
                for (String aLatLngArray : latLngArray) {
                    String[] latLngSplits = aLatLngArray.split(" - ");
                    Double lat = Double.parseDouble(latLngSplits[0]);
                    Double longt = Double.parseDouble(latLngSplits[1]);
                    arrayListLatLng.add(new LatLng(lat, longt));
                }
                // Build the retrieved trip object from storage
                availableTrips.add(new Trip(cursor.getString(0),
                        cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getString(4),
                        cursor.getString(5),cursor.getString(6),
                        cursor.getString(7),cursor.getString(8),
                        cursor.getString(9),
                        arrayListLatLng,cursor.getString(10)));


            } while ((cursor.moveToNext()));

        }
        cursor.close();
        sqLiteDatabase.close();
        return availableTrips;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
}
