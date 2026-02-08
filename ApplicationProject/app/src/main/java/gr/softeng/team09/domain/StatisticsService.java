package gr.softeng.team09.domain;

import java.util.*;

/**
 * The type Statistics service.
 */
public class StatisticsService {

    private final List<Order> completedOrders;
    private final List<Order> allOrders;
    private final List<Order> pendingOrders = new ArrayList<>();

    /**
     * Instantiates a new Statistics service.
     *
     * @param completedOrders the completed orders
     * @param allOrders       the all orders
     */
    public StatisticsService(List<Order> completedOrders, List<Order> allOrders) {
        if (completedOrders == null || allOrders == null)
            throw new IllegalArgumentException("orders list cannot be null");
        this.completedOrders = completedOrders;
        this.allOrders = allOrders;
    }

    private static int compareTimestamps(long a, long b) {
        return Long.compare(a, b);
    }

    /**
     * Gets orders by pharmacy.
     *
     * @param afm the afm
     * @return the orders by pharmacy
     */
    public List<Order> getOrdersByPharmacy(int afm) {

        List<Order> result = new ArrayList<>();


        for (Order o : completedOrders) {
            if (!isCountedAsSale(o)) continue;

            if (o.getPharmacy().getAfm() == afm) {
                result.add(o);
            }
        }

        result.sort(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return compareTimestamps(
                        o1.getCreationDate(),
                        o2.getCreationDate()
                );
            }
        });

        return result;
    }

    /**
     * Gets revenue by pharmacy.
     *
     * @param afm the afm
     * @return the revenue by pharmacy
     */
//REVENUE BY PHARMACY
    public double getRevenueByPharmacy(int afm) {
        double total = 0.0;

        for (Order o : completedOrders) {
            if (!isCountedAsSale(o)) continue;

            if (o.getPharmacy().getAfm() == afm) {
                for (OrderLine l : o.getLines()) {
                    total += l.getQuantity() * l.getProduct().getPriceWithVAT();
                }
            }
        }

        return total;
    }

    /**
     * Gets product sales between.
     *
     * @param start the start
     * @param end   the end
     * @return the product sales between
     */
//PRODUCT SALES IN TIME RANGE (timestamps)
    public Map<Product, Integer> getProductSalesBetween(long start, long end) {

        Map<Product, Integer> sales = new HashMap<>();

        for (Order o : completedOrders) {
            if (!isCountedAsSale(o)) continue;

            long t = o.getCreationDate();

            if (t < start) continue;
            if (t > end) continue;

            for (OrderLine l : o.getLines()) {
                Product pr = l.getProduct();
                int qty = l.getQuantity();

                sales.put(pr, sales.getOrDefault(pr, 0) + qty);
            }
        }

        return sales;
    }

    /**
     * Gets total revenue.
     *
     * @return the total revenue
     */
//TOTAL REVENUE
    public double getTotalRevenue() {
        double total = 0.0;

        for (Order o : completedOrders) {
            if (!isCountedAsSale(o)) continue;

            for (OrderLine l : o.getLines()) {
                total += l.getQuantity() * l.getProduct().getPriceWithVAT();
            }
        }

        return total;
    }


    //=============== Helper ===================

    private boolean isCountedAsSale(Order o) {
        if (o == null) return false;
        OrderState s = o.getState();
        return s == OrderState.COMPLETED || s == OrderState.COMPLETEDBACKORDER;
    }
}
