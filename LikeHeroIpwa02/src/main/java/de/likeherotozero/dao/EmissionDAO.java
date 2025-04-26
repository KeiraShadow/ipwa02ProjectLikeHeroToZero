package de.likeherotozero.dao;

//For your entity classes
import de.likeherotozero.entities.Emission;
import de.likeherotozero.entities.Country;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;


@Named
@ApplicationScoped
public class EmissionDAO {
    
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;
    public List<Emission> getAllEmissionsForYear(int year) {
        try {
            return entityManager.createQuery(
                "SELECT e FROM Emission e WHERE e.year = :year ORDER BY e.country.countryName",
                Emission.class)
                .setParameter("year", year)
                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public EmissionDAO() {
        try {
            entityManager = Persistence.createEntityManagerFactory("likeherotozero").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();

            // Initialize if needed
            long count = getEmissionCount();
            System.out.println("Current emission count: " + count);

            if(count == 0) {
                System.out.println("Initializing emission data...");
                EntityTransaction t = getAndBeginTransaction();
                // Add initialization code if needed
                t.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Emission getLatestEmission(String countryIso) {
        try {
            return entityManager.createQuery(
                "SELECT e FROM Emission e " +
                "WHERE e.iso = :iso " +
                "AND e.year = 2021", // Explicitly use 2021 as the latest year
                Emission.class)
                .setParameter("iso", countryIso)
                .getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No emission data found for country: " + countryIso);
            return null;
        }
    }

    public Emission getEmissionByYear(String countryIso, int year) {
        try {
            return entityManager.createQuery(
                "SELECT e FROM Emission e " +
                "WHERE e.iso = :iso AND e.year = :year",
                Emission.class)
                .setParameter("iso", countryIso)
                .setParameter("year", year)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long getEmissionCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Emission.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public Emission getEmissionAtIndex(int pos) {
        CriteriaQuery<Emission> cq = criteriaBuilder.createQuery(Emission.class);
        Root<Emission> variableRoot = cq.from(Emission.class);
        return entityManager.createQuery(cq)
                          .setMaxResults(1)
                          .setFirstResult(pos)
                          .getSingleResult();
    }

    public List<Country> getAllCountries() {
        return entityManager.createQuery(
            "SELECT c FROM Country c ORDER BY c.countryName", 
            Country.class)
            .getResultList();
    }

    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
    }

    public void merge(Emission emission) {
        if (emission != null) {
            try {
                emission.setLastUpdate(new Date()); // Update the last modified timestamp
                entityManager.merge(emission);
            } catch (Exception e) {
                System.err.println("Error merging emission: " + e.getMessage());
                throw e;
            }
        }
    }

    public void persist(Emission emission) {
        if (emission != null) {
            try {
                emission.setLastUpdate(new Date()); // Set initial timestamp
                entityManager.persist(emission);
            } catch (Exception e) {
                System.err.println("Error persisting emission: " + e.getMessage());
                throw e;
            }
        }
    }
}


