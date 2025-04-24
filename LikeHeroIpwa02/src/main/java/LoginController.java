import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.nio.charset.StandardCharsets; 

  
@Named
@SessionScoped
public class LoginController implements Serializable {
    
    @Inject
    private UserDAO userDAO;
    
    private String username;
    private String password;
    private User currentUser;
    private String failureMessage; // Add this property
    
    // Add these getter and setter methods
    public String getFailureMessage() {
        return failureMessage;
    }
    
    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
    
    public String login() {
        try {
            System.out.println("Login attempt for user: " + username);
            User user = userDAO.findByUsername(username);
            
            if (user != null) {
                System.out.println("User found with role: " + user.getRole());
                System.out.println("Stored password hash: " + user.getPassword());
                
                // Create a test hash to verify our hashing process
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] testBytes = md.digest("password".getBytes(StandardCharsets.UTF_8));
                String testHash = Base64.getEncoder().encodeToString(testBytes);
                System.out.println("Test hash of 'password': " + testHash);
                
                // Hash the actual input password
                md.reset();
                byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
                String hashedPassword = Base64.getEncoder().encodeToString(hashedBytes);
                
                System.out.println("Input password hash: " + hashedPassword);
                System.out.println("Password match: " + hashedPassword.equals(user.getPassword()));
                
                if (hashedPassword.equals(user.getPassword())) {
                    currentUser = user;
                    failureMessage = null;
                    
                    if ("ADMIN".equals(user.getRole())) {
                        return "emissionsManagement.xhtml?faces-redirect=true";
                    } else {
                        return "emissionsManagement.xhtml?faces-redirect=true";
                    }
                }
            }
            
            failureMessage = "Ungültige Anmeldedaten";
            return null;
            
        } catch (Exception e) {
            e.printStackTrace();
            failureMessage = "Login-Fehler: " + e.getMessage();
            return null;
        }
    }

    


        public String logout() {
            currentUser = null;
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "login.xhtml?faces-redirect=true";
        }

    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
    
    public boolean isScientist() {
        return currentUser != null && "SCIENTIST".equals(currentUser.getRole());
    }
}
