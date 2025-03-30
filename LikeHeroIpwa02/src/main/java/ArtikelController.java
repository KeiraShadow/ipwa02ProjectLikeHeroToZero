import java.io.Serializable;
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@ViewScoped
public class ArtikelController implements Serializable
{
    private int index = 0;

    @Inject
    ArtikelDAO artikelDAO;

    @Inject
    CurrentUser currentUser;

    Artikel artikel;

    public Artikel getArtikel()
    {
        return artikel = artikelDAO.getArtikelAtIndex(index);
    }

    public void vor()
    {
        System.err.println("Saving Artikel " + artikel.getName());
        EntityTransaction t = artikelDAO.getAndBeginTransaction();
        artikelDAO.merge(artikel);
        t.commit();
      if (index < artikelDAO.getArtikelCount() - 1) {
        index++;
      }
    }

    public void zurueck()
    {
        System.err.println("Saving Artikel " + artikel.getName());
        EntityTransaction t = artikelDAO.getAndBeginTransaction();
        artikelDAO.merge(artikel);
        t.commit();
      if (index > 0) {
        index--;
      }
    }

    public void removeArtikel() {
        if(artikelDAO.getArtikelCount()>0)
            artikelDAO.removeArtikel(artikel);
    }

    public int getIndex()
    {
        return index;
    }

    public  int getMaxIndex() {
    	return (int) artikelDAO.getArtikelCount()-1;
    }


    public List<String> getAlleBilder() {
        return artikelDAO.getAlleBilder();
    }
}
