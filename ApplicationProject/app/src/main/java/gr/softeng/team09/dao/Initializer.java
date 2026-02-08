package gr.softeng.team09.dao;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Initializer.
 */
public abstract class Initializer {

    /**
     * The constant DEPON_EOF.
     */
    public static final int DEPON_EOF = 1001;
    /**
     * The constant PONSTAN_EOF.
     */
    public static final int PONSTAN_EOF = 1002;
    /**
     * The constant VITAMIN_C_EOF.
     */
    public static final int VITAMIN_C_EOF = 1003;
    /**
     * The constant AUGMENTIN_EOF.
     */
    public static final int AUGMENTIN_EOF = 1004;
    /**
     * The constant PANADOL_EOF.
     */
    public static final int PANADOL_EOF = 1005;
    /**
     * The constant ASPIRIN_EOF.
     */
    public static final int ASPIRIN_EOF = 1006;
    /**
     * The constant NUROFEN_EOF.
     */
    public static final int NUROFEN_EOF = 1007;
    /**
     * The constant MESULID_EOF.
     */
    public static final int MESULID_EOF = 1008;

    /**
     * The constant AMOXIL_EOF.
     */
    public static final int AMOXIL_EOF = 1009;
    /**
     * The constant KLARICID_EOF.
     */
    public static final int KLARICID_EOF = 1010;
    /**
     * The constant ZITHROMAX_EOF.
     */
    public static final int ZITHROMAX_EOF = 1011;
    /**
     * The constant VIBRAMYCIN_EOF.
     */
    public static final int VIBRAMYCIN_EOF = 1012;
    /**
     * The constant CEFACLOR_EOF.
     */
    public static final int CEFACLOR_EOF = 1013;

    /**
     * The constant VITAMIN_D3_EOF.
     */
    public static final int VITAMIN_D3_EOF = 1014;
    /**
     * The constant MAGNESIUM_EOF.
     */
    public static final int MAGNESIUM_EOF = 1015;
    /**
     * The constant IRON_EOF.
     */
    public static final int IRON_EOF = 1016;
    /**
     * The constant MULTIVITAMIN_EOF.
     */
    public static final int MULTIVITAMIN_EOF = 1017;
    /**
     * The constant OMEGA3_EOF.
     */
    public static final int OMEGA3_EOF = 1018;

    /**
     * The constant FUCIDIN_EOF.
     */
    public static final int FUCIDIN_EOF = 1019;
    /**
     * The constant VOLTAREN_EOF.
     */
    public static final int VOLTAREN_EOF = 1020;
    /**
     * The constant ZIRTEK_EOF.
     */
    public static final int ZIRTEK_EOF = 1021;
    /**
     * The constant LOSEC_EOF.
     */
    public static final int LOSEC_EOF = 1022;
    /**
     * The constant GLUCOPHAGE_EOF.
     */
    public static final int GLUCOPHAGE_EOF = 1023;
    /**
     * The constant LIPITOR_EOF.
     */
    public static final int LIPITOR_EOF = 1024;

    /**
     * The constant PHARMACIST_EMAIL.
     */
    public static final String PHARMACIST_EMAIL = "pharmacist@gmail.gr";
    /**
     * The constant CLERK_EMAIL.
     */
    public static final String CLERK_EMAIL = "clerk@gmail.gr";
    /**
     * The constant PHARMACY_AFM.
     */
    public static final int PHARMACY_AFM = 1;


    /**
     * Erase data.
     */
    protected abstract void eraseData();

