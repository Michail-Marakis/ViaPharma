package gr.softeng.team09.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Pharmacy.
 */
public class Pharmacy {

    private String name;
    private final int afm;
    private Address address;
    private String password;
    private StatisticsService s;
    /**
     * The Draft orders.
     */
    public List<Order> draftOrders = new ArrayList<>();
    /**
     * The Personal pending orders.
     */
    public List<Order> personalPendingOrders = new ArrayList<>();

    /**
     * The Cart.
     */
    public List<OrderLine> cart = new ArrayList<>();



    // pharmacy has 1 pharmacist
    private Pharmacist owner;

    /**
     * Instantiates a new Pharmacy.
     *
     * @param name     the name
     * @param afm      the afm
     * @param owner    the owner
     * @param address  the address
     * @param password the password
     */
    public Pharmacy(String name, int afm, Pharmacist owner, Address address, String password) {
        this.name = name;
        this.afm = afm;
        this.setOwner(owner);
        this.address = address;
        this.password = password;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Get password string.
     *
     * @return the string
     */
    public String getPassword(){
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

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public Pharmacist getOwner() { return owner; }

    /**
     * Gets afm.
     *
     * @return the afm
     */
    public int getAfm() {
        return afm;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(Pharmacist owner) {
        this.owner = owner;
        if (owner != null && !owner.getPharmacies().contains(this)) {
            owner.addPharmacy(this);
        }
    }

    /**
     * Add to cart.
     *
     * @param order     the order
     * @param orderLine the order line
     */
    public void addToCart(Order order, OrderLine orderLine) {
        boolean found = false;

        for (OrderLine ol : order.getLines()) {
            if (ol.getProduct().equals(orderLine.getProduct())) {
                ol.setQuantity(ol.getQuantity() + orderLine.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            order.addLine(orderLine);
            cart.add(orderLine);
        }
    }

    /**
     * Get cart list.
     *
     * @return the list
     */
    public List<OrderLine> getCart(){
        return cart;
    }

    /**
     * Calculate order total double.
     *
     * @param order the order
     * @return the double
     */
// υπολογισμός συνολικού ποσού παραγγελίας (με βάση priceWithVAT)
    public double calculateOrderTotal(Order order) {
        Objects.requireNonNull(order);
        double total = 0.0;
        for (OrderLine l : order.getLines()) {
            total += l.getQuantity() * l.getProduct().getPriceWithVAT();
        }
        return total;
    }

    /**
     * Payment boolean.
     *
     * @param budjet the budjet
     * @param order  the order
     * @return the boolean
     */
    public boolean payment(int budjet, Order order){
        return budjet >= calculateOrderTotal(order);
    }

    /**
     * Check availability for order map.
     *
     * @param order the order
     * @param inv   the inv
     * @return the map
     */
    public Map<Product, Integer> checkAvailabilityForOrder(Order order, Inventory inv) {

        Map<Product, Integer> result = new HashMap<Product, Integer>();

        List<OrderLine> lines = order.getLines();
        for (int i = 0; i < lines.size(); i++) {
            OrderLine line = lines.get(i);

            Product product = line.getProduct();
            int requested = line.getQuantity();

            InventoryLine invLine = inv.findLine(product);
            int available;
            if (invLine != null) {
                available = invLine.getQuantity();
            } else {
                available = 0;
            }

            int canServe;
            if (requested < available) {
                canServe = requested;
            } else {
                canServe = available;
            }

            result.put(product, canServe);
        }

        return result;
    }

    /**
     * Update order with availability.
     *
     * @param order        the order
     * @param availability the availability
     */
    public void updateOrderWithAvailability(Order order, Map<Product, Integer> availability) {
        List<OrderLine> lines = order.getLines();
        List<OrderLine> toRemove = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            OrderLine line = lines.get(i);
            Product product = line.getProduct();

            Integer canServeObj = availability.get(product);
            int canServe;
            if (canServeObj == null) {
                canServe = 0;
            } else {
                canServe = canServeObj;
            }

            if (canServe <= 0) {
                toRemove.add(line);
            } else if (canServe < line.getQuantity()) {
                line.setQuantity(canServe);
            }
        }

        //αφαιρώ τις γραμμές που δεν εξυπηρετούνται
        for (int i = 0; i < toRemove.size(); i++) {
            OrderLine removeLine = toRemove.get(i);
            lines.remove(removeLine);
        }
    }

    /**
     * Submit order.
     *
     * @param order           the order
     * @param inv             the inv
     * @param budget          the budget
     * @param allowBackorders the allow backorders
     */
    public void submitOrder(Order order, Inventory inv, int budget, boolean allowBackorders) {
        Objects.requireNonNull(order);
        Objects.requireNonNull(inv);

        if(!allowBackorders) { //αν δεν θελει backorder, αλλαζει η παραγγελια για να πληρωσει λιγότερα
            updateOrderWithAvailability(order, checkAvailabilityForOrder(order, inv)); //διαθέσιμα προιόντα για την παραγγελία, ενημερωση
        }else{
            System.out.println(checkAvailabilityForOrder(order, inv)); //ενημερωση για το ποια προιοντα υπάρχουν διαθεσιμα
        }

        if (!payment(budget, order)) {
            boolean exists = false;
            for(Order or : draftOrders){
                if(or.getId() == order.getId()){
                    exists = true;
                    break;
                }
            }
            if(!exists) draftOrders.add(order);
            return;
        }
        draftOrders.remove(order);
        personalPendingOrders.add(order);
        order.setState(OrderState.PENDING);
        inv.addToAll(order);


    }


    /**
     * Delete submitted orders.
     *
     * @param orderToCancel the order to cancel
     * @param inv           the inv
     */
    public void deleteSubmittedOrders(Order orderToCancel, Inventory inv){
            for(Order or: personalPendingOrders) {
                if (or.getId() == orderToCancel.getId()) {
                    personalPendingOrders.remove(or);
                    System.out.println("It was succesfully removed");
                    break;
                }
            }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pharmacy pharmacy = (Pharmacy) o;
        return afm == pharmacy.afm; //Ταυτοποίηση βάσει ΑΦΜ
    }

    @Override
    public int hashCode() {
        return Objects.hash(afm);
    }
}
