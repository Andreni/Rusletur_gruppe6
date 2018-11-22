package no.hiof.informatikk.gruppe6.rusletur.Spinner;

/**
 * Makes an object that is used in the "Fylke" spinner in the FindAtrip class
 * It can either be created with an image, or without. The image is the "fylkesvåpen" for the gived county
 * @author Andreas M.
 * @version 1.0
 */
public class SpinnerData {
    private int picture = 0;
    private String name;

    /**
     * Constructor for a SpinnerData object with image
     * @param name The name of the county
     * @param picture The "fylkesvåpen" of the county
     */
    public SpinnerData(String name, int picture) {
        this.name = name;
        this.picture = picture;
    }

    /**
     * Constructor with only the name of the county
     * @param name The name of the county
     */
    public SpinnerData(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getPicture(){
        return picture;
    }

    @Override
    public String toString() {
        return name + ": Picture: " + picture;
    }
}
