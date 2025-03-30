import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Named
@ApplicationScoped
public class ArtikelDAO {

    EntityManager entityManager;

    CriteriaBuilder criteriaBuilder;



    public ArtikelDAO() {
        try {
            entityManager = Persistence.createEntityManagerFactory("onlineshop").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();

            // init if need be
            long count = getArtikelCount();
            System.err.println("Wir haben " + count+ " Artikeln.");

            if(count == 0) {
                System.err.println("Initialisierung der Daten.");
                EntityTransaction t = getAndBeginTransaction();
                for(Artikel art : Shop.baseSortiment) {
                    persist(art);
                }
                t.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public long getArtikelCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Artikel.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public Artikel getArtikelAtIndex(int pos) {
        CriteriaQuery<Artikel> cq = criteriaBuilder.createQuery(Artikel.class);
        Root<Artikel> variableRoot = cq.from(Artikel.class);
        return entityManager.createQuery(cq).setMaxResults(1).setFirstResult(pos).getSingleResult();
    }

    public List<String> getAlleBilder() {
        return entityManager.createQuery("SELECT a.bild FROM Artikel a GROUP BY a.bild", String.class).getResultList();
    }

    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction tran = entityManager.getTransaction();
        tran.begin();
        return tran;
    }

    public void merge(Artikel art) {
        entityManager.merge(art);
    }
    public void persist(Artikel art) {
        entityManager.persist(art);
    }

    public void removeArtikel(Artikel art) {
        // TODO: use createCriteriaDelete
    }

    public static void main(String[] args) {
        ArtikelDAO dao = new ArtikelDAO();
        System.err.println("Wir haben " + dao.getArtikelCount() + " Artikeln.");
    }



}
