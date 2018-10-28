package no.hiof.informatikk.gruppe6.rusletur.Model;

import java.util.ArrayList;

/**
 * "Kommune" objects, Contains available "IdForTur" objects downloaded from Register.json
 * @see Fylke
 */
public class Kommune {
    ArrayList<IdForTur> idForTurArrayList = new ArrayList<>();
    String kommuneNavn;

    public Kommune(String kommuneNavn) {
        this.kommuneNavn = kommuneNavn;
    }

    public void addIdForKommune(IdForTur aID){
        this.idForTurArrayList.add(aID);
    }

    public String getKommuneNavn() {
        return kommuneNavn;
    }

    @Override
    public String toString(){
        return kommuneNavn;
    }
}
