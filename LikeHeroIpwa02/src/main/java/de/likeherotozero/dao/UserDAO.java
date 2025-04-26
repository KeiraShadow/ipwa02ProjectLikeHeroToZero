package de.likeherotozero.dao;

//Your entity classes
import de.likeherotozero.entities.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

@Named
@ApplicationScoped
public class UserDAO {
    
    private EntityManager entityManager;
    
    public UserDAO() {
        try {
            System.out.println("Initializing UserDAO...");
            entityManager = Persistence.createEntityManagerFactory("likeherotozero").createEntityManager();
            System.out.println("UserDAO initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing UserDAO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public User findByUsername(String username) {
        try {
            System.out.println("Searching for user: " + username);
            User user = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username",
                User.class)
                .setParameter("username", username)
                .getSingleResult();
            System.out.println("User found: " + (user != null));
            return user;
        } catch (NoResultException e) {
            System.out.println("No user found with username: " + username);
            return null;
        } catch (Exception e) {
            System.err.println("Error finding user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
 }

}


