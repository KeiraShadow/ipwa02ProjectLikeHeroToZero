package de.likeherotozero.utils;

//Your entity classes
import de.likeherotozero.entities.Country;

import de.likeherotozero.dao.EmissionDAO;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
@FacesConverter(value = "countryConverter", managed = true)
public class CountryConverter implements Converter<Country> {
    
    @Inject
    private EmissionDAO emissionDAO;
    
    @Override
    public Country getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            // Debug log
            System.out.println("Converting value: " + value);
            System.out.println("EmissionDAO is " + (emissionDAO == null ? "null" : "not null"));
            
            return emissionDAO.getAllCountries()
                    .stream()
                    .filter(country -> country.getIso().equals(value))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.err.println("Error in converter: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Country country) {
        if (country == null) {
            return "";
        }
        return country.getIso();
    }
}
