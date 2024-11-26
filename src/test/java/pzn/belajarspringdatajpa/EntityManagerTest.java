package pzn.belajarspringdatajpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EntityManagerTest {
    //because entity manager already set by spring, we can test it

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void testEntityManagerFactory() {
        Assertions.assertNotNull(entityManagerFactory);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Assertions.assertNotNull(entityManager);

        entityManager.close();
    }
}
