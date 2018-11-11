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

import no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips.Trip;

/**
 * Basic db for Rusletur
 * Allows only one copy of the database to be available for the user
 * Not thread safe
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
    public LocalStorage(Context context,String name, SQLiteDatabase.CursorFactory factory,
                        int version){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /*
    /**
     * Get a singelton of the created object's helper
     * @param context content provider contect
     * @return SQLite helper object.
     */
   /*public static LocalStorage getInstance(Context context){
        if(instance == null){
            Log.d(TAG, "getInstance: call for getting a instance");
            instance = new LocalStorage(context);
        }else{
            instance = getInstance(context);
        }
        return */

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Setting uo database");
        String createStatement = "CREATE TABLE IF NOT EXISTS "+ TABLE_TRIPS + " (" +
                COLUMN_ID + "TEXT PRIMARY KEY," +
                COLUMN_NAVN + "TEXT," +
                COLUMN_TAG + "TEXT," +
                COLUMN_GRADERING + "TEXT," +
                COLUMN_TILBYDER + "TEXT," +
                COLUMN_FYLKE + "TEXT," +
                COLUMN_KOMMUNE + "TEXT," +
                COLUMN_BESKRIVELSE + "TEXT," +
                COLUMN_LISENS + "TEXT," +
                COLUMN_URL + "TEXT," +
                COLUMN_TIDSBRUK + "TEXT," +
                COLUMN_LATLNG + "TEXT" +
                ");";
        db.execSQL(createStatement);
        db.close();
    }

    /**
     * Add a trip to the local db
     * @param aTrip takes the passed trip that is to be stored locally.
     */
    public void addTrip(Trip aTrip){
        Log.d(TAG, "addTrip: Adding trip");
        ContentValues values = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

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
        sqLiteDatabase.delete(TABLE_TRIPS,COLUMN_ID + " =  " + idOfTrip,null);
        sqLiteDatabase.close();
    }

    public ArrayList<Trip> getAllTrips(){
        ArrayList<Trip> availableTrips = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TRIPS + ";", null);
        if (cursor.moveToFirst()) {
            do {
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
                availableTrips.add(new Trip(cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),
                        cursor.getString(6),cursor.getString(7),
                        cursor.getString(8),cursor.getString(9),
                        cursor.getString(10),
                        arrayListLatLng,cursor.getString(11)));


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
