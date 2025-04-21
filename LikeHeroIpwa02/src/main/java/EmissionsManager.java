import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ApplicationScoped
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    ArtikelDAO artikelDAO;

    private final String[][] users =
            new String[][]{
                    // Admin user
                    new String[]{"koch",
                            "+INdDt2JaxoJLHzD4iAlWPYMJA0uJhusP37DvMHBKmen15EMj1Vn7BAxWS1TYFniKFKjuSyIEFbxy9jSx4d8Tw==",
                            "admin"},
                    // Scientist user
                    new String[]{"scientist1",
                            "jCgkZGVP4sDSY4cyUiGYWpg1x9ciU06KTp4LnvdZCxQPk3jH+0lKEh9ZO9BCvvEZL6aL7ZsYUrrSYuA0XC8W+w==",
                            "scientist"}
            };

    static final List<Artikel> baseSortiment = Arrays.asList(new Artikel[]{
            new Artikel("Pantoffeln \"Rudolph\"",
                    "Wunderschöne Filzpantoffeln, in beige Farbe mit einem braunen und schwarzen Kringel. Sehr angenehm für kalte Wintertage.",
                    "filzschuhe.jpg", (new GregorianCalendar(2012, 11, 23).getTime())),
            new Artikel("Handtasche \"Cosmopolita\"",
                    "Modische Filz-Handtasche mit praktischer Cocktailglas-Halterung. Irgendwie kommen wir nie aus dem Haus ohne solchen nützlichen Accessoire.",
                    "handtasche.jpg", (new GregorianCalendar(2010, 10, 3).getTime())),
            new Artikel("Filz-Hase \"Moe\"",
                    "Ein putziger Hase aus Filz zur Dekoration. Er lässt sich gern am Rand seines Büros stellen, um Mut zu geben.", "hase.jpg",
                    (new GregorianCalendar(2013, 11, 31).getTime())),
            new Film("Laurence d'Arabie", "Wahnsinnige langes und spannendes Film. Ich verspreche es Ihnen. Aber wirklich.", "laurence.png", "laurence-trailer.mp4")
    });

    public Shop() {
    }

    static String hashPassword(String name, String pass, String salt) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digester.digest((name + pass + salt)
                    .getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(hashBytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void validateUsernameAndPassword(CurrentUser currentUser, String name, String pass, String salt) {
        String passHash = hashPassword(name, pass, salt);
        currentUser.reset();
        
        for (String[] user : users) {
            if (user[0].equals(name)) {
                if (user[1].equals(passHash)) {
                    switch (user[2]) {
                        case "admin":
                            currentUser.setAdmin(true);
                            return;
                        case "scientist":
                            currentUser.setScientist(true);
                            return;
                        default:
                            throw new RuntimeException("Benutzer " + name + " ist falsch angelegt.");
                    }
                }
            }
        }
    }
}
