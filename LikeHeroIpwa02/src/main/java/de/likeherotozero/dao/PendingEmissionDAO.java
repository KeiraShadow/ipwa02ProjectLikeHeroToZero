package de.likeherotozero.dao;

//Your entity classes
import de.likeherotozero.entities.PendingEmission;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Named
@ApplicationScoped
public class PendingEmissionDAO {
    
    private EntityManager entityManager;
    
    public PendingEmissionDAO() {
        try {
            System.out.println("Initializing PendingEmissionDAO...");
            entityManager = Persistence.createEntityManagerFactory("likeherotozero").createEntityManager();
            System.out.println("PendingEmissionDAO initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing PendingEmissionDAO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public List<PendingEmission> getPendingEmissions() {
        try {
            System.out.println("Fetching all pending emissions...");
            List<PendingEmission> pendingEmissions = entityManager.createQuery(
                "SELECT pe FROM PendingEmission pe WHERE pe.status = :status",
                PendingEmission.class)
                .setParameter("status", PendingEmission.ApprovalStatus.PENDING)
                .getResultList();
            System.out.println("Found " + pendingEmissions.size() + " pending emissions");
            return pendingEmissions;
        } catch (Exception e) {
            System.err.println("Error fetching pending emissions: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public PendingEmission findById(Long id) {
        try {
            System.out.println("Searching for pending emission with ID: " + id);
            PendingEmission pendingEmission = entityManager.find(PendingEmission.class, id);
            System.out.println("Pending emission found: " + (pendingEmission != null));
            return pendingEmission;
        } catch (Exception e) {
            System.err.println("Error finding pending emission: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void persist(PendingEmission pendingEmission) {
        try {
            System.out.println("Persisting new pending emission...");
            EntityTransaction transaction = getAndBeginTransaction();
            entityManager.persist(pendingEmission);
            transaction.commit();
            System.out.println("Pending emission persisted successfully");
        } catch (Exception e) {
            System.err.println("Error persisting pending emission: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public void merge(PendingEmission pendingEmission) {
        try {
            System.out.println("Updating pending emission...");
            EntityTransaction transaction = getAndBeginTransaction();
            entityManager.merge(pendingEmission);
            transaction.commit();
            System.out.println("Pending emission updated successfully");
        } catch (Exception e) {
            System.err.println("Error updating pending emission: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
    }
}
