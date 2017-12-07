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
public interface CityInterface extends BaseInterface {

    String getCity();

    int getCityId();

    CountryInterface getCountry();

    void setCity(String city);

    void setCityId(int cityId);

    void setCountry(CountryInterface country);
    
}
