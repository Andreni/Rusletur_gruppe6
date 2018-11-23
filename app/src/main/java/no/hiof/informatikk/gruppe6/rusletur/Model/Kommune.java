package no.hiof.informatikk.gruppe6.rusletur.Model;

import java.util.ArrayList;

/**
 * A kommune object used by {@link no.hiof.informatikk.gruppe6.rusletur.ApiCalls.LookUpRegisterNasjonalTurbase}
 * "Kommune" objects, Contains available {@link IdForTur} objects.
 * @author Andreas N
 * @version 1.0
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

    public ArrayList<IdForTur> getIdForTurArrayList() {
        return idForTurArrayList;
    }
}
