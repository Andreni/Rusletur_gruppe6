package no.hiof.informatikk.gruppe6.rusletur.Model;

import java.util.ArrayList;

/**
 * @author Andreas N.
 * @version 1.0
 * "Fylke objects, Contains available "Kommune" objects downloaded from Register.json
 * @see FylkeList
 */
public class Fylke {
    String fylkeName;
    ArrayList<Kommune> kommuneArrayList = new ArrayList<>();


    public Fylke(String fylkeName) {
        this.fylkeName = fylkeName;
    }

    public String getFylkeName() {
        return fylkeName;
    }


    public void addKommuneForFylke(Kommune aKommune){
        this.kommuneArrayList.add(aKommune);
    }

    public void addFylke(Fylke aFylke){
    }

    public ArrayList<Kommune> getKommuneArrayList() {
        return kommuneArrayList;
    }

    @Override
    public String toString(){
        return fylkeName;
    }
}
