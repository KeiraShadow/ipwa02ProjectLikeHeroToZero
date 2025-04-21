import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ApplicationScoped
public class EmissionsManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    EmissionDAO emissionDAO;

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

public EmissionsManager() {
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

/**
 * Gets the latest emissions for a specific country
 */
public Emission getLatestEmissionForCountry(String countryIso) {
    return emissionDAO.getLatestEmission(countryIso);
}

/**
 * Gets all available countries
 */
public List<Country> getAllCountries() {
    return emissionDAO.getAllCountries();
}
}