package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.List;
import gr.softeng.team09.dao.PharmacistDAO;
import gr.softeng.team09.domain.Pharmacist;

/**
 * The type Pharmacist dao memory.
 */
public class PharmacistDAOMemory implements PharmacistDAO {

    /**
     * The constant entities.
     */
    public static List<Pharmacist> entities = new ArrayList<>();

    @Override
    public void save(Pharmacist pharmacist) {
        if (!entities.contains(pharmacist)) {
            MemoryStore.addPharmacist(pharmacist);
            entities.add(pharmacist);
        }
    }

    @Override
    public Pharmacist findByEmailAndPassword(String email, String password) {
        for (Pharmacist p : entities) {
            if (p.getEmail().equals(email) && p.getPassword().equals(password)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean exists(String email) {
        for (Pharmacist p : entities) {
            if (p.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}