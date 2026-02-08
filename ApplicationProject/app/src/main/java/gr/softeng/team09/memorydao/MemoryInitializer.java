package gr.softeng.team09.memorydao;

import gr.softeng.team09.dao.*;

/**
 * The type Memory initializer.
 */
public class MemoryInitializer extends gr.softeng.team09.dao.Initializer {

    @Override
    public void eraseData() {

        ProductDAOMemory.entities.clear();
        PharmacistDAOMemory.entities.clear();
        PharmacyDAOMemory.entitiesPharmacy.clear();
        ClerkDAOMemory.entitiesClerk.clear();


        MemoryStore.clearSessionPharma();
        MemoryStore.clearSessionClerk();

    }

    @Override
    public ProductDAO getProductDAO() {
        return new ProductDAOMemory();
    }

    @Override
    public PharmacistDAO getPharmacistDAO() {
        return new PharmacistDAOMemory();
    }

    @Override
    public PharmacyDAO getPharmacyDAO() {
        return new PharmacyDAOMemory();
    }

    @Override
    public ClerkDAO getClerkDAO() {
        return new ClerkDAOMemory();
    }

    @Override
    public BatchDAO getBatchDAO() {
        return new BatchDAOMemory();
    }

    @Override
    public OrderDAO getOrderDAO() {
        return new OrderDAOMemory();
    }

    @Override
    public InventoryLineDAO getInventoryLineDAO() {
        return new InventoryLineDAOMemory();
    }

    @Override
    public BackorderDAO getBackorderDAO() {
        return new BackorderDAOMemory();
    }

    @Override
    public ReservationDAO getReservationDAO() {
        return new ReservationDAOMemory();
    }
}