package no.hiof.informatikk.gruppe6.rusletur.Spinner;

public class SpinnerData {
    private int picture;
    private String name;

    public SpinnerData(String name, int picture) {
        this.name = name;
        this.picture = picture;
    }
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
