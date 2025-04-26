package de.likeherotozero.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pending_emissions")
public class PendingEmission implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String iso;
    
    private int year;
    
    @Column(name = "emission_value")  // Add this annotation to match database column name
    private double emissionValue;
    
    @Column(name = "submission_date")  // Add this annotation
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate;
    
    @Column(name = "scientist_username")  // Add this annotation
    private String scientistUsername;
    
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;
    
    @ManyToOne
    @JoinColumn(name = "country_iso")
    private Country country;
    
    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getEmissionValue() {
        return emissionValue;
    }

    public void setEmissionValue(double emissionValue) {
        this.emissionValue = emissionValue;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getScientistUsername() {
        return scientistUsername;
    }

    public void setScientistUsername(String scientistUsername) {
        this.scientistUsername = scientistUsername;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}

