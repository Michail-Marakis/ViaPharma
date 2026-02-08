package gr.softeng.team09.domain;

import java.util.*;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.dao.OrderDAO;
import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.memorydao.OrderDAOMemory;


/**
 * The type Inventory.
 */
public class Inventory {


    private final InventoryLineDAO inventoryLineDAO;
    /**
     * The Backorder dao.
     */
    public final BackorderDAO backorderDAO;
    /**
     * The Reservation dao.
     */
    public final ReservationDAO reservationDAO;
    private int totalbatches = 0;

    private OrderDAO orderDAO = new OrderDAOMemory();

    private final BatchDAO batchDAO;
    //private PharmacyShowProductsView view;

    private final List<Order> completedOrders = new ArrayList<>();
    private final List<Order> allOrders = new ArrayList<>();

    private  Clerk owner;
    private int backorderCounter = 1;
    private List<Clerk> owners = new ArrayList<>();

    /**
     * Instantiates a new Inventory.
     *
     * @param clerk            the clerk
     * @param batchDAO         the batch dao
     * @param inventoryLineDAO the inventory line dao
     * @param backorderDAO     the backorder dao
     * @param reservationDAO   the reservation dao
     */
    public Inventory(Clerk clerk,
                     BatchDAO batchDAO,
                     InventoryLineDAO inventoryLineDAO,
                     BackorderDAO backorderDAO,
                     ReservationDAO reservationDAO) {

        this.owner = clerk;
        owners.add(this.owner);
        this.batchDAO = Objects.requireNonNull(batchDAO);
        this.inventoryLineDAO = Objects.requireNonNull(inventoryLineDAO);
        this.backorderDAO = Objects.requireNonNull(backorderDAO);
        this.reservationDAO = Objects.requireNonNull(reservationDAO);
    }

    /**
     * Set owner.
     *
     * @param owner the owner
     */
    public void setOwner(Clerk owner){ this.owner = owner;}

    /**
     * Gets all batches.
     *
     * @return the all batches
     */
    public List<Batch> getAllBatches() {
        return batchDAO.findAll();
    }

    /**
     * Add to all.
     *
     * @param order the order
     */
    public void addToAll(Order order){
        allOrders.add(order);
    }

    /**
     * Get owners list.
     *
     * @return the list
     */
    public List<Clerk> getOwners(){
        return new ArrayList<>(owners);
    }

    private int generateBackorderId() {
        return backorderCounter++;
    }


    /**
     * Get owner clerk.
     *
     * @return the clerk
     */
    public Clerk getOwner(){
        return owner;
    }


    /**
     * Find line inventory line.
     *
     * @param product the product
     * @return the inventory line
     */
    public InventoryLine findLine(Product product) {
        return inventoryLineDAO.findByProduct(product);
    }

    /**
     * Gets available items.
     *
     * @return the available items
     */
    public List<InventoryLine> getAvailableItems() {
        return inventoryLineDAO.findAll();
    }

    /**
     * Add backorder.
     *
     * @param bo the bo
     */
    public void addBackorder(Backorder bo) {
        if (bo == null) return;
        backorderDAO.enqueue(bo);
    }

    /**
     * Gets back from one pharm.
     *
     * @param afm the afm
     * @return the back from one pharm
     */
    public List<Backorder> getBackFromOnePharm(int afm) {
        if (afm == -1) return new ArrayList<>();
        List<Backorder> res = new ArrayList<>();
        for (Backorder bo : backorderDAO.findAll()) {
            if (bo.getPharmacy().getAfm() == afm) {
                res.add(bo);
            }
        }
        return res;
    }

    private InventoryLine ensureLine(Product product) {
        InventoryLine l = inventoryLineDAO.findByProduct(product);
        if (l == null) {
            l = new InventoryLine(product, 0);
            inventoryLineDAO.save(l);
        }
        return l;
    }


    /**
     * Gets backorders.
     *
     * @return the backorders
     */
    public List<Backorder> getBackorders() {
        return backorderDAO.findAll();
    }

    /**
     * Gets completed orders.
     *
     * @return the completed orders
     */
    public List<Order> getCompletedOrders() {
        return new ArrayList<>(completedOrders);
    }

    /**
     * Gets all orders.
     *
     * @return the all orders
     */
    public List<Order> getAllOrders() {
        return allOrders;
    }