    /**
     * Prepare data.
     */
    public void prepareData() {
        eraseData();

        Product depon = new Product("Depon", ProductCategory.ANALGESIC, DEPON_EOF, 2.50);
        Product ponstan = new Product("Ponstan", ProductCategory.ANALGESIC, PONSTAN_EOF, 3.50);
        Product vitaminC = new Product("Vitamin C", ProductCategory.VITAMIN, VITAMIN_C_EOF, 5.00);
        Product augmentin = new Product("Augmentin", ProductCategory.ANTIBIOTIC, AUGMENTIN_EOF, 8.50);
        Product panadol = new Product("Panadol Extra", ProductCategory.ANALGESIC, PANADOL_EOF, 2.20);
        Product aspirin = new Product("Aspirin", ProductCategory.ANALGESIC, ASPIRIN_EOF, 1.80);
        Product nurofen = new Product("Nurofen Express", ProductCategory.ANALGESIC, NUROFEN_EOF, 4.50);
        Product mesulid = new Product("Mesulid", ProductCategory.ANALGESIC, MESULID_EOF, 3.80);
        Product amoxil = new Product("Amoxil 500mg", ProductCategory.ANTIBIOTIC, AMOXIL_EOF, 5.50);
        Product klaricid = new Product("Klaricid", ProductCategory.ANTIBIOTIC, KLARICID_EOF, 9.20);
        Product zithromax = new Product("Zithromax", ProductCategory.ANTIBIOTIC, ZITHROMAX_EOF, 7.80);
        Product vibramycin = new Product("Vibramycin", ProductCategory.ANTIBIOTIC, VIBRAMYCIN_EOF, 4.20);
        Product cefaclor = new Product("Cefaclor", ProductCategory.ANTIBIOTIC, CEFACLOR_EOF, 6.50);
        Product d3 = new Product("Vitamin D3 2000iu", ProductCategory.VITAMIN, VITAMIN_D3_EOF, 8.00);
        Product mag = new Product("Magnesium B6", ProductCategory.VITAMIN, MAGNESIUM_EOF, 12.50);
        Product iron = new Product("Iron/SiderAL", ProductCategory.VITAMIN, IRON_EOF, 16.00);
        Product multi = new Product("Centrum Multi", ProductCategory.VITAMIN, MULTIVITAMIN_EOF, 15.00);
        Product omega = new Product("Omega 3 Fish Oil", ProductCategory.VITAMIN, OMEGA3_EOF, 18.50);
        Product fucidin = new Product("Fucidin Cream", ProductCategory.OTHER, FUCIDIN_EOF, 5.20);
        Product voltaren = new Product("Voltaren Emulgel", ProductCategory.ANALGESIC, VOLTAREN_EOF, 6.80); // Μπορεί να μπει και στα αναλγητικά
        Product zirtek = new Product("Zirtek Antihistamine", ProductCategory.OTHER, ZIRTEK_EOF, 4.00);
        Product losec = new Product("Losec 20mg", ProductCategory.OTHER, LOSEC_EOF, 7.50);
        Product glucophage = new Product("Glucophage 1000mg", ProductCategory.OTHER, GLUCOPHAGE_EOF, 3.20);
        Product lipitor = new Product("Lipitor 20mg", ProductCategory.OTHER, LIPITOR_EOF, 9.50);

        getProductDAO().save(depon);
        getProductDAO().save(ponstan);
        getProductDAO().save(vitaminC);
        getProductDAO().save(augmentin);
        getProductDAO().save(panadol);
        getProductDAO().save(aspirin);
        getProductDAO().save(nurofen);
        getProductDAO().save(mesulid);
        getProductDAO().save(amoxil);
        getProductDAO().save(klaricid);
        getProductDAO().save(zithromax);
        getProductDAO().save(vibramycin);
        getProductDAO().save(cefaclor);
        getProductDAO().save(d3);
        getProductDAO().save(mag);
        getProductDAO().save(iron);
        getProductDAO().save(multi);
        getProductDAO().save(omega);
        getProductDAO().save(fucidin);
        getProductDAO().save(voltaren);
        getProductDAO().save(zirtek);
        getProductDAO().save(losec);
        getProductDAO().save(glucophage);
        getProductDAO().save(lipitor);


        Pharmacist owner = new Pharmacist("Νίκος", "Παπαδόπουλος", PHARMACIST_EMAIL, 697123456, "1");
        getPharmacistDAO().save(owner);


        Address address = new Address("Πανεπιστημίου", 10, "Αθήνα", "10564");

        Pharmacy pharmacy = new Pharmacy("Pharmacy", PHARMACY_AFM, owner, address, "1");
        getPharmacyDAO().save(pharmacy);


        Clerk clerk = new Clerk("Clerk", "Clerk", "69", CLERK_EMAIL, "1");
        getClerkDAO().save(clerk);

        MemoryStore.initInventoryIfNeeded(clerk, getBatchDAO(), getInventoryLineDAO(), getBackorderDAO(), getReservationDAO());
        Inventory inventory = MemoryStore.getInventory();


        Batch b1 = new Batch(1, depon, 50, 50);
        inventory.receiveBatch(b1);


        Batch b2 = new Batch(2, ponstan, 20, 20);
        inventory.receiveBatch(b2);


        Batch b3 = new Batch(3, vitaminC, 100, 100);
        inventory.receiveBatch(b3);


        inventory.receiveBatch(new Batch(4, panadol, 40, 40));

        inventory.receiveBatch(new Batch(5, aspirin, 60, 60));

        inventory.receiveBatch(new Batch(6, nurofen, 25, 25));


        inventory.receiveBatch(new Batch(7, amoxil, 15, 15));

        inventory.receiveBatch(new Batch(8, klaricid, 10, 10));


        inventory.receiveBatch(new Batch(9, d3, 80, 80));

        inventory.receiveBatch(new Batch(10, mag, 45, 45));

        inventory.receiveBatch(new Batch(11, multi, 50, 50));


        inventory.receiveBatch(new Batch(12, fucidin, 20, 20));

        inventory.receiveBatch(new Batch(13, voltaren, 35, 35));


        inventory.receiveBatch(new Batch(14, zirtek, 60, 60));



        Order o = new Order(1,pharmacy);
        o.addLine(new OrderLine(depon,3));
        getOrderDAO().saveDraft(o);

        Order o2 = new Order(2,pharmacy);
        o2.addLine(new OrderLine(aspirin,5));
        getOrderDAO().saveDraft(o2);

    }


    /**
     * Gets product dao.
     *
     * @return the product dao
     */
    protected abstract ProductDAO getProductDAO();

    /**
     * Gets pharmacist dao.
     *
     * @return the pharmacist dao
     */
    protected abstract PharmacistDAO getPharmacistDAO();

    /**
     * Gets pharmacy dao.
     *
     * @return the pharmacy dao
     */
    protected abstract PharmacyDAO getPharmacyDAO();

    /**
     * Gets clerk dao.
     *
     * @return the clerk dao
     */
    protected abstract ClerkDAO getClerkDAO();

    /**
     * Gets batch dao.
     *
     * @return the batch dao
     */
    protected abstract BatchDAO getBatchDAO();

    /**
     * Gets order dao.
     *
     * @return the order dao
     */
    protected abstract OrderDAO getOrderDAO();

    /**
     * Gets inventory line dao.
     *
     * @return the inventory line dao
     */
    protected abstract InventoryLineDAO getInventoryLineDAO();

    /**
     * Gets backorder dao.
     *
     * @return the backorder dao
     */
    protected abstract BackorderDAO getBackorderDAO();

    /**
     * Gets reservation dao.
     *
     * @return the reservation dao
     */
    protected abstract ReservationDAO getReservationDAO();
}