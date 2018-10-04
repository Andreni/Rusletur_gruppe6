package no.hiof.informatikk.gruppe6.rusletur;

/**
 * Stores the userinformation for the RTDB. Easier to store and retrieve information.
 */

public class User {
    private String firstName;
    private String lastName;
    private String tlf;
    private String email;

    public User(String firstName, String lastName, String tlf, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tlf = tlf;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
