package no.hiof.informatikk.gruppe6.rusletur.MapsAndTrips;


import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;
import io.ticofab.androidgpxparser.parser.task.GpxFetchedAndParsed;

import static android.support.constraint.Constraints.TAG;

/**
 * Class to make a map object by parsing provided .gpx file and retrieving latLng coordinates.
 *  /*Under Construction --- Only Example Provided ---
 */
public class GenerateMap {
    //TODO Make constructor
    //TODO Generate or migrate this class
    //TODO Call on
    //        super.onCreate();
    //        JodaTimeAndroid.init(this);
    static final String GPXLOG = "GPXLOG";


    GPXParser mParser = new GPXParser(); // consider injection


    /**
     * Example method to parse a gpx file and print its out put
     * @param urlToGpx Webaddress to gpx file. Can be both relative and absolute
     */

    public static void parseGpx(String urlToGpx){
        final GPXParser mParser = new GPXParser();
        mParser.parse(urlToGpx, new GpxFetchedAndParsed() {
            @Override
            public void onGpxFetchedAndParsed(Gpx gpx) {
                if (gpx == null) {
                    // error parsing track
                } else {
                    if (mParser != null) {
                        // log stuff
                        List<Track> tracks = gpx.getTracks();
                        for (int i = 0; i < tracks.size(); i++) {
                            Track track = tracks.get(i);
                            Log.d(GPXLOG, "track " + i + ":");
                            List<TrackSegment> segments = track.getTrackSegments();
                            for (int j = 0; j < segments.size(); j++) {
                                TrackSegment segment = segments.get(j);
                                Log.d(GPXLOG, "  segment " + j + ":");
                                for (TrackPoint trackPoint : segment.getTrackPoints()) {
                                    Log.d(GPXLOG, "    point: lat " + trackPoint.getLatitude() + ", lon " + trackPoint.getLongitude());
                                }

                            }

                        }
                    } else {
                        Log.e(TAG, "Error parsing gpx track!");
                    }
                }
                }

        });
    }

    /*public void parseLocalGpx(String pathToFile){
        Gpx parsedGpx = null;
        try {
            InputStream in = getAssets() .open(pathToFile);
            parsedGpx = mParser.parse(in);
        } catch (IOException | XmlPullParserException e) {
            // do something with this exception
            e.printStackTrace();
        }
        if (parsedGpx == null) {
            // error parsing track
        } else {
            // do something with the parsed track
        }
    } */







}
