package gr.softeng.team09.view.ClerkShowStats;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.StatisticsService;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Clerk show stats presenter.
 */
public class ClerkShowStatsPresenter {

    private ClerkShowStatsView view;

    private final Inventory inventory = MemoryStore.getInventory();
    private StatisticsService statisticsService;

    private List<Order> lastShownOrders = new ArrayList<>();

    /**
     * Instantiates a new Clerk show stats presenter.
     */
    public ClerkShowStatsPresenter() {
        refreshStatisticsService();
    }

    private void refreshStatisticsService() {
        statisticsService = new StatisticsService(
                inventory.getCompletedOrders(),
                inventory.getAllOrders()
        );
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(ClerkShowStatsView view) {
        this.view = view;
    }

    /**
     * Gets order at.
     *
     * @param position the position
     * @return the order at
     */
    public Order getOrderAt(int position) {
        if (position < 0 || position >= lastShownOrders.size()) return null;
        return lastShownOrders.get(position);
    }

    /**
     * Show orders by pharmacy.
     *
     * @param afmText the afm text
     */
// ---------------- Orders per pharmacy (AFM) ----------------
    public void showOrdersByPharmacy(String afmText) {

        refreshStatisticsService();

        if (afmText == null || afmText.trim().isEmpty()) {
            view.showErrorOnOrdersInput("AFM");
            return;
        }

        final int afm;
        try {
            afm = Integer.parseInt(afmText.trim());
        } catch (NumberFormatException e) {
            view.showErrorOnOrdersInput("Invalid AFM");
            return;
        }

        List<Order> orders = statisticsService.getOrdersByPharmacy(afm);
        lastShownOrders = orders;

        List<String> display = new ArrayList<>();

        for (Order o : orders) {
            double total = 0.0;
            for (OrderLine l : o.getLines()) {
                total += l.getQuantity() * l.getProduct().getPriceWithVAT();
            }

            String type;
            if (o.getState() == OrderState.COMPLETEDBACKORDER) {
                type = "BACKORDER";
            } else {
                type = "ORDER";
            }

            display.add(
                    "ID: " + o.getId() +
                            " | Type: " + type +
                            " | Total: " + String.format("%.2f", total)
            );
        }

        view.showOrders(display);
    }

    /**
     * Show revenue by pharmacy.
     *
     * @param afmText the afm text
     */
// ---------------- Revenue by pharmacy (AFM) ----------------
    public void showRevenueByPharmacy(String afmText) {

        refreshStatisticsService();

        if (afmText == null || afmText.trim().isEmpty()) {
            view.showErrorOnRevenueInput("AFM");
            return;
        }

        final int afm;
        try {
            afm = Integer.parseInt(afmText.trim());
        } catch (NumberFormatException e) {
            view.showErrorOnRevenueInput("Invalid AFM");
            return;
        }

        double revenue = statisticsService.getRevenueByPharmacy(afm);
        view.showRevenue("Revenue generated: " + String.format("%.2f", revenue) + " €");
    }

    /**
     * Show total revenue.
     */
// ---------------- Total revenue ----------------
    public void showTotalRevenue() {

        refreshStatisticsService();

        double totalRevenue = statisticsService.getTotalRevenue();
        view.showTotalRevenue("Total revenue generated: " +
                String.format("%.2f", totalRevenue) + " €");
    }

    // ---------------- Sales between dates (d/M/yyyy) ----------------

    private static final DateTimeFormatter DATE_FMT = new DateTimeFormatterBuilder()
            .appendPattern("d/M/uuuu")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

    private long toStartOfDayMillis(String dateText) {
        LocalDate d = LocalDate.parse(dateText, DATE_FMT);
        return d.atStartOfDay(ZoneId.of("Europe/Athens")).toInstant().toEpochMilli();
    }

    private long toEndOfDayMillis(String dateText) {
        LocalDate d = LocalDate.parse(dateText, DATE_FMT);
        return d.plusDays(1)
                .atStartOfDay(ZoneId.of("Europe/Athens"))
                .toInstant()
                .toEpochMilli() - 1L;
    }

    /**
     * Show sales between.
     *
     * @param startText the start text
     * @param endText   the end text
     */
    public void showSalesBetween(String startText, String endText) {

        refreshStatisticsService();

        if (startText == null || startText.trim().isEmpty()) {
            view.showErrorOnStartDateInput("Use d/M/yyyy");
            return;
        }
        if (endText == null || endText.trim().isEmpty()) {
            view.showErrorOnEndDateInput("Use d/M/yyyy");
            return;
        }

        final long start;
        final long end;

        try {
            start = toStartOfDayMillis(startText.trim());
        } catch (Exception e) {
            view.showErrorOnStartDateInput("Invalid date");
            return;
        }

        try {
            end = toEndOfDayMillis(endText.trim());
        } catch (Exception e) {
            view.showErrorOnEndDateInput("Invalid date");
            return;
        }

        long s = start;
        long e = end;
        if (s > e) {
            long tmp = s;
            s = e;
            e = tmp;
        }

        Map<Product, Integer> sales = statisticsService.getProductSalesBetween(s, e);

        List<String> displayList = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : sales.entrySet()) {
            displayList.add(
                    entry.getKey().getName().toUpperCase()+" with EOF code: "+ entry.getKey().getEofyCode() + " and price: " + entry.getKey().getPrice() + "€ sold: " + entry.getValue()
            );
        }

        view.showSalesBetween(displayList);
    }
}