    /**
     * Αποδέσμευση κρατημένων αποθεμάτων για μια παραγγελία.
     * Επαναφέρουμε τις ποσότητες στο free stock.
     *
     * @param order the order
     */
    public void unreserveStockForOrder(Order order) {
        if(order == null) return;
        Map<Product, Integer> reservedForThisOrder = reservationDAO.get(order.getId());
        if (reservedForThisOrder == null) return;

        for (Map.Entry<Product, Integer> e : reservedForThisOrder.entrySet()) {
            Product p = e.getKey();
            int qty = e.getValue();

            InventoryLine line = ensureLine(p);
            line.increase(qty); // επιστροφή στο free stock
            inventoryLineDAO.save(line);
        }
        reservationDAO.remove(order.getId());
        System.out.println("Order " + order.getId() + " reserved stock released.");
    }

    /**
     * Βασική ροή εκτέλεσης:
     * - προσπαθεί να κάνει reserve,
     * - αν δεν γίνει reserve:
     *   - αν ΔΕΝ επιτρέπονται backorders -> CANCELED,
     *   - αν επιτρέπονται -> PENDING (για πιθανό backorder / μελλοντική εκτέλεση),
     * - αν γίνει reserve, καλεί fulfillOrder.
     */

    private Order findOrderById(int id) {
        for (Order o : allOrders) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }


    /**
     * Cancel order by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean cancelOrderById(int id) {
        Order order = findOrderById(id);
        if (order == null) {
            System.out.println("This order doesnt exist");
            return false;
        }

        addToAllOrdersIfMissing(order);


        if (order.getState() == OrderState.COMPLETED) {
            System.out.println("Completed order cannot be canceled");
            return false;
        }

        if (order.getState() == OrderState.CANCELED) {
            System.out.println("Order already canceled");
            return false;
        }
        if(order.getState() == OrderState.PENDING){
            return false;
        }
        if(order.getState() == OrderState.DRAFT){
            return false;
        }
        if(order.getState() == OrderState.BACKORDER){
            return false;
        }

        if (order.getState() == OrderState.TOCANCEL) {
            unreserveStockForOrder(order);
            order.setState(OrderState.CANCELED);
        }
        return true;
    }


    private void addToAllOrdersIfMissing(Order order) {
        boolean exists = false;
        for (Order o : allOrders) {
            if (o.getId() == order.getId()) {
                exists = true;
                break;
            }
        }
        if (!exists) allOrders.add(order);
    }


    /**
     * Execute order.
     *
     * @param order           the order
     * @param allowBackorders the allow backorders
     */
    public void executeOrder(Order order, boolean allowBackorders) {
        Objects.requireNonNull(order);
        addToAllOrdersIfMissing(order);


        Map<Product, Integer> toShipMap = new HashMap<>();
        Map<Product, Integer> toBackorderMap = new HashMap<>();

        for (OrderLine ol : order.getLines()) {
            Product p = ol.getProduct();
            int requested = ol.getQuantity();

            InventoryLine line = ensureLine(p);
            int available = line.getQuantity();

            int shipQty = Math.min(requested, Math.max(0, available));
            int backQty = requested - shipQty;

            if (shipQty > 0) toShipMap.put(p, shipQty);
            if (backQty > 0) toBackorderMap.put(p, backQty);
        }

        boolean anyShip = !toShipMap.isEmpty();
        boolean anyBack = !toBackorderMap.isEmpty();

        if (!anyShip) {
            if (!allowBackorders) {
                order.setState(OrderState.CANCELED);
                orderDAO.deletePending(order);
                return;
            }
            order.setState(OrderState.BACKORDER);
            orderDAO.savePending(order);
            return;
        }


        for (Map.Entry<Product, Integer> e : toShipMap.entrySet()) {
            Product p = e.getKey();
            int qty = e.getValue();

            InventoryLine line = ensureLine(p);
            line.decrease(qty);
            inventoryLineDAO.save(line);


            allocateFromBatches(p, order.getPharmacy(), qty);
        }


        Iterator<OrderLine> it = order.getLines().iterator();
        while (it.hasNext()) {
            OrderLine ol = it.next();
            Product p = ol.getProduct();
            int shipQty = toShipMap.getOrDefault(p, 0);

            if (shipQty <= 0) {
                it.remove();
            } else {
                ol.setQuantity(shipQty);
            }
        }


        if (anyBack) {

            int newId = generateBackorderId();

            Backorder backOrder = new Backorder(newId, order.getPharmacy());

            for (Map.Entry<Product, Integer> e : toBackorderMap.entrySet()) {
                Product p = e.getKey();
                int qty = e.getValue();
                backOrder.addLine(new BackorderLine(p, qty));
            }
            backorderDAO.enqueue(backOrder);
        }

        order.setState(OrderState.COMPLETED);
        orderDAO.deletePending(order);
        orderDAO.saveCompleted(order);
        completedOrders.add(order);
    }


