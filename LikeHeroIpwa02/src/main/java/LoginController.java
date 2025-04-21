import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class LoginController implements Serializable {
    
	 private static final long serialVersionUID = 1L;
    @Inject
    Shop shop;

    @Inject
    CurrentUser currentUser;

    private static final String salt = "vXsia8c04PhBtnG3isvjlemj7Bm6rAhBR8JRkf2z";

    private String user, password;
    private String tempUsername;
    private String failureMessage = "";

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    // Add getters and setters for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Add getter and setter for failureMessage
    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public void checkLogin() {
        if(!currentUser.isValid()) {
            failureMessage = "Bitte loggen Sie sich ein.";
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "login.xhtml?faces-redirect=true");
        }
    }

    public String logout() {
        currentUser.reset();
        return "login.xhtml?faces-redirect=true";
    }

    public void postValidateUser(ComponentSystemEvent ev) {
        UIInput temp = (UIInput) ev.getComponent();
        this.tempUsername = (String) temp.getValue();
    }

    public void validateLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String password = (String) value;
        shop.validateUsernameAndPassword(currentUser, tempUsername, password, salt);
        if (!currentUser.isValid()) {
            throw new ValidatorException(new FacesMessage("Ungültige Anmeldedaten!"));
        }
    }

    public String login() {
        if (currentUser.isAdmin()) {
            this.failureMessage = "";
            return "backoffice.xhtml?faces-redirect=true";
        } else if (currentUser.isScientist()) {  // Add scientist check
            this.failureMessage = "";
            return "shopclient.xhtml?faces-redirect=true";  // New destination for scientists
        } else {
            this.failureMessage = "Benutzername oder Passwort nicht erkannt.";
            return "";
        }
    }
    

    // Helper method to generate hashed password for scientists
    public static void main(String[] args) {
        if(args.length!=2) {
            System.err.println("Usage: java LoginController username password");
            System.exit(1);
        }
        System.out.println("Generated hash for scientist login:");
        System.out.println(Shop.hashPassword(args[0], args[1], salt));
    }
}