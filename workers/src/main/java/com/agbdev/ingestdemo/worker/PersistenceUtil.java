package com.agbdev.ingestdemo.worker;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtil {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("store-content");

    public static void persist(final Object obj) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(obj);
		em.getTransaction().commit();
		em.close();
	}

    public static void list(final Class<?> entityClass) {
    	EntityManager em = emf.createEntityManager();
    	List<?> result = em.createQuery( "from Movies", entityClass).getResultList();
    	for (Object obj : result) {
    	    System.out.println(obj);
    	}
    	em.getTransaction().commit();
    	em.close();
    }
}
