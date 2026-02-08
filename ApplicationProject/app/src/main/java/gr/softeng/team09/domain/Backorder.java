package gr.softeng.team09.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Backorder.
 */
public class Backorder {

    private final int id;
    private final Pharmacy pharmacy;
    private OrderState state;
    private final long creationTimestamp;
    private final List<BackorderLine> lines = new ArrayList<>();

    /**
     * Instantiates a new Backorder.
     *
     * @param id                the id
     * @param pharmacy          the pharmacy
     * @param creationTimestamp the creation timestamp
     */
    public Backorder(int id, Pharmacy pharmacy, long creationTimestamp) {
        this.id = id;
        this.pharmacy = Objects.requireNonNull(pharmacy);
        this.state = OrderState.BACKORDER;
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * Instantiates a new Backorder.
     *
     * @param id       the id
     * @param pharmacy the pharmacy
     */
//Default constructor -> timestamp = τώρα
    public Backorder(int id, Pharmacy pharmacy) {
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
     * Gets lines.
     *
     * @return the lines
     */
    public List<BackorderLine> getLines() {
        return lines;
    }

    /**
     * Add line.
     *
     * @param line the line
     */
    public void addLine(BackorderLine line) {
        if (line != null) {
            lines.add(line);
        }
    }

    /**
     * Is fully fulfilled boolean.
     *
     * @return the boolean
     */
    public boolean isFullyFulfilled() {
        for (BackorderLine l : lines) {
            if (!l.isFulfilled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Try mark completed.
     */
    public void tryMarkCompleted() {
        if (state == OrderState.BACKORDER) {
            if (isFullyFulfilled()) {
                state = OrderState.COMPLETEDBACKORDER;
            } else {
                state = OrderState.BACKORDER;
            }
        }
    }

    /**
     * Cancel.
     */
    public void cancel() {
        if (state == OrderState.BACKORDER) {
            state = OrderState.CANCELED;
        }
    }
}
