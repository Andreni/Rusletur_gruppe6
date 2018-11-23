package no.hiof.informatikk.gruppe6.rusletur.Model;

/**
 * Stores avalible ids for a location {@link Fylke}--> {@link Kommune} --> {@link IdForTur})
 * Used for looking up available trip objects towards Nasjonal turbase
 * @author Andreas N
 * @version 1.0
 */
public class IdForTur {
    String idForTur;

    public IdForTur(String idForTur) {
        this.idForTur = idForTur;
    }

    @Override
    public String toString(){
        return idForTur;
    }

    public String getIdForTur() {
        return idForTur;
    }

}
