/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

/**
 *
 * @author sagegatzke
 */
public interface CountryInterface extends BaseInterface {

    String getCountry();

    int getCountryId();

    void setCountry(String country);

    void setCountryId(int countryId);
    
}
