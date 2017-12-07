/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

import java.time.LocalDateTime;

/**
 *
 * @author sagegatzke
 */
public class Country extends Base implements CountryInterface {

    private int countryId;
    private String country;

    @Override
    public int getCountryId() {
        return this.countryId;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    public Country(int countryId, String country, String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdated) {
        super(createdBy, createdDate, lastUpdatedBy, lastUpdated);
        this.countryId = countryId;
        this.country = country;
    }

    public Country() {
        super();
        this.countryId = -1;
    }
}
