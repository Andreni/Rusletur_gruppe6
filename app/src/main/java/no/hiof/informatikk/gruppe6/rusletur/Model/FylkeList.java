package no.hiof.informatikk.gruppe6.rusletur.Model;

import java.util.ArrayList;

/**
 * @author Andreas N.
 * @version 1.0
 * Register object to keep track of existing "Fylke" made from Register.json
 * Stores "Fylke" objects
 * The "FylkeList" is made to keep track of valid ids for an existing "Fylke" and "Kommune"
 */
public class FylkeList {
    String nameForRegister;
    static ArrayList<Fylke> registerForFylke = new ArrayList<>();
    static ArrayList<FylkeList> fylkeListArrayList = new ArrayList<>();

    public FylkeList(String nameForRegister) {
        this.nameForRegister = nameForRegister;
        registerForFylke.clear();
        fylkeListArrayList.clear();
        fylkeListArrayList.add(this);
    }

    public void addFylkeToList(Fylke aFylke){
         this.registerForFylke.add(aFylke);
    }

    public String getNameForRegister() {
        return nameForRegister;
    }

    public static ArrayList<Fylke> getRegisterForFylke() {
        return registerForFylke;
    }

    public static ArrayList<FylkeList> getFylkeListArrayList() {
        return fylkeListArrayList;
    }
}
