package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.List;
import gr.softeng.team09.dao.PharmacyDAO;
import gr.softeng.team09.domain.Pharmacy;

/**
 * The type Pharmacy dao memory.
 */
public class PharmacyDAOMemory implements PharmacyDAO {

    /**
     * The constant entitiesPharmacy.
     */
    public static List<Pharmacy> entitiesPharmacy = new ArrayList<>();

    @Override
    public void save(Pharmacy pharmacy) {
        if (pharmacy == null) return;
        if (!entitiesPharmacy.contains(pharmacy)) {
            MemoryStore.addPharmacy(pharmacy);
            entitiesPharmacy.add(pharmacy);
        }
    }

    @Override
    public Pharmacy findByAfm(String afm) {
        for (Pharmacy p : entitiesPharmacy) {
            if (String.valueOf(p.getAfm()).equals(afm)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Pharmacy findByName(String name) {
        for (Pharmacy p : entitiesPharmacy) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean contains(String afm) {
        return findByAfm(afm) != null;
    }
}