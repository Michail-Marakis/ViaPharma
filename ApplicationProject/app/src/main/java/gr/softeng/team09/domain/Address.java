package gr.softeng.team09.domain;

/**
 * The type Address.
 */
public class Address {

    private String street;
    private int streetNo;
    private String region;
    private String postalCode;

    /**
     * Instantiates a new Address.
     *
     * @param street     the street
     * @param streetNo   the street no
     * @param region     the region
     * @param postalCode the postal code
     */
    public Address(String street, int streetNo, String region, String postalCode) {
        this.street = street;
        this.streetNo = streetNo;
        this.region = region;
        this.postalCode = postalCode;
    }

    /**
     * Gets street.
     *
     * @return the street
     */
    public String getStreet() { return street; }

    /**
     * Sets street.
     *
     * @param street the street
     */
    public void setStreet(String street) { this.street = street; }

    /**
     * Gets street no.
     *
     * @return the street no
     */
    public int getStreetNo() { return streetNo; }

    /**
     * Sets street no.
     *
     * @param streetNo the street no
     */
    public void setStreetNo(int streetNo) { this.streetNo = streetNo; }

    /**
     * Gets region.
     *
     * @return the region
     */
    public String getRegion() { return region; }

    /**
     * Sets region.
     *
     * @param region the region
     */
    public void setRegion(String region) { this.region = region; }

    /**
     * Gets postal code.
     *
     * @return the postal code
     */
    public String getPostalCode() { return postalCode; }

    /**
     * Sets postal code.
     *
     * @param postalCode the postal code
     */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}
