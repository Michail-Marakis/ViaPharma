package gr.softeng.team09.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Clerk.
 */
public class Clerk {

    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;
    private List<StringBuilder> EmailsSent = new ArrayList<>();

    /**
     * Gets sent to.
     *
     * @return the sent to
     */
    public List<Pharmacist> getSentTo() {
        return SentTo;
    }

    private List<Pharmacist> SentTo = new ArrayList<>();

    /**
     * Instantiates a new Clerk.
     *
     * @param name     the name
     * @param surname  the surname
     * @param phone    the phone
     * @param email    the email
     * @param password the password
     */
    public Clerk(String name, String surname, String phone, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    /**
     * Set emails sent.
     *
     * @param emailsSent the emails sent
     * @param pharmacist the pharmacist
     */
    public void setEmailsSent(StringBuilder emailsSent, Pharmacist pharmacist){
        EmailsSent.add(emailsSent);
        SentTo.add(pharmacist);
    }


    /**
     * Get emails sent list.
     *
     * @return the list
     */
    public List<StringBuilder> getEmailsSent(){
        return EmailsSent;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets surname.
     *
     * @return the surname
     */
    public String getSurname() { return surname; }

    /**
     * Sets surname.
     *
     * @param surname the surname
     */
    public void setSurname(String surname) { this.surname = surname; }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() { return phone; }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() { return email; }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password.
     *
     * @param pass the pass
     */
    public void setPassword(String pass){
        this.password = pass;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clerk)) return false;
        Clerk clerk = (Clerk) o;
        return Objects.equals(email, clerk.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
