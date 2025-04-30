package de.likeherotozero.beans;

//For entity classes
import de.likeherotozero.entities.Emission;
import de.likeherotozero.entities.PendingEmission;
import de.likeherotozero.entities.Country;

//For DAOs
import de.likeherotozero.dao.EmissionDAO;
import de.likeherotozero.dao.PendingEmissionDAO;

import java.io.Serializable;
import java.util.List;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import java.util.Date; 

@Named
@ViewScoped
public class EmissionController implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private EmissionDAO emissionDAO;
    
    @Inject
    private PendingEmissionDAO pendingEmissionDAO;
    
    @Inject
    private LoginController loginController;
    
    @Inject
    private PendingEmission lastSubmittedEmission;
    
    
    private List<Emission> allEmissions2021;
    private Country selectedCountry;
    private List<Country> allCountries;
    private double trend;
    private Emission emission = new Emission(); // Single declaration for emission
    private int pageSize = 10; // Default page size
    private int currentPage = 0;
    private List<Emission> allEmissions2020;
    private Emission lastSavedEmission;
    
    @PostConstruct
    public void init() {
        try {
            // Initialize lists to prevent NPE
            allEmissions2021 = new ArrayList<>();
            allEmissions2020 = new ArrayList<>();
            allCountries = new ArrayList<>();
            lastSubmittedEmission = null;
            
            // Load initial data
            loadEmissions2021();
            loadEmissions2020();
            allCountries = emissionDAO.getAllCountries();
            
            System.out.println("EmissionDAO is " + (emissionDAO == null ? "null" : "not null"));
            System.out.println("Loaded " + (allCountries != null ? allCountries.size() : 0) + " countries");
        } catch (Exception e) {
            System.err.println("Error in init(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    

    public void setLastSavedEmission(Emission lastSavedEmission) {
        this.lastSavedEmission = lastSavedEmission;
    }
    
    private void loadEmissions2021() {
        try {
            allEmissions2021 = emissionDAO.getAllEmissionsForYear(2021);
        } catch (Exception e) {
            e.printStackTrace();
            allEmissions2021 = new ArrayList<>();
        }
    }
    
    private void loadEmissions2020() {
        try {
            allEmissions2020 = emissionDAO.getAllEmissionsForYear(2020);
        } catch (Exception e) {
            e.printStackTrace();
            allEmissions2020 = new ArrayList<>();
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
            System.out.println("Save emission method called");
            if (selectedCountry != null) {
                System.out.println("Selected country: " + selectedCountry.getIso());
                
                // Create pending emission
                PendingEmission pendingEmission = new PendingEmission();
                pendingEmission.setIso(selectedCountry.getIso());
                pendingEmission.setYear(emission.getYear());
                pendingEmission.setEmissionValue(emission.getEmissionValue());
                pendingEmission.setCountry(selectedCountry);
                pendingEmission.setSubmissionDate(new Date());
                
                // Check if loginController is properly injected
                if (loginController == null) {
                    throw new IllegalStateException("LoginController not injected");
                }
                pendingEmission.setScientistUsername(loginController.getUsername());
                pendingEmission.setStatus(PendingEmission.ApprovalStatus.PENDING);
                
                System.out.println("About to persist pending emission");
                
                // Save to pending table
                pendingEmissionDAO.persist(pendingEmission);
                this.lastSubmittedEmission = pendingEmission; // Use this. to be explicit
                
                System.out.println("Emission saved successfully");
                
                // Reset form
                emission = new Emission();
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Daten zur Überprüfung eingereicht", null));
                    
                return null;
            } else {
                System.out.println("Selected country is null");
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Bitte wählen Sie ein Land aus", null));
            }
        } catch (Exception e) {
            System.err.println("Error saving emission: " + e.getMessage());
            e.printStackTrace();
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
    
    public Emission getLastSavedEmission() {
        return lastSavedEmission;
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
    
    
 // Pagination methods
    public List<Country> getPaginatedEmissions() {
        int fromIndex = currentPage * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allCountries.size());
        return allCountries.subList(fromIndex, toIndex);
    }
    
    public double getEmissionValueForYear(String iso, int year) {
        List<Emission> emissionList = year == 2020 ? allEmissions2020 : allEmissions2021;
        return emissionList.stream()
                .filter(e -> e.getIso().equals(iso))
                .findFirst()
                .map(Emission::getEmissionValue)
                .orElse(0.0);
    }
    
    public String getEmissionTrend(String iso) {
        double emission2020 = getEmissionValueForYear(iso, 2020);
        double emission2021 = getEmissionValueForYear(iso, 2021);
        
        if (emission2020 == 0) return "N/A";
        
        double trend = ((emission2021 - emission2020) / emission2020) * 100;
        return String.format("%.1f%%", trend);
    }
    
    public String getTrendStyleClass(String iso) {
        double emission2020 = getEmissionValueForYear(iso, 2020);
        double emission2021 = getEmissionValueForYear(iso, 2021);
        return emission2021 > emission2020 ? "text-danger" : "text-success";
    }
    
    public void nextPage() {
        if (!isLastPage()) {
            currentPage++;
        }
    }
    
    public void previousPage() {
        if (!isFirstPage()) {
            currentPage--;
        }
    }
    
    public boolean isFirstPage() {
        return currentPage == 0;
    }
    
    public boolean isLastPage() {
        return (currentPage + 1) * pageSize >= allCountries.size();
    }
    
    public int getFirstRowIndex() {
        return currentPage * pageSize;
    }
    
    public int getLastRowIndex() {
        return Math.min(getFirstRowIndex() + pageSize, allCountries.size());
    }
    
    public int getTotalRows() {
        return allCountries.size();
    }
    
   
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.currentPage = 0; // Reset to first page when changing page size
    }
    public PendingEmission getLastSubmittedEmission() {
        return lastSubmittedEmission;
    }

}


