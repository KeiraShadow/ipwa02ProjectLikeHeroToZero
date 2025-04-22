import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "countries")
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private String iso; 
    
    @Column(name = "country_name")
    private String countryName;
    
    private String unit;
    
    @OneToMany(mappedBy = "country")
    private List<Emission> emissions; 

    // Default constructor required by JPA
    public Country() {
    }

    // Constructor with main fields
    public Country(String iso, String countryName, String unit) {
        this.iso = iso;
        this.countryName = countryName;
        this.unit = unit;
    }

    // Getters and Setters
    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Emission> getEmissions() {
        return emissions;
    }

    public void setEmissions(List<Emission> emissions) {
        this.emissions = emissions;
    }

    // hashCode and equals based on ISO (primary key)
    @Override
    public int hashCode() {
        return iso != null ? iso.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Country country = (Country) obj;
        return iso != null && iso.equals(country.iso);
    }

    @Override
    public String toString() {
        return "Country{" +
                "iso='" + iso + '\'' +
                ", countryName='" + countryName + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
