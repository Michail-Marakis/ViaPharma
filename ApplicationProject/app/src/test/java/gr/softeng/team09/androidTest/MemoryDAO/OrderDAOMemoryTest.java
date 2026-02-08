package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.OrderDAOMemory;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Order dao memory test.
 */
public class OrderDAOMemoryTest {
    private OrderDAOMemory dao;
    private Pharmacy pharmacy1;
    private Pharmacy pharmacy2;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        dao = new OrderDAOMemory();

        OrderDAOMemory.draft_entities.clear();
        OrderDAOMemory.pending_entities.clear();
        OrderDAOMemory.completed_entities.clear();

        Pharmacist owner1 = new Pharmacist("Nik", "Baglamas", "nikos@gmail.com", 69, "1");
        pharmacy1 = new Pharmacy("NikosPharma", 99, owner1, null, "1");

        Pharmacist owner2 = new Pharmacist("Mike", "Baglamas", "mike@gmail.com", 69, "1");
        pharmacy2 = new Pharmacy("MikePharma", 89, owner2, null, "1");
    }


    /**
     * Save draft operations.
     */
    @Test
    public void saveDraft_Operations() {
        Order order = new Order(1, pharmacy1);

        dao.saveDraft(order);
        assertEquals(1, OrderDAOMemory.draft_entities.size());

        dao.saveDraft(order);
        assertEquals("Should not add duplicate", 1, OrderDAOMemory.draft_entities.size());

        dao.saveDraft(null);
        assertEquals("Should ignore null", 1, OrderDAOMemory.draft_entities.size());
    }

    /**
     * Save pending operations.
     */
    @Test
    public void savePending_Operations() {
        Order order = new Order(2, pharmacy1);

        dao.savePending(order);
        assertEquals(1, OrderDAOMemory.pending_entities.size());

        dao.savePending(order);
        assertEquals(1, OrderDAOMemory.pending_entities.size());

        dao.savePending(null);
        assertEquals(1, OrderDAOMemory.pending_entities.size());
    }

    /**
     * Save completed operations.
     */
    @Test
    public void saveCompleted_Operations() {
        Order order = new Order(3, pharmacy1);

        dao.saveCompleted(order);
        assertEquals(1, OrderDAOMemory.completed_entities.size());

        dao.saveCompleted(order);
        assertEquals(1, OrderDAOMemory.completed_entities.size());

        dao.saveCompleted(null);
        assertEquals(1, OrderDAOMemory.completed_entities.size());
    }


    /**
     * Delete operations.
     */
    @Test
    public void deleteOperations() {
        Order d = new Order(1, pharmacy1);
        Order p = new Order(2, pharmacy1);
        Order c = new Order(3, pharmacy1);

        dao.saveDraft(d);
        dao.savePending(p);
        dao.saveCompleted(c);

        dao.deleteDraft(d);
        assertEquals(0, OrderDAOMemory.draft_entities.size());

        dao.deletePending(p);
        assertEquals(0, OrderDAOMemory.pending_entities.size());

        dao.deleteCompleted(c);
        assertEquals(0, OrderDAOMemory.completed_entities.size());
    }

    /**
     * Cancel removes from drafts.
     */
    @Test
    public void cancel_RemovesFromDrafts() {
        Order order = new Order(1, pharmacy1);
        dao.saveDraft(order);

        dao.cancel(order);
        assertEquals(0, OrderDAOMemory.draft_entities.size());
    }


    /**
     * Find searches all lists.
     */
    @Test
    public void find_SearchesAllLists() {
        Order draft = new Order(1, pharmacy1);
        Order completed = new Order(2, pharmacy1);
        Order pending = new Order(3, pharmacy1);

        dao.saveDraft(draft);
        dao.saveCompleted(completed);
        dao.savePending(pending);

        assertEquals(draft, dao.find(1));
        assertEquals(completed, dao.find(2));
        assertEquals(pending, dao.find(3));

        assertNull(dao.find(99));
    }

    /**
     * Find all aggregates all lists.
     */
    @Test
    public void findAll_AggregatesAllLists() {
        dao.saveDraft(new Order(1, pharmacy1));
        dao.saveCompleted(new Order(2, pharmacy1));
        dao.savePending(new Order(3, pharmacy1));

        List<Order> all = dao.findAll();
        assertEquals(3, all.size());
    }

    /**
     * Find cart returns correct cart.
     */
    @Test
    public void findCart_ReturnsCorrectCart() {
        Order cart = new Order(1, pharmacy1);
        cart.setState(OrderState.DRAFT);
        dao.saveDraft(cart);

        Order otherCart = new Order(2, pharmacy2);
        otherCart.setState(OrderState.DRAFT);
        dao.saveDraft(otherCart);

        assertEquals(cart, dao.findCart(pharmacy1));

        Pharmacy emptyPharma = new Pharmacy("Empty", 100, new Pharmacist("A","B","c",1,"1"), null, "1");
        assertNull(dao.findCart(emptyPharma));
    }


    /**
     * Find by state covers all branches.
     */
    @Test
    public void findByState_CoversAllBranches() {
        Order draft = new Order(1, pharmacy1); draft.setState(OrderState.DRAFT);
        Order completed = new Order(2, pharmacy1); completed.setState(OrderState.COMPLETED);
        Order pending = new Order(3, pharmacy1); pending.setState(OrderState.PENDING);

        dao.saveDraft(draft);
        dao.saveCompleted(completed);
        dao.savePending(pending);

        List<Order> resCompleted = dao.findByState(OrderState.COMPLETED);
        assertEquals(1, resCompleted.size());
        assertEquals(completed, resCompleted.get(0));

        List<Order> resDraft = dao.findByState(OrderState.DRAFT);
        assertEquals(1, resDraft.size());
        assertEquals(draft, resDraft.get(0));

        List<Order> resPending = dao.findByState(OrderState.PENDING);
        assertEquals(1, resPending.size());
        assertEquals(pending, resPending.get(0));
    }

    /**
     * Find by pharmacy and state covers all branches.
     */
    @Test
    public void findByPharmacyAndState_CoversAllBranches() {

        Order completed = new Order(1, pharmacy1); completed.setState(OrderState.COMPLETED);
        dao.saveCompleted(completed);

        Order draft = new Order(2, pharmacy1); draft.setState(OrderState.DRAFT);
        dao.saveDraft(draft);

        Order pending = new Order(3, pharmacy1); pending.setState(OrderState.PENDING);
        dao.savePending(pending);

        Order canceled = new Order(4, pharmacy1); canceled.setState(OrderState.CANCELED);
        dao.savePending(canceled);

        Order other = new Order(5, pharmacy2); other.setState(OrderState.DRAFT);
        dao.saveDraft(other);

        List<Order> resComp = dao.findByPharmacyAndState(pharmacy1, OrderState.COMPLETED);
        assertEquals(1, resComp.size());
        assertEquals(completed, resComp.get(0));

        List<Order> resDraft = dao.findByPharmacyAndState(pharmacy1, OrderState.DRAFT);
        assertEquals(1, resDraft.size());
        assertEquals(draft, resDraft.get(0));

        List<Order> resPending = dao.findByPharmacyAndState(pharmacy1, OrderState.PENDING);
        assertEquals(1, resPending.size());
        assertEquals(pending, resPending.get(0));

        List<Order> resCanceled = dao.findByPharmacyAndState(pharmacy1, OrderState.CANCELED);
        assertEquals(1, resCanceled.size());
        assertEquals(canceled, resCanceled.get(0));
    }


    /**
     * Complete logic.
     */
    @Test
    public void complete_Logic() {
        Order draftOrder = new Order(1, pharmacy1);
        draftOrder.setState(OrderState.DRAFT);
        dao.saveDraft(draftOrder);

        dao.complete(draftOrder);

        assertFalse(OrderDAOMemory.draft_entities.contains(draftOrder));
        assertTrue(OrderDAOMemory.pending_entities.contains(draftOrder));
        assertEquals(OrderState.PENDING, draftOrder.getState());

        Order pendingOrder = new Order(2, pharmacy1);
        pendingOrder.setState(OrderState.PENDING);
        dao.savePending(pendingOrder);

        dao.complete(pendingOrder);

        assertTrue(OrderDAOMemory.pending_entities.contains(pendingOrder));
        assertEquals(OrderState.PENDING, pendingOrder.getState());

        Order unknown = new Order(99, pharmacy1);
        dao.complete(unknown);
    }

    /**
     * Next id calculations.
     */
    @Test
    public void nextId_Calculations() {
        assertEquals(1, dao.nextId());

        dao.saveDraft(new Order(10, pharmacy1));
        dao.saveCompleted(new Order(20, pharmacy1));
        dao.savePending(new Order(15, pharmacy1));

        assertEquals(21, dao.nextId());
    }
}