    /**
     * Execute order by id boolean.
     *
     * @param id              the id
     * @param allowBackorders the allow backorders
     * @return the boolean
     */
    public boolean executeOrderById(int id, boolean allowBackorders) {
        Order order = findOrderById(id);

        if (order == null) {
            System.out.println("This order doesnt exist");
            return false;
        }
        if(order.getState() == OrderState.PENDING) {
            executeOrder(order, allowBackorders);
            return true;
        }
        return false;
    }


    /**
     * Ο διαχειριστής αναβάλλει / ακυρώνει την εκτέλεση:
     * - αποδεσμεύονται τα δεσμευμένα αποθέματα,
     * - η παραγγελία δεν "πεθαίνει" αλλά μένει PENDING
     * ώστε ο πελάτης να μπορεί να επιλέξει backorders / νέα εκτέλεση.
     *
     * @param order the order
     */
    public void postponeOrderOnPurpose(Order order) {
        if (order == null) return;
        if(order.getState() == OrderState.PENDING) {
            unreserveStockForOrder(order);
            System.out.println("Order " + order.getId() + " postponed on purpose (set to PENDING).");
            addToAllOrdersIfMissing(order);
        }
    }


    private Map<Integer, Order> shipmentsInCurrentReceive = null;

    /**
     * Add or increase line.
     *
     * @param order the order
     * @param p     the p
     * @param qty   the qty
     */
    public void addOrIncreaseLine(Order order, Product p, int qty) {
        for (OrderLine ol : order.getLines()) {
            if (ol.getProduct().equals(p)) {
                ol.setQuantity(ol.getQuantity() + qty);
                return;
            }
        }
        order.addLine(new OrderLine(p, qty));
    }


    /**
     * Receive batch.
     *
     * @param batch the batch
     */
//receive batch: add to batches map and increase product total, then try backorders
    public void receiveBatch(Batch batch) {
        addToTotalBatches();
        batchDAO.save(batch);
        batchDAO.BatchesCounter();
        Product p = batch.getProduct();

        InventoryLine line = ensureLine(p);
        line.increase(batch.getQuantity());
        inventoryLineDAO.save(line);

        shipmentsInCurrentReceive = new HashMap<>();
        try {
            fulfillBackordersForProduct(p);
        } finally {

            shipmentsInCurrentReceive = null;
        }
    }

    /**
     * Next batch id int.
     *
     * @return the int
     */
    public int nextBatchId() {
        return batchDAO.nextId();
    }


    /**
     * Add to total batches.
     */
    public void addToTotalBatches(){
        totalbatches++;
    }


    private StringBuilder buildBackorderCompletionEmail(Backorder bo, Map<Product, Integer> shippedNow) {
        StringBuilder sb = new StringBuilder();

        sb.append("Email sent successfully!\n\n");

        sb.append("Sent to: ").append(bo.getPharmacy().getName()).append("\n\n");

        sb.append("Context: BACKORDER COMPLETED\n");
        sb.append("Backorder ID: ").append(bo.getId()).append("\n");
        sb.append("Pharmacy: ").append(bo.getPharmacy().getName()).append("\n\n");

        sb.append("Shipped items:\n");
        if (shippedNow == null || shippedNow.isEmpty()) {
            sb.append("- No items shipped\n");
        } else {
            for (Map.Entry<Product, Integer> e : shippedNow.entrySet()) {
                Product p = e.getKey();
                Integer qty = e.getValue();
                sb.append("- ")
                        .append(p != null ? p.getName() : "Product")
                        .append(" | qty: ")
                        .append(qty)
                        .append("\n");
            }
        }

        sb.append("\nTHANKS!\nKIND REGARDS");
        sb.append("\nNAME: ").append(getOwner().getName());
        sb.append("\nPHONE NUMBER: ").append(getOwner().getPhone());
        sb.append("\nEMAIL: ").append(getOwner().getEmail());

        return sb;
    }

