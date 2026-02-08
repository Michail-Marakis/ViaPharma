package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.domain.Backorder;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacy;

/**
 * The type Backorder dao memory.
 */
public class BackorderDAOMemory implements BackorderDAO {

    private static final Queue<Backorder> fifo = new LinkedList<>();
    private static final List<Backorder> all = new ArrayList<>();
    private boolean flag = false;

    @Override
    public void enqueue(Backorder backorder) {
        if (backorder == null) return;
        fifo.add(backorder);
        upsert(backorder);
        setFlag(true);
    }

    @Override
    public Backorder poll() {
        return fifo.poll();
    }

    @Override
    public List<Backorder> findAll() {
        return new ArrayList<>(all);
    }

    @Override
    public List<Backorder> findByState(OrderState state) {
        List<Backorder> res = new ArrayList<>();
        for (Backorder b : all) {
            if (b.getState() == state) res.add(b);
        }
        return res;
    }

    @Override
    public List<Backorder> findByPharmacy(Pharmacy pharmacy) {
        List<Backorder> res = new ArrayList<>();
        if (pharmacy == null) return res;
        for (Backorder b : all) {
            if (pharmacy.equals(b.getPharmacy())) res.add(b);
        }
        return res;
    }

    @Override
    public Backorder find(int id) {
        for (Backorder b : all) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    @Override
    public void save(Backorder backorder) {
        if (backorder == null) return;
        upsert(backorder);
    }

    private void upsert(Backorder backorder) {
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == backorder.getId()) {
                all.set(i, backorder);
                return;
            }
        }
        all.add(backorder);
    }

    @Override
    public void setFlag(boolean flag){
        this.flag = flag;
    }

    @Override
    public boolean getFlag(){
        return flag;
    }
}
