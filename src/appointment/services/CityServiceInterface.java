/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.CityInterface;

/**
 *
 * @author sagegatzke
 */
public interface CityServiceInterface {

    CityInterface addCity(CityInterface city);

    CityInterface[] getCities(int countryId);
    
}
