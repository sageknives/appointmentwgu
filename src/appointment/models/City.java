package appointment.models;

import java.time.LocalDateTime;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sagegatzke
 */
public class City extends Base implements CityInterface {

    private int cityId;
    private String city;
    private CountryInterface country;

    @Override
    public int getCityId() {
        return this.cityId;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public CountryInterface getCountry() {
        return this.country;
    }

    @Override
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public void setCountry(CountryInterface country) {
        this.country = country;
    }

    public City(int cityId, String city, CountryInterface country, String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdated) {
        super(createdBy, createdDate, lastUpdatedBy, lastUpdated);
        this.cityId = cityId;
        this.city = city;
        this.country = country;
    }

    public City() {
        super();
        this.cityId = -1;
    }
}
