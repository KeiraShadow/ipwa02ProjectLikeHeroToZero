import java.io.Serializable;
import java.util.List;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import java.util.Date;  // Added import for Date resolution

@Named
@ViewScoped
public class EmissionController implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private EmissionDAO emissionDAO;
    
    private List<Emission> allEmissions2021;
    private Country selectedCountry;
    private List<Country> allCountries;
    private double trend;
    private Emission emission = new Emission(); // Single declaration for emission
    
    @PostConstruct
    public void init() {
        try {
            // Initialize lists to prevent NPE
            allEmissions2021 = new ArrayList<>();
            allCountries = new ArrayList<>();
            
            // Load initial data
            loadEmissions2021();
            allCountries = emissionDAO.getAllCountries();
            
            System.out.println("EmissionDAO is " + (emissionDAO == null ? "null" : "not null"));
            System.out.println("Loaded " + (allCountries != null ? allCountries.size() : 0) + " countries");
        } catch (Exception e) {
            System.err.println("Error in init(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadEmissions2021() {
        try {
            allEmissions2021 = emissionDAO.getAllEmissionsForYear(2021);
        } catch (Exception e) {
            e.printStackTrace();
            allEmissions2021 = new ArrayList<>();
        }
    }
    
    public void onCountrySelect() {
        if (selectedCountry != null) {
            emission = emissionDAO.getLatestEmission(selectedCountry.getIso());
            if (emission != null) {
                calculateTrend();
            }
        }
    }
    
    private void calculateTrend() {
        if (selectedCountry != null && emission != null) {
            Emission previousYearEmission = emissionDAO.getEmissionByYear(
                selectedCountry.getIso(), 
                emission.getYear() - 1
            );
            
            if (previousYearEmission != null) {
                double currentValue = emission.getEmissionValue();
                double previousValue = previousYearEmission.getEmissionValue();
                trend = ((currentValue - previousValue) / previousValue) * 100;
            }
        }
    }
    
    public String saveEmission() {
        try {
            if (selectedCountry != null) {
                emission.setIso(selectedCountry.getIso());
                emission.setLastUpdate(new Date()); // Now Date is properly imported
                
                // Prüfen ob Eintrag bereits existiert
                Emission existingEmission = emissionDAO.getEmissionByYear(
                    selectedCountry.getIso(), 
                    emission.getYear()
                );
                
                if (existingEmission != null) {
                    // Update existierenden Eintrag
                    existingEmission.setEmissionValue(emission.getEmissionValue());
                    emissionDAO.merge(existingEmission);
                } else {
                    // Neuen Eintrag erstellen
                    emissionDAO.persist(emission);
                }
                
                // Reset Form
                emission = new Emission();
                loadEmissions2021();
                
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Daten erfolgreich gespeichert", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Fehler beim Speichern: " + e.getMessage(), null));
        }
        return null;
    }
    
    // Getters and Setters
    public List<Emission> getAllEmissions2021() {
        return allEmissions2021;
    }
    
    public Country getSelectedCountry() {
        return selectedCountry;
    }
    
    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }
    
    public List<Country> getAllCountries() {
        return allCountries;
    }
    
    public double getCurrentEmissionValue() {
        return emission != null ? emission.getEmissionValue() : 0.0;
    }
    
    public boolean isHasEmissionData() {
        return selectedCountry != null && emission != null;
    }
    
    public Emission getEmission() {
        return emission;
    }

    public void setEmission(Emission emission) {
        this.emission = emission;
    }
}

    
    