    private void fulfillBackordersForProduct(Product p) {
        if (p == null) return;

        // fifo
        List<Backorder> stillWaiting = new ArrayList<>();

        Backorder bo;
        while ((bo = backorderDAO.poll()) != null) {

            boolean touched = false;

            Map<Product, Integer> shippedNow = new HashMap<>();

            for (BackorderLine bol : bo.getLines()) {
                if (bol.isFulfilled()) continue;
                if (!p.equals(bol.getProduct())) continue;

                int pending = bol.getQuantityPending();

                InventoryLine line = ensureLine(p);
                int available = line.getQuantity();

                int toAllocate = Math.min(pending, available);
                if (toAllocate <= 0) continue;

                int allocated = allocateFromBatches(p, bo.getPharmacy(), toAllocate);
                if (allocated <= 0) continue;

                line.decrease(allocated);
                inventoryLineDAO.save(line);

                bol.decreasePending(allocated);
                touched = true;

                shippedNow.put(p, shippedNow.getOrDefault(p, 0) + allocated);
            }

            if (!shippedNow.isEmpty()) {

                int boId = bo.getId();
                Order shippedOrder = shipmentsInCurrentReceive.get(boId);

                if (shippedOrder == null) {
                    int shipmentId = orderDAO.nextId();
                    shippedOrder = new Order(shipmentId, bo.getPharmacy(), System.currentTimeMillis());
                    shippedOrder.setState(OrderState.COMPLETEDBACKORDER);
                    completedOrders.add(shippedOrder);
                    addToAllOrdersIfMissing(shippedOrder);
                    orderDAO.saveCompleted(shippedOrder);

                    shipmentsInCurrentReceive.put(boId, shippedOrder);
                }


                for (Map.Entry<Product, Integer> e : shippedNow.entrySet()) {
                    addOrIncreaseLine(shippedOrder, e.getKey(), e.getValue());
                }
            }

            if (touched) {
                bo.tryMarkCompleted();
                backorderDAO.save(bo);
            }

            if (bo.getState() != OrderState.COMPLETEDBACKORDER && bo.getState() != OrderState.CANCELED) {
                stillWaiting.add(bo);
            } else if (bo.getState() == OrderState.COMPLETEDBACKORDER) {
                System.out.println("Backorder " + bo.getId() +
                        " completed. Notifying " + bo.getPharmacy().getName());
                StringBuilder sb = buildBackorderCompletionEmail(bo, shippedNow);
                getOwner().setEmailsSent(sb, bo.getPharmacist());
            }
        }

        for (Backorder w : stillWaiting) {
            backorderDAO.enqueue(w);
        }
    }




    private int allocateFromBatches(Product p, Pharmacy destPharmacy, int amountNeeded) {
        if (amountNeeded <= 0) return 0;


        List<Batch> list = batchDAO.findActiveByProduct(p);
        if (list.isEmpty()) return 0;

        int remaining = amountNeeded;

        //Αν τελειώσει batch, ενημερώνουμε το DAO με removeFromActiveIfEmpty(b).
        for (Batch b : list) {
            if (remaining <= 0) break;
            if (b.getQuantity() <= 0) continue;

            int consumed = b.consume(remaining, destPharmacy);

            if (consumed > 0) {
                remaining -= consumed;



                if (b.getQuantity() == 0) {
                    batchDAO.removeFromActiveIfEmpty(b);
                }
            }
        }

        return amountNeeded - remaining;
    }

    /**
     * Recall batch.
     *
     * @param batchId the batch id
     */
    public void recallBatch(int batchId) {


        Batch b = batchDAO.find(batchId);

        if (b == null) {
            System.out.println("This batch doesnt exist");
            return;
        }

        int qtyBefore = b.getQuantity();

        for (Pharmacy ph : b.getDistributedTo()) {
            System.out.println("Recall notice: batch " + batchId +
                    " of product " + b.getProduct().getName() +
                    " for pharmacy " + ph.getName());
        }

        b.recall();

        batchDAO.removeFromActiveIfEmpty(b);

        InventoryLine line = findLine(b.getProduct());
        if (line != null) {
            line.decrease(qtyBefore);
            inventoryLineDAO.save(line);
        }
    }

}
