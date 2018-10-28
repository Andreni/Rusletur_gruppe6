package no.hiof.informatikk.gruppe6.rusletur.Model;

/**
 * Stores avalible ids for a location (Fylke --> Kommune --> Ids)
 * Used for looking up available trip objects towards Nasjonal turbase
 * @see Kommune
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
