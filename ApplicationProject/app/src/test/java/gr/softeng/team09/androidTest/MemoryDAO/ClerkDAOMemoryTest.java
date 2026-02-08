package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.memorydao.ClerkDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;

import static org.junit.Assert.*;

/**
 * The type Clerk dao memory test.
 */
public class ClerkDAOMemoryTest {

    private ClerkDAOMemory dao;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        dao = new ClerkDAOMemory();
        ClerkDAOMemory.entitiesClerk.clear();
        MemoryStore.AllClerk.clear();
    }

    /**
     * Save operations full coverage.
     */
    @Test
    public void save_Operations_FullCoverage() {
        dao.save(null);
        assertEquals(0, ClerkDAOMemory.entitiesClerk.size());

        Clerk c = new Clerk("Maria", "Vas", "69", "maria@mail.com", "pass");
        dao.save(c);

        assertEquals(1, ClerkDAOMemory.entitiesClerk.size());
        assertTrue(ClerkDAOMemory.entitiesClerk.contains(c));

        dao.save(c);
        assertEquals("Should ignore duplicates", 1, ClerkDAOMemory.entitiesClerk.size());
    }

    /**
     * Find by email and password full coverage.
     */
    @Test
    public void findByEmailAndPassword_FullCoverage() {
        Clerk c = new Clerk("Maria", "Vas", "69", "maria@mail.com", "pass");
        dao.save(c);

        Clerk found = dao.findByEmailAndPassword("maria@mail.com", "pass");
        assertNotNull(found);
        assertEquals(c, found);

        Clerk wrongPass = dao.findByEmailAndPassword("maria@mail.com", "wrong");
        assertNull(wrongPass);

        Clerk wrongEmail = dao.findByEmailAndPassword("wrong@mail.com", "pass");
        assertNull(wrongEmail);
    }

    /**
     * Exists full coverage.
     */
    @Test
    public void exists_FullCoverage() {
        Clerk c = new Clerk("Maria", "Vas", "69", "maria@mail.com", "pass");
        dao.save(c);

        assertTrue(dao.exists("maria@mail.com"));

        assertFalse(dao.exists("unknown@mail.com"));
    }
}
