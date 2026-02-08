package gr.softeng.team09.domain;

/**
 * The enum Order state.
 */
public enum OrderState {
    /**
     * Pending order state.
     */
    PENDING,    //PENDING περιμενει αποθεμα
    /**
     * Draft order state.
     */
    DRAFT,      //ολες είναι draft μέχρι να υποβληθούν
    /**
     * Completed order state.
     */
    COMPLETED,  //ολοκληρωμένη
    /**
     * Canceled order state.
     */
    CANCELED,   //ακύρωση παραγγελίας
    /**
     * Tocancel order state.
     */
    TOCANCEL,    //θελει να ακυρωθει η παραγγελια του
    /**
     * Backorder order state.
     */
    BACKORDER,
    /**
     * Completedbackorder order state.
     */
    COMPLETEDBACKORDER
}