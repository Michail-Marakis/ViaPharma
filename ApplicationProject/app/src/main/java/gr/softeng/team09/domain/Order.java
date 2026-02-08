package gr.softeng.team09.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Order.
 */
public class Order {

    private final int id;
    private final Pharmacy pharmacy;
    private OrderState state;
    private final long creationTimestamp;
    private final List<OrderLine> lines = new ArrayList<>();


    /**
     * Instantiates a new Order.
     *
     * @param id                the id
     * @param pharmacy          the pharmacy
     * @param creationTimestamp the creation timestamp
     */
    public Order(int id, Pharmacy pharmacy, long creationTimestamp) {
        this.id = id;
        this.pharmacy = Objects.requireNonNull(pharmacy);
        this.state = OrderState.DRAFT;
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * Instantiates a new Order.
     *
     * @param id       the id
     * @param pharmacy the pharmacy
     */
//Default constructor -> παίρνει τώρα το timestamp
    public Order(int id, Pharmacy pharmacy) {
        this(id, pharmacy, System.currentTimeMillis());
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets pharmacist.
     *
     * @return the pharmacist
     */
    public Pharmacist getPharmacist() {
        return pharmacy.getOwner();
    }

    /**
     * Gets pharmacy.
     *
     * @return the pharmacy
     */
    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public OrderState getState() {
        return state;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public long getCreationDate() {
        return creationTimestamp;
    }

    /**
     * Gets lines.
     *
     * @return the lines
     */
    public List<OrderLine> getLines() {
        return lines;
    }

    /**
     * Add line.
     *
     * @param line the line
     */
    public void addLine(OrderLine line) {
        if (line != null) {
            lines.add(line);
        }
    }

    /**
     * Remove line.
     *
     * @param line the line
     */
    public void removeLine(OrderLine line) {
        if (line != null) {
            lines.remove(line);
        }
    }

    /**
     * Sets state.
     *
     * @param s the s
     */
    public void setState(OrderState s) {
        this.state = s;
    }

    /**
     * Is completed boolean.
     *
     * @return the boolean
     */
    public boolean isCompleted() {
        return state == OrderState.COMPLETED || state == OrderState.COMPLETEDBACKORDER;
    }

}
