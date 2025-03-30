import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transaction;

import java.util.List;

public class RandomBewertungen {


    public static void main(String[] args) {

        new RandomBewertungen().run();
    }

    void run() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("onlineshop").createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        List<Artikel> artikeln = (List<Artikel>) entityManager.createQuery("SELECT a from Artikel a").getResultList();
        transaction.begin();
        for(Artikel artikel: artikeln) {
            addBewertung(entityManager, new Bewertung("Wow", Math.floor(Math.random()*10)), artikel);
            addBewertung(entityManager,  new Bewertung("Baf", Math.floor(Math.random()*10)), artikel);
            addBewertung(entityManager,  new Bewertung("buf", Math.floor(Math.random()*10)), artikel);
        }
        transaction.commit();
    }

    private void addBewertung(EntityManager entityManager, Bewertung bewertung, Artikel artikel) {
        entityManager.persist(bewertung);
        artikel.addBewertung(bewertung);
        entityManager.merge(artikel);
    }
}