import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class CurrentUser implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Change to private fields with proper encapsulation
    private boolean admin;
    private boolean scientist;   

    public void reset() {
        admin = false;
        scientist = false;
    }

    // Add getters and setters
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isScientist() {
        return scientist;
    }

    public void setScientist(boolean scientist) {
        this.scientist = scientist;
    }

    public boolean isValid() {
        return isAdmin() || isScientist();
    }
}