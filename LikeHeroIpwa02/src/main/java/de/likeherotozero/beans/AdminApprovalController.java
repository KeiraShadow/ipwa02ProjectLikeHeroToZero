package de.likeherotozero.beans;

import java.io.Serializable;
import java.util.List;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import java.util.Date; 

//For entity classes
import de.likeherotozero.entities.Emission;
import de.likeherotozero.entities.PendingEmission;

//For DAOs
import de.likeherotozero.dao.EmissionDAO;
import de.likeherotozero.dao.PendingEmissionDAO;

@Named
@ViewScoped
public class AdminApprovalController implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Inject
    private PendingEmissionDAO pendingEmissionDAO;
    
    @Inject
    private EmissionDAO emissionDAO;
    
    private List<PendingEmission> pendingEmissions;
    
    @PostConstruct
    public void init() {
        loadPendingEmissions();
    }
    
    public void loadPendingEmissions() {
        pendingEmissions = pendingEmissionDAO.getPendingEmissions();
    }
    
    public void approveEmission(PendingEmission pending) {
        try {
            // Create or update emission in main database
            Emission emission = new Emission();
            emission.setIso(pending.getIso());
            emission.setYear(pending.getYear());
            emission.setEmissionValue(pending.getEmissionValue());
            emission.setLastUpdate(new Date());
            
            // Check if entry exists
            Emission existingEmission = emissionDAO.getEmissionByYear(
                pending.getIso(), pending.getYear());
            
            if (existingEmission != null) {
                existingEmission.setEmissionValue(pending.getEmissionValue());
                emissionDAO.merge(existingEmission);
            } else {
                emissionDAO.persist(emission);
            }
            
            // Update pending status
            pending.setStatus(PendingEmission.ApprovalStatus.APPROVED);
            pendingEmissionDAO.merge(pending);
            
            // Reload pending list
            loadPendingEmissions();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Änderung erfolgreich freigegeben", null));
                
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Fehler bei der Freigabe: " + e.getMessage(), null));
        }
    }
    
    public void rejectEmission(PendingEmission pending) {
        try {
        	pending.setStatus(PendingEmission.ApprovalStatus.REJECTED);
        	
            pendingEmissionDAO.merge(pending);
            loadPendingEmissions();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Änderung abgelehnt", null));
                
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Fehler bei der Ablehnung: " + e.getMessage(), null));
        }
    }
    
    public List<PendingEmission> getPendingEmissions() {
        return pendingEmissions;
    }
}
