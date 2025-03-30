import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public class Film extends Artikel implements Serializable {

    String trailer = null;

    public Film() {

    }

    public Film(String name, String text, String bild, String trailer) {
        super(name, text, bild);
        this.trailer = trailer;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}
