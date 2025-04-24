import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "emissions")
public class Emission implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private int id;
    
    private String iso;  
    
    private int year;
    
    @Column(name = "emission_value")
    private double emissionValue;  
    
    @Column(name = "last_update")
    private Date lastUpdate;
    
    @ManyToOne
    @JoinColumn(name = "iso", insertable = false, updatable = false)
    private Country country;  

    // Default constructor required by JPA
    public Emission() {
    }

    // Constructor with main fields
    public Emission(String iso, int year, double emissionValue, Date lastUpdate) {
        this.iso = iso;
        this.year = year;
        this.emissionValue = emissionValue;
        this.lastUpdate = lastUpdate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    // hashCode and equals based on id
    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Emission emission = (Emission) obj;
        return id == emission.id;
    }

    // toString method
    @Override
    public String toString() {
        return "Emission{" +
                "id=" + id +
                ", iso='" + iso + '\'' +
                ", year=" + year +
                ", emissionValue=" + emissionValue +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}







