package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.dao.ClerkDAO;
import gr.softeng.team09.domain.Clerk;

/**
 * The type Clerk dao memory.
 */
public class ClerkDAOMemory implements ClerkDAO {

    /**
     * The constant entitiesClerk.
     */
    public static final List<Clerk> entitiesClerk = new ArrayList<>();

    @Override
    public void save(Clerk clerk) {
        if (clerk == null) return;
        if (!entitiesClerk.contains(clerk)) {
            MemoryStore.addClerk(clerk);
            entitiesClerk.add(clerk);
        }
    }

    @Override
    public Clerk findByEmailAndPassword(String email, String password) {
        for (Clerk c : entitiesClerk) {
            if (c.getEmail().equals(email) &&
                    c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public boolean exists(String email) {
        for (Clerk c : entitiesClerk) {
            if (c.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
